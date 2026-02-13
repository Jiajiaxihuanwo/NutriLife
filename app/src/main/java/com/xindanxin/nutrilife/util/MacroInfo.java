package com.xindanxin.nutrilife.util;

public class MacroInfo {
    public int calories;
    public int protein;
    public int carbs;
    public int fats;

    public MacroInfo(int calories, int protein, int carbs, int fats) {
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fats = fats;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public int getFats() {
        return fats;
    }

    public void setFats(int fat) {
        this.fats = fat;
    }
}
