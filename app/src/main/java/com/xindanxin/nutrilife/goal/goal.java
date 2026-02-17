package com.xindanxin.nutrilife.goal;

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
import com.google.firebase.auth.FirebaseUser;
import com.xindanxin.nutrilife.R;
import com.xindanxin.nutrilife.firestore.DailyGoalsFirestore;
import com.xindanxin.nutrilife.util.CaloriesViewModel;

import java.util.Map;
import java.util.Objects;

public class goal extends Fragment {
    private TextView aguaTomado, aguaTotal;
    private ProgressBar waterProgress;
    private CardView waterCard;
    private ImageView waterGridIcon;
    private TextView waterGridStatus;

    private TextView carbTomado, carbTotal;
    private ProgressBar carbProgress;
    private CardView carbCard;
    private ImageView carbGridIcon;
    private TextView carbGridStatus;

    private TextView azucarTomado, azucarTotal;
    private ProgressBar proteinProgress;
    private CardView proteinCard;
    private ImageView proteinGridIcon;
    private TextView proteinGridStatus;

    private TextView fatTomado, fatTotal;
    private ProgressBar fatProgress;
    private CardView fatCard;
    private ImageView fatGridIcon;
    private TextView fatGridStatus;

    private TextView nuevaPrueba, vez;
    private ProgressBar newFoodProgress;
    private CardView newFoodCard;
    private ImageView newFoodGridIcon;
    private TextView newFoodGridStatus;

