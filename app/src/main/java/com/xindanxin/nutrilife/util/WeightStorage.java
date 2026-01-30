package com.xindanxin.nutrilife.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class WeightStorage {
    private static final String PREFS_WEIGHTS = "WeightsPrefs";
    private static final List<String> DAY_OF_WEEK = List.of("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");

    public static List<Integer> getHeights(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_WEIGHTS, Context.MODE_PRIVATE);
        List<Integer> heights= new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            heights.add(sharedPreferences.getInt(DAY_OF_WEEK.get(i),0));
        }
        return heights;
    }

    public static void save(Context context, List<Integer> lista) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_WEIGHTS, Context.MODE_PRIVATE);
        for (int i = 0; i < 7; i++) {
            sharedPreferences.edit()
                    .putLong("weight_"+DAY_OF_WEEK.get(i),lista.get(i))
                    .apply();
        }
    }
}
