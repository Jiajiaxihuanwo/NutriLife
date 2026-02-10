package com.xindanxin.nutrilife.util;

import android.content.Context;

import com.xindanxin.nutrilife.meals.FoodItem;

import java.util.List;

public class FoodRepository {
    public static List<FoodItem> getListaProvisional(Context context) {
        return List.of(
                new FoodItem(MealsStorage.getNextFoodId(context), "Oatmeal with Banana", "08:00", "320 kcal", "P:10g C:55g F:6g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Scrambled Eggs", "08:30", "280 kcal", "P:18g C:2g F:22g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Greek Yogurt", "09:00", "150 kcal", "P:12g C:8g F:5g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Apple", "10:30", "95 kcal", "P:0g C:25g F:0g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Protein Bar", "11:00", "210 kcal", "P:20g C:23g F:7g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Grilled Chicken Salad", "13:00", "420 kcal", "P:35g C:25g F:18g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Rice with Vegetables", "13:30", "380 kcal", "P:8g C:70g F:6g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Salmon Fillet", "14:00", "450 kcal", "P:40g C:0g F:30g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Pasta Bolognese", "14:30", "560 kcal", "P:22g C:75g F:18g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Lentil Soup", "15:00", "300 kcal", "P:18g C:40g F:6g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Mixed Nuts", "17:00", "250 kcal", "P:8g C:10g F:22g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Smoothie", "17:30", "280 kcal", "P:12g C:45g F:6g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Toast with Avocado", "18:00", "320 kcal", "P:7g C:30g F:20g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Cottage Cheese", "18:30", "180 kcal", "P:22g C:6g F:4g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Dark Chocolate", "19:00", "170 kcal", "P:2g C:18g F:12g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Grilled Steak", "21:00", "520 kcal", "P:45g C:0g F:38g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Mashed Potatoes", "21:15", "260 kcal", "P:5g C:45g F:6g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Roasted Vegetables", "21:30", "220 kcal", "P:6g C:30g F:10g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Chicken Wrap", "22:00", "480 kcal", "P:32g C:50g F:16g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Yogurt with Honey", "22:30", "200 kcal", "P:8g C:30g F:4g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Whole Wheat Pancakes", "07:30", "360 kcal", "P:12g C:60g F:8g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Orange Juice", "08:15", "110 kcal", "P:2g C:26g F:0g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Cheese Sandwich", "09:30", "290 kcal", "P:12g C:32g F:14g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Boiled Eggs", "10:00", "155 kcal", "P:13g C:1g F:11g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Fruit Salad", "11:30", "180 kcal", "P:3g C:42g F:1g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Turkey Breast", "12:30", "210 kcal", "P:38g C:0g F:4g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Quinoa Bowl", "13:45", "410 kcal", "P:14g C:65g F:12g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Vegetable Omelette", "14:45", "330 kcal", "P:20g C:8g F:24g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Peanut Butter Toast", "16:30", "340 kcal", "P:10g C:28g F:22g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Banana", "17:15", "105 kcal", "P:1g C:27g F:0g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Tuna Sandwich", "18:45", "390 kcal", "P:28g C:35g F:12g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Vegetable Soup", "19:30", "190 kcal", "P:6g C:30g F:4g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Chicken Rice Bowl", "20:00", "520 kcal", "P:34g C:60g F:14g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Protein Shake", "21:45", "240 kcal", "P:30g C:12g F:4g"),
                new FoodItem(MealsStorage.getNextFoodId(context), "Chen salchichon", "23:00", "160 kcal", "P:20g C:8g F:3g"));
    }
}
