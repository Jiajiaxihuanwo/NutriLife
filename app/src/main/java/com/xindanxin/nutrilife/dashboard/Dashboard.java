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
    List<Integer> weights = new ArrayList<>();

    private WeightStorageFirestore weightStorageFirestore;

    private CaloriesViewModel caloriesViewModel;

    //objetivos que viene dle profile
    TextView objetivoCaloria,totalProtein,totalCarbohidrato,totalGrasa;
    int vasosDani;

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

        weightStorageFirestore = new WeightStorageFirestore(FirebaseAuth.getInstance().getCurrentUser().getUid());

        objetivoCaloria = view.findViewById(R.id.restanteDiaria);
        totalProtein = view.findViewById(R.id.totalProteina);
        totalCarbohidrato = view.findViewById(R.id.totalCarbohidrato);
        totalGrasa = view.findViewById(R.id.totalGrasa);

        //animacion
        TextView totalCaloria = view.findViewById(R.id.totalCaloria);
        TextView consumed = view.findViewById(R.id.consumed_text);
        CardView card1 = view.findViewById(R.id.card1);
        CardView card2 = view.findViewById(R.id.card2);
        CardView card3 = view.findViewById(R.id.card3);
        CardView card4 = view.findViewById(R.id.card4);
        CardView cardAgua = view.findViewById(R.id.cardAgua);
        CardView cardWeigth = view.findViewById(R.id.progress);

        TextView caloriaDiaria = view.findViewById(R.id.caloriaDiaria);
        TextView restanteDiaria = view.findViewById(R.id.restanteDiaria);
        ProgressBar circleProgress = view.findViewById(R.id.circleProgress);
        TextView protein = view.findViewById(R.id.proteina);
        TextView carbohidrato = view.findViewById(R.id.carbohidrato);
        TextView grasa = view.findViewById(R.id.grasa);

        caloriesViewModel = new ViewModelProvider(getActivity()).get(CaloriesViewModel.class);

        // Cargar los macros iniciales desde Firestore
        loadInitialMacros();

        caloriesViewModel.getTotalMacros().observe(getViewLifecycleOwner(), macroInfo -> {
            if (macroInfo == null) return;

            totalCaloria.setText(String.valueOf(macroInfo.getCalories()));
            protein.setText(String.valueOf(macroInfo.getProtein()));
            carbohidrato.setText(String.valueOf(macroInfo.getCarbs()));
            grasa.setText(String.valueOf(macroInfo.getFats()));

            // progresos
            int objetivo = Integer.parseInt(caloriaDiaria.getText().toString());
            int porcentaje = (int)((macroInfo.getCalories() / (float) objetivo) * 100);
            animateProgress(porcentaje, circleProgress);
            int restante = objetivo - macroInfo.getCalories();
            restanteDiaria.setText(restante < 0 ? "0" : String.valueOf(restante));
        });

        Animation cardaAnimacion = AnimationUtils.loadAnimation(view.getContext(),R.anim.dashboard_anim_card);
        Animation animacion = AnimationUtils.loadAnimation(view.getContext(), R.anim.dashboard_anim_letra);
        totalCaloria.startAnimation(animacion);
        consumed.startAnimation(animacion);
        card1.startAnimation(cardaAnimacion);
        card2.startAnimation(cardaAnimacion);
        card3.startAnimation(cardaAnimacion);
        card4.startAnimation(cardaAnimacion);
        cardAgua.startAnimation(cardaAnimacion);
        cardWeigth.startAnimation(cardaAnimacion);






        weightStorageFirestore.getWeights(weights -> {
            this.weights = weights;
            refreshChart();
        });
        super.onViewCreated(view, savedInstanceState);
        //rv del agua
        TextView textView = view.findViewById(R.id.aguaDiaria);
        ProgressBar progressBar = view.findViewById(R.id.waterProgress);
        RecyclerView rvAgua = view.findViewById(R.id.rvAgua);
        rvAgua.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        TextView cantidadAgua = view.findViewById(R.id.totalAgua);
        cantidadAgua.setText(String.valueOf(vasosDani));
        List<Boolean> aguasTomadas = new ArrayList<>();
        for (int i = 0; i < vasosDani; i++) {
            aguasTomadas.add(false);
        }

        WaterAdapter adapter = new WaterAdapter(aguasTomadas);
        rvAgua.setAdapter(adapter);
        SharedPreferences prefs = getContext().getSharedPreferences("AguaPrefs", Context.MODE_PRIVATE);
        int progresoGuardado = prefs.getInt("progreso", 0);
        textView.setText(String.valueOf(progresoGuardado));
        progressBar.setProgress((int) ((progresoGuardado * 100f) / vasosDani));

        for (int i = 0; i < vasosDani; i++) {
            boolean tomada = prefs.getBoolean("toma_" + i, false);
            aguasTomadas.set(i, tomada);
        }
        adapter.notifyDataSetChanged();

        cardAgua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentText = textView.getText().toString();
                int currentValue = Integer.parseInt(currentText);
                int newValue = currentValue + 1;

                if (newValue <= vasosDani) {
                    textView.setText(String.valueOf(newValue));
                    int progress = (int) ((newValue * 100f) / vasosDani);
                    progressBar.setProgress(progress);

                    Snackbar.make(view, "+1\uD83D\uDCA7", Snackbar.LENGTH_SHORT).show();

                    //logica para que vaya aumentando las tomas de agua
                    for (int i = 0; i < vasosDani; i++) {
                        if (!aguasTomadas.get(i)) {
                            aguasTomadas.set(i, true);
                            break;
                        }
                    }
                } else {
                    textView.setText(String.valueOf(newValue));
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.accent));
                    int progress = (int) ((newValue / vasosDani) * 100);
                    progressBar.setProgress(progress);

                    Snackbar.make(view, "+1\uD83D\uDCA6", Snackbar.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();

                // ===== GUARDAR DATOS ===== <<<=== AGREGADO
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("progreso", newValue);
                for (int i = 0; i < aguasTomadas.size(); i++) {
                    editor.putBoolean("toma_" + i, aguasTomadas.get(i));
                }
                editor.apply();
                // ===== FIN GUARDAR DATOS ===== <<<=== AGREGADO
            }
        });

        TextView fecha = view.findViewById(R.id.fecha);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd/MM/yyyy");

            fecha.setText(today.format(formatter));
        }

