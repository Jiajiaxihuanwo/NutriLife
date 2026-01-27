package com.xindanxin.nutrilife.dashboard;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.xindanxin.nutrilife.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Dashboard extends Fragment {

    private LinearLayout barChartContainer;
    List<Integer> heights = new ArrayList<>();

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
        super.onViewCreated(view, savedInstanceState);
        CardView cardView = view.findViewById(R.id.cardAgua);
        TextView textView = view.findViewById(R.id.aguaDiaria);
        ProgressBar progressBar = view.findViewById(R.id.waterProgress);


        //rv del agua
        RecyclerView rvAgua = view.findViewById(R.id.rvAgua);
        rvAgua.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        int vasosDani =20;
        TextView cantidadAgua = view.findViewById(R.id.totalAgua);
        cantidadAgua.setText(String.valueOf(vasosDani));
        List<Boolean> aguasTomadas = new ArrayList<>(); //aqui traemos la capacidad del agua
        for (int i = 0; i < vasosDani; i++) {
            aguasTomadas.add(false);
        }

        WaterAdapter adapter = new WaterAdapter(aguasTomadas);

        cardView.setOnClickListener(new View.OnClickListener() {
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
                        if(!aguasTomadas.get(i)){
                            aguasTomadas.set(i,true);
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


        View fondo = view.findViewById(R.id.fondo);
        CardView progress = view.findViewById(R.id.progress);
        CardView peso = view.findViewById(R.id.peso);

        peso.setVisibility(View.GONE);
        fondo.setVisibility(View.GONE);

        progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                peso.setVisibility(View.VISIBLE);
                fondo.setVisibility(View.VISIBLE);
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
        heights.clear();
        for (int i = 0; i < 7; i++) {
            heights.add(null);
        }

        add.setOnClickListener(v -> {
            String text = valorPeso.getText().toString().trim();
            if (text.isEmpty()) return;
            int value = Integer.parseInt(text);
            heights.set(todayIndex, value);
            peso.setVisibility(View.GONE);
            fondo.setVisibility(View.GONE);
            refreshChart();
        });

        cancel.setOnClickListener(v -> {
            peso.setVisibility(View.GONE);
            fondo.setVisibility(View.GONE);
        });


    }

    private void refreshChart() {
        barChartContainer.removeAllViews();
        barChartContainer.post(() -> {
            int containerHeight = barChartContainer.getHeight();
            for (Integer v : heights) {
                View bar = new View(getContext());
                int barHeight = 0;
                if (v != null) {
                    barHeight = (int) (containerHeight * v / 100f);
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
