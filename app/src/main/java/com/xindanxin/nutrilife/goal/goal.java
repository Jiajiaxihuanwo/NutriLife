package com.xindanxin.nutrilife.goal;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.xindanxin.nutrilife.R;
import com.xindanxin.nutrilife.firestore.DailyGoalsFirestore;
import com.xindanxin.nutrilife.util.CaloriesViewModel;

import java.util.Map;

public class goal extends Fragment {

    private TextView aguaTomado, aguaTotal;
    private ProgressBar waterProgress;
    private CardView waterCard;
    private ImageView waterGridIcon;
    private TextView waterGridStatus;

    private TextView verduraTomado, verduraTotal;
    private ProgressBar foodProgress;
    private CardView achievementSugar;
    private ImageView foodGridIcon;
    private TextView foodGridStatus;

    private TextView azucarTomado, azucarTotal;
    private ProgressBar proteinProgress;
    private CardView achievementMindful;
    private ImageView proteinGridIcon;
    private TextView proteinGridStatus;

    private TextView nuevaPrueba, vez;
    private ProgressBar newFoodProgress;
    private CardView newFoodCard;
    private ImageView newFoodGridIcon;
    private TextView newFoodGridStatus;

    private CaloriesViewModel caloriesViewModel;
    private int waterGoalTotal = 0;
    private int proteinGoalTotal = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_goal, container, false);
        initializeViews(view);
        caloriesViewModel = new ViewModelProvider(getActivity()).get(CaloriesViewModel.class);
        loadGoalsFromFirestore();
        return view;
    }

    private void initializeViews(View view) {
        aguaTomado = view.findViewById(R.id.agua_tomado);
        aguaTotal = view.findViewById(R.id.agua_total);
        waterProgress = view.findViewById(R.id.water_progress);
        waterCard = view.findViewById(R.id.goal1);
        waterGridIcon = view.findViewById(R.id.water_icon);
        waterGridStatus = view.findViewById(R.id.water_status);

        verduraTomado = view.findViewById(R.id.verdura_tomado);
        verduraTotal = view.findViewById(R.id.verdura_total);
        foodProgress = view.findViewById(R.id.food_progress);
        achievementSugar = view.findViewById(R.id.goal2);
        foodGridIcon = view.findViewById(R.id.plan_icon);
        foodGridStatus = view.findViewById(R.id.plan_status);

        azucarTomado = view.findViewById(R.id.azugar_tomado);
        azucarTotal = view.findViewById(R.id.azucar_total);
        proteinProgress = view.findViewById(R.id.protein_progress);
        achievementMindful = view.findViewById(R.id.goal3);
        proteinGridIcon = view.findViewById(R.id.protein_icon);
        proteinGridStatus = view.findViewById(R.id.protein_status);

        nuevaPrueba = view.findViewById(R.id.nueva_prueba);
        vez = view.findViewById(R.id.vez);
        newFoodProgress = view.findViewById(R.id.new_food_progress);
        newFoodCard = view.findViewById(R.id.goal4);
        newFoodGridIcon = view.findViewById(R.id.new_food_icon);
        newFoodGridStatus = view.findViewById(R.id.new_food_status);
    }

    private void loadGoalsFromFirestore() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DailyGoalsFirestore firestore = new DailyGoalsFirestore(uid);

        firestore.getGoals(goal -> {
            waterGoalTotal = (goal.get("Water") != null) ? goal.get("Water") : 0;
            proteinGoalTotal = (goal.get("Protein") != null) ? goal.get("Protein") : 0;
            loadCurrentData();
        });
    }

    private void loadCurrentData() {
        int waterCurrent = getCurrentWaterIntake();

        caloriesViewModel.getTotalMacros().observe(getViewLifecycleOwner(), macroInfo -> {
            if (macroInfo == null) return;

            int proteinCurrent = macroInfo.getProtein();

            int foodCurrent = 1;
            int foodTotal = 1;
            int newFoodCurrent = 1;
            int newFoodTotal = 1;

            updateAllGoals(waterCurrent, waterGoalTotal, foodCurrent, foodTotal,
                    proteinCurrent, proteinGoalTotal, newFoodCurrent, newFoodTotal);
        });
    }

    private int getCurrentWaterIntake() {
        if (getContext() == null) return 0;

        SharedPreferences prefs = getContext().getSharedPreferences("AguaPrefs", getContext().MODE_PRIVATE);

        return prefs.getInt("progreso", 0);
    }

    private void updateAllGoals(int waterCurrent, int waterTotal,
                                int foodCurrent, int foodTotal,
                                int proteinCurrent, int proteinTotal,
                                int newFoodCurrent, int newFoodTotal) {
        updateWaterGoal(waterCurrent, waterTotal);
        updateFoodGoal(foodCurrent, foodTotal);
        updateProteinGoal(proteinCurrent, proteinTotal);
        updateNewFoodGoal(newFoodCurrent, newFoodTotal);
    }

    private void updateWaterGoal(int current, int total) {
        aguaTomado.setText(String.valueOf(current));
        aguaTotal.setText(String.valueOf(total));

        int progress = (total == 0) ? 0 : (current * 100) / total;
        waterProgress.setProgress(progress);

        int progressColor = (progress >= 100) ? R.color.greenApp : R.color.blueWaterDeshboard_ic;
        waterProgress.getProgressDrawable().setColorFilter(
                ContextCompat.getColor(getContext(), progressColor), PorterDuff.Mode.SRC_IN);

        updateCardStyle(waterCard, waterGridIcon, waterGridStatus, current >= total,
                R.drawable.goal_cup_foreground);
    }

    private void updateFoodGoal(int current, int total) {
        verduraTomado.setText(String.valueOf(current));
        verduraTotal.setText(String.valueOf(total));

        int progress = (total == 0) ? 0 : (current * 100) / total;
        foodProgress.setProgress(progress);

        int progressColor = (progress >= 100) ? R.color.greenApp : R.color.blueWaterDeshboard_ic;
        foodProgress.getProgressDrawable().setColorFilter(
                ContextCompat.getColor(getContext(), progressColor), PorterDuff.Mode.SRC_IN);

        if (foodGridIcon != null && foodGridStatus != null) {
            updateCardStyle(achievementSugar, foodGridIcon, foodGridStatus, current >= total,
                    R.drawable.goal_eat);
        } else {
            if (current >= total) {
                achievementSugar.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                achievementSugar.setBackgroundTintList(
                        ContextCompat.getColorStateList(getContext(), R.color.achievements2Goal));
            } else {
                achievementSugar.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_grey));
            }
        }
    }

    private void updateProteinGoal(int current, int total) {
        azucarTomado.setText(String.valueOf(current));
        azucarTotal.setText(String.valueOf(total));

        int progress = (total == 0) ? 0 : (current * 100) / total;
        proteinProgress.setProgress(progress);

        int progressColor = (progress >= 100) ? R.color.greenApp : R.color.blueWaterDeshboard_ic;
        proteinProgress.getProgressDrawable().setColorFilter(
                ContextCompat.getColor(getContext(), progressColor), PorterDuff.Mode.SRC_IN);

        if (proteinGridIcon != null && proteinGridStatus != null) {
            updateCardStyle(achievementMindful, proteinGridIcon, proteinGridStatus, current >= total,
                    R.drawable.goal_muscle_foreground);
        } else {
            if (current >= total) {
                achievementMindful.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                achievementMindful.setBackgroundTintList(
                        ContextCompat.getColorStateList(getContext(), R.color.achievements2Goal));
            } else {
                achievementMindful.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_grey));
            }
        }
    }

    private void updateNewFoodGoal(int current, int total) {
        nuevaPrueba.setText(String.valueOf(current));
        vez.setText(String.valueOf(total));

        int progress = (total == 0) ? 0 : (current * 100) / total;
        newFoodProgress.setProgress(progress);

        int progressColor = (progress >= 100) ? R.color.greenApp : R.color.blueWaterDeshboard_ic;
        newFoodProgress.getProgressDrawable().setColorFilter(
                ContextCompat.getColor(getContext(), progressColor), PorterDuff.Mode.SRC_IN);

        updateCardStyle(newFoodCard, newFoodGridIcon, newFoodGridStatus, current >= total,
                R.drawable.goal_create_foreground);
    }

    private void updateCardStyle(CardView card, ImageView icon, TextView statusText,
                                 boolean isCompleted, int iconResId) {
        if (card == null) return;

        if (isCompleted) {
            card.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            card.setBackgroundTintList(
                    ContextCompat.getColorStateList(getContext(), R.color.achievements2Goal));

            if (icon != null) {
                icon.setImageResource(iconResId);

                if (icon.getId() == R.id.water_icon) {
                    icon.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.blueWaterDeshboard));
                    icon.setImageTintList(ContextCompat.getColorStateList(getContext(), R.color.goal_tint_water));
                } else if (icon.getId() == R.id.plan_icon) {
                    icon.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.goal_plan));
                    icon.setImageTintList(ContextCompat.getColorStateList(getContext(), R.color.goal_tint_plan));
                } else if (icon.getId() == R.id.protein_icon) {
                    icon.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.goal_protein));
                    icon.setImageTintList(ContextCompat.getColorStateList(getContext(), R.color.goal_tint_protein));
                } else if (icon.getId() == R.id.new_food_icon) {
                    icon.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.goal_create));
                    icon.setImageTintList(ContextCompat.getColorStateList(getContext(), R.color.goal_tint_create));
                }
            }

            if (statusText != null) {
                statusText.setText("✅ Completed");
                statusText.setTextColor(ContextCompat.getColor(getContext(), R.color.greenApp));
            }
        } else {
            card.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_grey));

            if (icon != null) {
                icon.setImageResource(iconResId);

                icon.setBackgroundTintList(
                        ContextCompat.getColorStateList(getContext(), R.color.grey));

                icon.setImageTintList(
                        ContextCompat.getColorStateList(getContext(), R.color.light_grey));
            }

            if (statusText != null) {
                statusText.setText("Not completed");
                statusText.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            }
        }
    }
}