//        View fondo = view.findViewById(R.id.fondo);
        CardView progress = view.findViewById(R.id.progress);
        CardView peso = view.findViewById(R.id.peso);

        peso.setVisibility(View.GONE);
//        fondo.setVisibility(View.GONE);

        progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                peso.setVisibility(View.VISIBLE);
//                fondo.setVisibility(View.VISIBLE);
            }
        });
        AppCompatButton add = view.findViewById(R.id.add);
        AppCompatButton cancel = view.findViewById(R.id.cancel);

        barChartContainer = view.findViewById(R.id.barChartContainer);
        EditText valorPeso = view.findViewById(R.id.valorpeso);

        int todayIndex;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            todayIndex = LocalDate.now().getDayOfWeek().getValue() - 1;
        } else {
            todayIndex = 0;
        }
        if (weights.isEmpty()) {
            for (int i = 0; i < 7; i++) {
                weights.add(null);
            }
        }

        //button de opcion
        add.setOnClickListener(v -> {
            String text = valorPeso.getText().toString().trim();
            if (text.isEmpty()) return;
            int value = Integer.parseInt(text);
            weights.set(todayIndex, value);
            weightStorageFirestore.saveWeights(weights);
            peso.setVisibility(View.GONE);
//            fondo.setVisibility(View.GONE);
            refreshChart();
        });

        cancel.setOnClickListener(v -> {
            peso.setVisibility(View.GONE);
//            fondo.setVisibility(View.GONE);
        });
        refreshChart();

    }
    private void animateProgress(int targetProgress,ProgressBar progressAnimacion) {
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
        barChartContainer.removeAllViews();
        barChartContainer.post(() -> {
            int containerHeight = barChartContainer.getHeight();

            int max = weights.stream()
                    .filter(Objects::nonNull)
                    .max(Integer::compareTo)
                    .orElse(0);

            // Si todos los valores son 0, no dibujamos nada
            if (max <= 0) {
                max = 1; // Evita división por cero
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
        String[] mealTypes = {"Breakfast", "Lunch", "Dinner","Snacks","Default"};

        for (String mealType : mealTypes) {
            firestore.getFoodItems(mealType, foodItems -> {
                int calories = 0, protein = 0, carbs = 0, fats = 0;

                for (FoodItem f : foodItems) {
                    calories += f.getCalories();
                    protein += f.getProtein();
                    carbs += f.getCarbs();
                    fats += f.getFats();
                    Log.e("Dashboard",Integer.toString(caloriesViewModel.getTotalMacros().getValue().calories));
                }

                caloriesViewModel.setMacros(mealType, calories, protein, carbs, fats);
            });
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        loadDailyGoalsFromFirestore();
    }

    private void loadDailyGoalsFromFirestore() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DailyGoalsFirestore firestore = new DailyGoalsFirestore(uid);

        firestore.getGoals(goal ->{
            objetivoCaloria.setText(String.valueOf(goal.get("Cal")));
            totalCarbohidrato.setText(String.valueOf(goal.get("Carbs")));
            totalProtein.setText(String.valueOf(goal.get("Protein")));
            totalGrasa.setText(String.valueOf(goal.get("Fats")));
            vasosDani = (goal.get("Water")!=null)? goal.get("Water") :0;

        });
    }

}