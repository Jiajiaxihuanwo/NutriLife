package com.xindanxin.nutrilife.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeightStorage {

    private static final String PREFS_WEIGHTS = "WeightsPrefs";
    private static final String KEY_PREFIX = "weight_v2_";

    private static final List<String> DAY_OF_WEEK =
            Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");

    public static List<Integer> getWeights(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFS_WEIGHTS, Context.MODE_PRIVATE);
        List<Integer> weights = new ArrayList<>();

        for (String day : DAY_OF_WEEK) {
            weights.add(sp.getInt(KEY_PREFIX + day, 0));
        }
        return weights;
    }

    public static void save(Context context, List<Integer> list) {
        if (list.size() < 7) return;

        SharedPreferences sp = context.getSharedPreferences(PREFS_WEIGHTS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        for (int i = 0; i < 7; i++) {
            editor.putInt(KEY_PREFIX + DAY_OF_WEEK.get(i), list.get(i));
        }
        editor.apply();
    }
}