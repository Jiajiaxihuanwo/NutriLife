package com.xindanxin.nutrilife.dashboard;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
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
import com.xindanxin.nutrilife.R;
import com.xindanxin.nutrilife.util.CaloriesViewModel;
import com.xindanxin.nutrilife.util.WeightStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Dashboard extends Fragment {

    private LinearLayout barChartContainer;
    List<Integer> heights = new ArrayList<>();

    CaloriesViewModel caloriesViewModel;

    public Dashboard() {
        // Required empty public constructor
    }

    public static Dashboard newInstance(String param1, String param2) {
        Dashboard fragment = new Dashboard();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
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
        //animacion
        TextView totalCaloria = view.findViewById(R.id.totalCaloria);
        TextView consumed = view.findViewById(R.id.consumed_text);
        CardView card1 = view.findViewById(R.id.card1);
        CardView card2 = view.findViewById(R.id.card2);
        CardView card3 = view.findViewById(R.id.card3);
        CardView card4 = view.findViewById(R.id.card4);
        CardView cardAgua = view.findViewById(R.id.cardAgua);
        CardView cardWeigth = view.findViewById(R.id.progress);
        caloriesViewModel = new ViewModelProvider(getActivity()).get(CaloriesViewModel.class);

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

        totalCaloria.setText(String.valueOf(caloriesViewModel.getTotalMacros().calories));

        //progressBar de caloria diaria
        TextView caloriaDiaria = view.findViewById(R.id.caloriaDiaria);
        TextView restanteDiaria = view.findViewById(R.id.restanteDiaria);
        ProgressBar circleProgress = view.findViewById(R.id.circleProgress);
        String objetivoCaloria = caloriaDiaria.getText().toString();
        int caloriaConsumida = (int)((caloriesViewModel.getTotalMacros().calories/Double.parseDouble(objetivoCaloria))*100);
        restanteDiaria.setText(Integer.parseInt(objetivoCaloria)-caloriesViewModel.getTotalMacros().calories < 0? "0" : String.valueOf(Integer.parseInt(objetivoCaloria)-caloriesViewModel.getTotalMacros().calories));
        animateProgress(caloriaConsumida,circleProgress);

        //protein,carb y fat
        TextView protein = view.findViewById(R.id.proteina);
        TextView carbohidrato = view.findViewById(R.id.carbohidrato);
        TextView grasa = view.findViewById(R.id.grasa);
        TextView totalProtein = view.findViewById(R.id.totalProteina);
        TextView totalCarbohidrato = view.findViewById(R.id.totalCarbohidrato);
        TextView totalGrasa = view.findViewById(R.id.totalGrasa);
        protein.setText(String.valueOf(caloriesViewModel.getTotalMacros().protein));
        carbohidrato.setText(String.valueOf(caloriesViewModel.getTotalMacros().carbs));
        grasa.setText(String.valueOf(caloriesViewModel.getTotalMacros().fat));


        heights = WeightStorage.getWeights(requireContext());
        super.onViewCreated(view, savedInstanceState);
        //rv del agua
        TextView textView = view.findViewById(R.id.aguaDiaria);
        ProgressBar progressBar = view.findViewById(R.id.waterProgress);
        RecyclerView rvAgua = view.findViewById(R.id.rvAgua);
        rvAgua.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        int vasosDani = 20;
        TextView cantidadAgua = view.findViewById(R.id.totalAgua);
        cantidadAgua.setText(String.valueOf(vasosDani));
        List<Boolean> aguasTomadas = new ArrayList<>(); //aqui traemos la capacidad del agua
        for (int i = 0; i < vasosDani; i++) {
            aguasTomadas.add(false);
        }

        WaterAdapter adapter = new WaterAdapter(aguasTomadas);

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
            }

        });
        //cargamos el recyclerview
        rvAgua.setAdapter(adapter);


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
        if (heights.isEmpty()) {
            for (int i = 0; i < 7; i++) {
                heights.add(null);
            }
        }


        //button de opcion
        add.setOnClickListener(v -> {
            String text = valorPeso.getText().toString().trim();
            if (text.isEmpty()) return;
            int value = Integer.parseInt(text);
            heights.set(todayIndex, value);
            WeightStorage.save(requireContext(),heights);
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
                0, // 从0开始
                targetProgress // 到目标值
        );
        animator.setDuration(1500); // 1.5秒
        animator.setInterpolator(new DecelerateInterpolator()); // 减速效果
        animator.start();
    }

    // 使用



    //cargar la tabla
    private void refreshChart() {
        barChartContainer.removeAllViews();
        barChartContainer.post(() -> {
            int containerHeight = barChartContainer.getHeight();

            int max = heights.stream().max((a, b) -> a - b).orElseThrow();

            for (Integer v : heights) {
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
}
