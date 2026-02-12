package com.xindanxin.nutrilife.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

public class DailyGoalsStorage {
    private static final String PREFS_GOALS="GoalsPrefs";
    private static final String KEY_GOALS="goals_";

    public static int getCal(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFS_GOALS, Context.MODE_PRIVATE);
        return sp.getInt(KEY_GOALS + "_Cal", 0);
    }

    public static void saveCal(Context context, int cal) {
        SharedPreferences sp = context.getSharedPreferences(PREFS_GOALS, Context.MODE_PRIVATE);
        sp.edit().putInt(KEY_GOALS + "_Cal", cal).apply();
    }

    // Carbs
    public static int getCarbs(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFS_GOALS, Context.MODE_PRIVATE);
        return sp.getInt(KEY_GOALS + "_Carbs", 0);
    }

    public static void saveCarbs(Context context, int carbs) {
        SharedPreferences sp = context.getSharedPreferences(PREFS_GOALS, Context.MODE_PRIVATE);
        sp.edit().putInt(KEY_GOALS + "_Carbs", carbs).apply();
    }

    // Protein
    public static int getProtein(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFS_GOALS, Context.MODE_PRIVATE);
        return sp.getInt(KEY_GOALS + "_Protein", 0);
    }

    public static void saveProtein(Context context, int protein) {
        SharedPreferences sp = context.getSharedPreferences(PREFS_GOALS, Context.MODE_PRIVATE);
        sp.edit().putInt(KEY_GOALS + "_Protein", protein).apply();
    }

    // Fats
    public static int getFats(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFS_GOALS, Context.MODE_PRIVATE);
        return sp.getInt(KEY_GOALS + "_Fats", 0);
    }

    public static void saveFats(Context context, int fats) {
        SharedPreferences sp = context.getSharedPreferences(PREFS_GOALS, Context.MODE_PRIVATE);
        sp.edit().putInt(KEY_GOALS + "_Fats", fats).apply();
    }

    // Water
    public static int getWater(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFS_GOALS, Context.MODE_PRIVATE);
        return sp.getInt(KEY_GOALS + "_Water", 0);
    }

    public static void saveWater(Context context, int water) {
        SharedPreferences sp = context.getSharedPreferences(PREFS_GOALS, Context.MODE_PRIVATE);
        sp.edit().putInt(KEY_GOALS + "_Water", water).apply();
    }
}