    private CaloriesViewModel caloriesViewModel;
    private int waterGoalTotal = 0;
    private int proteinGoalTotal = 0;
    private int carbGoalTotal = 0;
    private int fatGoalTotal = 0;
    private DailyGoalsFirestore dailyGoalsFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_goal, container, false);
        initializeViews(view);

        caloriesViewModel = new ViewModelProvider(getActivity()).get(CaloriesViewModel.class);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            dailyGoalsFirestore = new DailyGoalsFirestore(uid);
            loadAllGoalData();
        }

        return view;
    }

    private void initializeViews(View view) {
        aguaTomado = view.findViewById(R.id.agua_tomado);
        aguaTotal = view.findViewById(R.id.agua_total);
        waterProgress = view.findViewById(R.id.water_progress);
        waterCard = view.findViewById(R.id.goal1);
        waterGridIcon = view.findViewById(R.id.water_icon);
        waterGridStatus = view.findViewById(R.id.water_status);

        carbTomado = view.findViewById(R.id.carb_tomado);
        carbTotal = view.findViewById(R.id.carb_total);
        carbProgress = view.findViewById(R.id.carb_progress);
        carbCard = view.findViewById(R.id.goal2);
        carbGridIcon = view.findViewById(R.id.carb_icon);
        carbGridStatus = view.findViewById(R.id.carb_status);

        azucarTomado = view.findViewById(R.id.azugar_tomado);
        azucarTotal = view.findViewById(R.id.azucar_total);
        proteinProgress = view.findViewById(R.id.protein_progress);
        proteinCard = view.findViewById(R.id.goal3);
        proteinGridIcon = view.findViewById(R.id.protein_icon);
        proteinGridStatus = view.findViewById(R.id.protein_status);

        fatTomado = view.findViewById(R.id.fat_tomado);
        fatTotal = view.findViewById(R.id.fat_total);
        fatProgress = view.findViewById(R.id.fat_progress);
        fatCard = view.findViewById(R.id.goal4);
        fatGridIcon = view.findViewById(R.id.fat_icon);
        fatGridStatus = view.findViewById(R.id.fat_status);

        nuevaPrueba = view.findViewById(R.id.nueva_prueba);
        vez = view.findViewById(R.id.vez);
        newFoodProgress = view.findViewById(R.id.new_food_progress);
        newFoodCard = view.findViewById(R.id.goal5);
        newFoodGridIcon = view.findViewById(R.id.new_food_icon);
        newFoodGridStatus = view.findViewById(R.id.new_food_status);
    }

    private void loadAllGoalData() {
        dailyGoalsFirestore.getGoals(goal -> {
            waterGoalTotal = (goal.get("Water") != null) ? goal.get("Water") : 0;
            proteinGoalTotal = (goal.get("Protein") != null) ? goal.get("Protein") : 0;
            carbGoalTotal = (goal.get("Carbs") != null) ? goal.get("Carbs") : 0;
            fatGoalTotal = (goal.get("Fats") != null) ? goal.get("Fats") : 0;

            dailyGoalsFirestore.getWaterIntake(waterIntake -> {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    int waterCurrent = waterIntake;
                    loadMacroDataAndUpdateUI(waterCurrent);
                });
            });
        });
    }

    private void loadMacroDataAndUpdateUI(int waterCurrent) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        new DailyGoalsFirestore(uid).getHasCreatedFood(isCreated -> {
            int newFoodCurrent = isCreated ? 1 : 0;
            int newFoodTotal = 1;
            caloriesViewModel.getTotalMacros().observe(getViewLifecycleOwner(), macroInfo -> {
                if (macroInfo == null) return;

                int proteinCurrent = macroInfo.getProtein();
                int carbCurrent = macroInfo.getCarbs();
                int fatCurrent = macroInfo.getFats();

                updateAllGoals(waterCurrent, waterGoalTotal,
                        carbCurrent, carbGoalTotal,
                        proteinCurrent, proteinGoalTotal,
                        fatCurrent, fatGoalTotal,
                        newFoodCurrent, newFoodTotal);
            });
        });
    }

    private void updateAllGoals(int waterCurrent, int waterTotal,
                                int carbCurrent, int carbTotal,
                                int proteinCurrent, int proteinTotal,
                                int fatCurrent, int fatTotal,
                                int newFoodCurrent, int newFoodTotal) {
        updateWaterGoal(waterCurrent, waterTotal);
        updateCarbGoal(carbCurrent, carbTotal);
        updateProteinGoal(proteinCurrent, proteinTotal);
        updateFatGoal(fatCurrent, fatTotal);
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

    private void updateCarbGoal(int current, int total) {
        carbTomado.setText(String.valueOf(current));
        carbTotal.setText(String.valueOf(total));

        int progress = (total == 0) ? 0 : (current * 100) / total;
        carbProgress.setProgress(progress);

        int progressColor = (progress >= 100) ? R.color.greenApp : R.color.blueWaterDeshboard_ic;
        carbProgress.getProgressDrawable().setColorFilter(
                ContextCompat.getColor(getContext(), progressColor), PorterDuff.Mode.SRC_IN);

        updateCardStyle(carbCard, carbGridIcon, carbGridStatus, current >= total,
                R.drawable.goal_carb_foreground);
    }

    private void updateProteinGoal(int current, int total) {
        azucarTomado.setText(String.valueOf(current));
        azucarTotal.setText(String.valueOf(total));

        int progress = (total == 0) ? 0 : (current * 100) / total;
        proteinProgress.setProgress(progress);

        int progressColor = (progress >= 100) ? R.color.greenApp : R.color.blueWaterDeshboard_ic;
        proteinProgress.getProgressDrawable().setColorFilter(
                ContextCompat.getColor(getContext(), progressColor), PorterDuff.Mode.SRC_IN);

        updateCardStyle(proteinCard, proteinGridIcon, proteinGridStatus, current >= total,
                R.drawable.goal_muscle_foreground);
    }



    private void updateFatGoal(int current, int total) {
        if (fatTomado == null || fatTotal == null || fatProgress == null) return;

        fatTomado.setText(String.valueOf(current));
        fatTotal.setText(String.valueOf(total));

        int progress = (total == 0) ? 0 : (current * 100) / total;
        fatProgress.setProgress(progress);

        int color = progress >= 100 ? R.color.greenApp : R.color.blueWaterDeshboard_ic;
        fatProgress.getProgressDrawable().setColorFilter(
                ContextCompat.getColor(getContext(), color), PorterDuff.Mode.SRC_IN);

        updateCardStyle(fatCard, fatGridIcon, fatGridStatus, current >= total,
                R.drawable.goal_fat_foreground);
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
                } else if (icon.getId() == R.id.carb_icon) {
                    icon.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.goal_carb));
                    icon.setImageTintList(ContextCompat.getColorStateList(getContext(), R.color.goal_tint_carb));
                } else if (icon.getId() == R.id.protein_icon) {
                    icon.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.goal_protein));
                    icon.setImageTintList(ContextCompat.getColorStateList(getContext(), R.color.goal_tint_protein));
                } else if (icon.getId() == R.id.fat_icon) {
                    icon.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.goal_fat));
                    icon.setImageTintList(ContextCompat.getColorStateList(getContext(), R.color.goal_tint_fat));
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
            if (icon != null) {
                icon.setImageResource(iconResId);
                icon.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.grey));
                icon.setImageTintList(ContextCompat.getColorStateList(getContext(), R.color.light_grey));
            }

            if (statusText != null) {
                statusText.setText("Not completed");
                statusText.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            }
        }
    }
}