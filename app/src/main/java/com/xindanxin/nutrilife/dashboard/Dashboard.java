package com.xindanxin.nutrilife.dashboard;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import com.xindanxin.nutrilife.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Dashboard extends Fragment {

    private LinearLayout barChartContainer;

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

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentText = textView.getText().toString();
                int currentValue = Integer.parseInt(currentText);
                int newValue = currentValue + 1;
                if (newValue <= 8) {
                    textView.setText(String.valueOf(newValue));
                    int progress = (int) ((newValue / 8.0) * 100);
                    progressBar.setProgress(progress);
                    Toast.makeText(getContext(), "+1\uD83D\uDCA7", Toast.LENGTH_SHORT).show();
                } else {
                    textView.setText(String.valueOf(newValue));
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.accent));
                    int progress = (int) ((newValue / 8.0) * 100);
                    progressBar.setProgress(progress);
                    Toast.makeText(getContext(), "+1\uD83D\uDCA6", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                peso.setVisibility(View.GONE);
                fondo.setVisibility(View.GONE);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                peso.setVisibility(View.GONE);
                fondo.setVisibility(View.GONE);
            }
        });

        barChartContainer = view.findViewById(R.id.barChartContainer);

        // 柱子数据，百分比
        int[] heights = {65, 80, 70, 90, 75, 85, 95};

        // 先清空容器
        barChartContainer.removeAllViews();

        // 动态生成柱子
        for (int h : heights) {
            View bar = new View(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    0, 0, 1f // weight=1, 宽度等分
            );
            lp.height = (int) (barChartContainer.getHeight() * h / 100f);
            lp.setMargins(4, 0, 4, 0); // 柱子间距
            bar.setLayoutParams(lp);

            // 设置颜色
            bar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.greenApp));

            barChartContainer.addView(bar);
        }

        // 延迟布局完成后设置高度（保证 container.getHeight() 有值）
        barChartContainer.post(() -> {
            barChartContainer.removeAllViews();
            for (int h : heights) {
                View bar = new View(getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        0, (int) (barChartContainer.getHeight() * h / 100f),
                        1f
                );
                lp.setMargins(4, 0, 4, 0);
                bar.setLayoutParams(lp);
                bar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.greenApp));
                barChartContainer.addView(bar);
            }
        });
    }
}
