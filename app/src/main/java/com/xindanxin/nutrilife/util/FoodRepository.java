package com.xindanxin.nutrilife.util;

import android.content.Context;

import com.xindanxin.nutrilife.meals.FoodItem;

import java.util.List;

public class FoodRepository {
    public static List<FoodItem> getListaProvisional(Context context) {
        return List.of(
                new FoodItem("Oatmeal with Banana", 320, 10, 55, 6),
                new FoodItem("Scrambled Eggs", 280, 18, 2, 22),
                new FoodItem("Greek Yogurt", 150, 12, 8, 5),
                new FoodItem("Apple", 95, 0, 25, 0),
                new FoodItem("Protein Bar", 210, 20, 23, 7),
                new FoodItem("Grilled Chicken Salad", 420, 35, 25, 18),
                new FoodItem("Rice with Vegetables", 380, 8, 70, 6),
                new FoodItem("Salmon Fillet", 450, 40, 0, 30),
                new FoodItem("Pasta Bolognese", 560, 22, 75, 18),
                new FoodItem("Lentil Soup", 300, 18, 40, 6),
                new FoodItem("Mixed Nuts", 250, 8, 10, 22),
                new FoodItem("Smoothie", 280, 12, 45, 6),
                new FoodItem("Toast with Avocado", 320, 7, 30, 20),
                new FoodItem("Cottage Cheese", 180, 22, 6, 4),
                new FoodItem("Dark Chocolate", 170, 2, 18, 12),
                new FoodItem("Grilled Steak", 520, 45, 0, 38),
                new FoodItem("Mashed Potatoes", 260, 5, 45, 6),
                new FoodItem("Roasted Vegetables", 220, 6, 30, 10),
                new FoodItem("Chicken Wrap", 480, 32, 50, 16),
                new FoodItem("Yogurt with Honey", 200, 8, 30, 4),
                new FoodItem("Chen Salchichon", 999, 666, 67, 69)
        );
    }

}
