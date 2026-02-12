package com.xindanxin.nutrilife.meals;

public class FoodItem {

    private String docId;      // ID del documento en Firestore
    private String mealName;
    private int calories;
    private int protein;
    private int carbs;
    private int fats;

    // Constructor vacío necesario para Firestore
    public FoodItem() {}

    public FoodItem(String mealName, int calories, int protein, int carbs, int fats) {
        this.mealName = mealName;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fats = fats;
    }


    // ------------------ Getters y Setters ------------------

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
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

    public void setFats(int fats) {
        this.fats = fats;
    }

    // ------------------ Métodos Formateados ------------------

    public String getMacros() {
        return String.format("P:%dg C:%dg F:%dg", protein, carbs, fats);
    }

    public String valueOfCalories() {
        return calories + " kcal";
    }

    public String valueOfProtein() {
        return "P: " + protein + "g";
    }

    public String valueOfCarbs() {
        return "C: " + carbs + "g";
    }

    public String valueOfFats() {
        return "F: " + fats + "g";
    }
}
