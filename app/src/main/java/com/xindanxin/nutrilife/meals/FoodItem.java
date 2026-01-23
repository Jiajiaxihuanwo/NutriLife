package com.xindanxin.nutrilife.meals;

public class FoodItem {
    private static Long ultimoId = 0L;
    private Long id;
    private String mealName;
    private String mealTime;
    private String calories;
    private String macros;

    public FoodItem(String mealName, String mealTime, String calories, String macros) {
        this.mealName = mealName;
        this.mealTime = mealTime;
        this.calories = calories;
        this.macros = macros;
        this.id = ++ultimoId;
    }

    public static Long getUltimoId() {
        return ultimoId;
    }

    public static void setUltimoId(Long ultimoId) {
        FoodItem.ultimoId = ultimoId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMealTime() {
        return mealTime;
    }

    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getMacros() {
        return macros;
    }

    public void setMacros(String macros) {
        this.macros = macros;
    }
}
