package com.xindanxin.nutrilife.dashboard;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.xindanxin.nutrilife.R;
import com.xindanxin.nutrilife.firestore.DailyGoalsFirestore;
import com.xindanxin.nutrilife.firestore.MealsStorageFirestore;
import com.xindanxin.nutrilife.firestore.UserProfileFirestore;
import com.xindanxin.nutrilife.firestore.WeightStorageFirestore;
import com.xindanxin.nutrilife.meals.FoodItem;
import com.xindanxin.nutrilife.util.CaloriesViewModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Dashboard extends Fragment {

    private LinearLayout barChartContainer;
    TextView textView;
    List<Integer> weights = new ArrayList<>();

    private WeightStorageFirestore weightStorageFirestore;
    private CaloriesViewModel caloriesViewModel;

    // objetivos que viene del profile
    TextView objetivoCaloria, totalProtein, totalCarbohidrato, totalGrasa;
    int vasosDani;

    // 喝水相关UI组件（提取为成员变量，方便刷新）
    private RecyclerView rvAgua;
    private ProgressBar waterProgress;
    private TextView cantidadAgua;
    private List<Boolean> aguasTomadas = new ArrayList<>();
    private WaterAdapter waterAdapter;

    public Dashboard() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // 初始化Firestore
        weightStorageFirestore = new WeightStorageFirestore(FirebaseAuth.getInstance().getCurrentUser().getUid());

        // 初始化基础UI组件
        initBasicViews(view);

        // 初始化动画
        initAnimations(view);

        // 初始化喝水UI（先初始化，等数据加载后刷新）
        initWaterViews(view);

        // 初始化卡路里/宏量营养素ViewModel
        initCaloriesViewModel(view);

        // 加载每日目标（包括水目标），加载完成后刷新喝水UI
        loadDailyGoalsFromFirestore();

        // 加载初始宏量营养素数据
        loadInitialMacros();

        // 加载体重数据
        loadWeightsData();
    }

    /**
     * 初始化基础UI组件
     */
    private void initBasicViews(View view) {
        objetivoCaloria = view.findViewById(R.id.restanteDiaria);
        totalProtein = view.findViewById(R.id.totalProteina);
        totalCarbohidrato = view.findViewById(R.id.totalCarbohidrato);
        totalGrasa = view.findViewById(R.id.totalGrasa);

        TextView fecha = view.findViewById(R.id.fecha);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            fecha.setText(today.format(formatter));
        }

        // 体重相关UI
        View fondo = view.findViewById(R.id.fondo);
        CardView progress = view.findViewById(R.id.progress);
        CardView peso = view.findViewById(R.id.peso);
        AppCompatButton add = view.findViewById(R.id.add);
        AppCompatButton cancel = view.findViewById(R.id.cancel);
        EditText valorPeso = view.findViewById(R.id.valorpeso);

        peso.setVisibility(View.GONE);
        fondo.setVisibility(View.GONE);

        progress.setOnClickListener(v -> {
            peso.setVisibility(View.VISIBLE);
            fondo.setVisibility(View.VISIBLE);
        });

        int todayIndex = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                LocalDate.now().getDayOfWeek().getValue() - 1 : 0;

        add.setOnClickListener(v -> {
            String text = valorPeso.getText().toString().trim();
            if (text.isEmpty()) return;
            int value = Integer.parseInt(text);
            weights.set(todayIndex, value);
            weightStorageFirestore.saveWeights(weights);
            peso.setVisibility(View.GONE);
            fondo.setVisibility(View.GONE);
            refreshChart();
        });

        cancel.setOnClickListener(v -> {
            peso.setVisibility(View.GONE);
            fondo.setVisibility(View.GONE);
        });
    }

    /**
     * 初始化动画
     */
    private void initAnimations(View view) {
        TextView totalCaloria = view.findViewById(R.id.totalCaloria);
        TextView consumed = view.findViewById(R.id.consumed_text);
        CardView card1 = view.findViewById(R.id.card1);
        CardView card2 = view.findViewById(R.id.card2);
        CardView card3 = view.findViewById(R.id.card3);
        CardView card4 = view.findViewById(R.id.card4);
        CardView cardAgua = view.findViewById(R.id.cardAgua);
        CardView cardWeigth = view.findViewById(R.id.progress);

        Animation cardaAnimacion = AnimationUtils.loadAnimation(view.getContext(), R.anim.dashboard_anim_card);
        Animation animacion = AnimationUtils.loadAnimation(view.getContext(), R.anim.dashboard_anim_letra);
        totalCaloria.startAnimation(animacion);
        consumed.startAnimation(animacion);
        card1.startAnimation(cardaAnimacion);
        card2.startAnimation(cardaAnimacion);
        card3.startAnimation(cardaAnimacion);
        card4.startAnimation(cardaAnimacion);
        cardAgua.startAnimation(cardaAnimacion);
        cardWeigth.startAnimation(cardaAnimacion);

        // 喝水卡片点击事件（提取到这里，避免重复代码）
        cardAgua.setOnClickListener(v -> onWaterCardClick());
    }

    /**
     * 初始化喝水相关UI组件
     */
    private void initWaterViews(View view) {
        textView = view.findViewById(R.id.aguaDiaria);
        waterProgress = view.findViewById(R.id.waterProgress);
        rvAgua = view.findViewById(R.id.rvAgua);
        cantidadAgua = view.findViewById(R.id.totalAgua);

        // 初始化RecyclerView
        rvAgua.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        waterAdapter = new WaterAdapter(aguasTomadas);
        rvAgua.setAdapter(waterAdapter);
    }

    /**
     * 初始化卡路里ViewModel
     */
    private void initCaloriesViewModel(View view) {
        TextView totalCaloria = view.findViewById(R.id.totalCaloria);
        TextView caloriaDiaria = view.findViewById(R.id.caloriaDiaria);
        TextView restanteDiaria = view.findViewById(R.id.restanteDiaria);
        ProgressBar circleProgress = view.findViewById(R.id.circleProgress);
        TextView protein = view.findViewById(R.id.proteina);
        TextView carbohidrato = view.findViewById(R.id.carbohidrato);
        TextView grasa = view.findViewById(R.id.grasa);

        caloriesViewModel = new ViewModelProvider(getActivity()).get(CaloriesViewModel.class);
        caloriesViewModel.getTotalMacros().observe(getViewLifecycleOwner(), macroInfo -> {
            if (macroInfo == null) return;

            totalCaloria.setText(String.valueOf(macroInfo.getCalories()));
            protein.setText(String.valueOf(macroInfo.getProtein()));
            carbohidrato.setText(String.valueOf(macroInfo.getCarbs()));
            grasa.setText(String.valueOf(macroInfo.getFats()));

            // 进度计算（增加非空判断，避免崩溃）
            try {
                int objetivo = Integer.parseInt(caloriaDiaria.getText().toString());
                int porcentaje = (int) ((macroInfo.getCalories() / (float) objetivo) * 100);
                animateProgress(porcentaje, circleProgress);
                int restante = objetivo - macroInfo.getCalories();
                restanteDiaria.setText(restante < 0 ? "0" : String.valueOf(restante));
            } catch (NumberFormatException e) {
                Log.e("Dashboard", "Error parsing calorie goal", e);
            }
        });
    }

    /**
     * 加载体重数据
     */
    private void loadWeightsData() {
        weightStorageFirestore.getWeights(weights -> {
            this.weights = weights;
            if (weights.isEmpty()) {
                for (int i = 0; i < 7; i++) {
                    weights.add(null);
                }
            }
            refreshChart();
        });
    }

    /**
     * 加载每日目标（关键修复：加载完成后刷新喝水UI）
     */
    private void loadDailyGoalsFromFirestore() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DailyGoalsFirestore firestore = new DailyGoalsFirestore(uid);

        firestore.getGoals(goal -> {
            // 更新目标值
            objetivoCaloria.setText(String.valueOf(goal.get("Cal")));
            totalCarbohidrato.setText(String.valueOf(goal.get("Carbs")));
            totalProtein.setText(String.valueOf(goal.get("Protein")));
            totalGrasa.setText(String.valueOf(goal.get("Fats")));
            vasosDani = (goal.get("Water") != null) ? goal.get("Water") : 0;

            // 关键：水目标加载完成后，刷新喝水UI
            refreshWaterUI();
        });
    }

    /**
     * 刷新喝水相关UI（核心修复方法）
     */
    private void refreshWaterUI() {
        if (getContext() == null) return;

        // 1. 更新水目标显示
        cantidadAgua.setText(String.valueOf(vasosDani));

        // 2. 初始化/重置喝水记录列表（避免空列表）
        aguasTomadas.clear();
        for (int i = 0; i < vasosDani; i++) {
            aguasTomadas.add(false);
        }

        // 3. 读取SharedPreferences中的保存数据
        SharedPreferences prefs = getContext().getSharedPreferences("AguaPrefs", Context.MODE_PRIVATE);
        int progresoGuardado = prefs.getInt("progreso", 0);

        // 4. 修复：如果保存的进度超过目标值，重置为目标值
        if (progresoGuardado > vasosDani) {
            progresoGuardado = vasosDani;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("progreso", progresoGuardado);
            editor.apply();
        }

        // 5. 更新当前饮水量显示
        textView.setText(String.valueOf(progresoGuardado));

        // 6. 更新进度条（增加除以0判断）
        int progress = vasosDani == 0 ? 0 : (int) ((progresoGuardado * 100f) / vasosDani);
        waterProgress.setProgress(progress);

        // 7. 加载保存的喝水记录
        for (int i = 0; i < Math.min(aguasTomadas.size(), vasosDani); i++) {
            boolean tomada = prefs.getBoolean("toma_" + i, false);
            aguasTomadas.set(i, tomada);
        }

        // 8. 刷新RecyclerView
        waterAdapter.notifyDataSetChanged();
    }

    /**
     * 喝水卡片点击事件（独立封装，逻辑优化）
     */
    private void onWaterCardClick() {
        if (vasosDani == 0) {
            Snackbar.make(getView(), "Please set water goal first!", Snackbar.LENGTH_SHORT).show();
            return;
        }

        String currentText = textView.getText().toString();
        int currentValue;
        try {
            currentValue = Integer.parseInt(currentText);
        } catch (NumberFormatException e) {
            currentValue = 0;
        }

        int newValue = currentValue + 1;
        SharedPreferences prefs = getContext().getSharedPreferences("AguaPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // 1. 未达到目标值
        if (newValue <= vasosDani) {
            textView.setText(String.valueOf(newValue));
            int progress = (int) ((newValue * 100f) / vasosDani);
            waterProgress.setProgress(progress);
            Snackbar.make(getView(), "+1\uD83D\uDCA7", Snackbar.LENGTH_SHORT).show();

            // 标记对应的喝水记录为已喝
            for (int i = 0; i < aguasTomadas.size(); i++) {
                if (!aguasTomadas.get(i)) {
                    aguasTomadas.set(i, true);
                    break;
                }
            }
        } else {
            // 2. 超过目标值（重置为目标值，避免数值异常）
            newValue = vasosDani;
            textView.setText(String.valueOf(newValue));
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.accent));
            waterProgress.setProgress(100);
            Snackbar.make(getView(), "Goal completed! \uD83D\uDCA6", Snackbar.LENGTH_SHORT).show();
        }

        // 3. 刷新列表
        waterAdapter.notifyDataSetChanged();

        // 4. 保存数据到SharedPreferences
        editor.putInt("progreso", newValue);
        for (int i = 0; i < aguasTomadas.size(); i++) {
            editor.putBoolean("toma_" + i, aguasTomadas.get(i));
        }
        editor.apply();
    }

    private void animateProgress(int targetProgress, ProgressBar progressAnimacion) {
        ObjectAnimator animator = ObjectAnimator.ofInt(
                progressAnimacion,
                "progress",
                0,
                targetProgress
        );
        animator.setDuration(1500);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    private void refreshChart() {
        barChartContainer = getView().findViewById(R.id.barChartContainer);
        barChartContainer.removeAllViews();
        barChartContainer.post(() -> {
            int containerHeight = barChartContainer.getHeight();

            int max = weights.stream()
                    .filter(Objects::nonNull)
                    .max(Integer::compareTo)
                    .orElse(0);

            if (max <= 0) {
                max = 1; // 避免除以0
            }

            for (Integer v : weights) {
                View bar = new View(getContext());
                int barHeight = 0;
                if (v != null) {
                    barHeight = containerHeight * v / max;
                }

                LinearLayout.LayoutParams lp =
                        new LinearLayout.LayoutParams(0, barHeight, 1f);
                lp.setMargins(4, 0, 4, 0);

                bar.setLayoutParams(lp);
                bar.setBackgroundColor(
                        ContextCompat.getColor(getContext(), R.color.greenApp)
                );
                barChartContainer.addView(bar);
            }
        });
    }

    private void loadInitialMacros() {
        MealsStorageFirestore firestore = new MealsStorageFirestore();
        String[] mealTypes = {"Breakfast", "Lunch", "Dinner", "Snacks", "Default"};

        for (String mealType : mealTypes) {
            firestore.getFoodItems(mealType, foodItems -> {
                int calories = 0, protein = 0, carbs = 0, fats = 0;

                for (FoodItem f : foodItems) {
                    calories += f.getCalories();
                    protein += f.getProtein();
                    carbs += f.getCarbs();
                    fats += f.getFats();
                    if (caloriesViewModel.getTotalMacros().getValue() != null) {
                        Log.e("Dashboard", String.valueOf(caloriesViewModel.getTotalMacros().getValue().getCalories()));
                    }
                }

                caloriesViewModel.setMacros(mealType, calories, protein, carbs, fats);
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // 重新加载目标并刷新喝水UI
        loadDailyGoalsFromFirestore();
    }

    // 确保WaterAdapter是内部类或已正确导入（如果是外部类，需确认存在）
    private class WaterAdapter extends RecyclerView.Adapter<WaterAdapter.WaterViewHolder> {
        private List<Boolean> waterIntakeList;

        public WaterAdapter(List<Boolean> waterIntakeList) {
            this.waterIntakeList = waterIntakeList;
        }

        @NonNull
        @Override
        public WaterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.water_item_layout, parent, false);
            return new WaterViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull WaterViewHolder holder, int position) {
            boolean isDrank = waterIntakeList.get(position);
            // 根据是否已喝设置样式（示例，需匹配你的item_water.xml）
            holder.waterItemView.setBackgroundColor(isDrank ?
                    ContextCompat.getColor(getContext(), R.color.blueWaterDeshboard) :
                    ContextCompat.getColor(getContext(), R.color.light_grey));
        }

        @Override
        public int getItemCount() {
            return waterIntakeList.size();
        }

        public class WaterViewHolder extends RecyclerView.ViewHolder {
            View waterItemView;

            public WaterViewHolder(@NonNull View itemView) {
                super(itemView);
                waterItemView = itemView;
            }
        }
    }
}