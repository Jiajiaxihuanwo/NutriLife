package com.xindanxin.nutrilife.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xindanxin.nutrilife.meals.FoodItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MealsStorage {

    private static final String PREFS_MEALS = "MealsPrefs";
    private static final String KEY_FOOD_LIST_PREFIX = "foodList_";
    private static final String KEY_ULTIMO_ID_FOOD = "ultimo_food";

    //Metodo de manejo de id para los meals
    public static long getNextFoodId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_MEALS, Context.MODE_PRIVATE);

        long nuevoId = prefs.getLong(KEY_ULTIMO_ID_FOOD, 0) + 1;

        prefs.edit()
                .putLong(KEY_ULTIMO_ID_FOOD, nuevoId)
                .apply();

        return nuevoId;
    }


    //metodos de persistencia con ShharedPreferences

    //metodo para sacar la key correcta para acceder a los datos guardados
    private static String getFoodListKey(String mealType) {
        return KEY_FOOD_LIST_PREFIX + mealType;
    }

    /**
     * Guarda la lista de alimentos en SharedPreferences como JSON.
     * Esto permite que los datos persistan aunque cierres la app.
     */
    public static void saveFoodList(Context context,String mealType, List<FoodItem> foodList) {
        // Obtenemos el SharedPreferences con nombre "MealsPrefs" en modo privado
        SharedPreferences prefs = context.getSharedPreferences(PREFS_MEALS, Context.MODE_PRIVATE);

        //obtenemos la key real
        String key = getFoodListKey(mealType);

        // Creamos un editor para modificar el SharedPreferences
        // Gson es una librería que convierte objetos Java en JSON (texto) y viceversa
        prefs.edit().
                putString(key,new Gson().toJson(foodList))
                .apply();
    }

    public static List<FoodItem> loadFoodList(Context context,String mealType) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_MEALS, Context.MODE_PRIVATE);

        String key = getFoodListKey(mealType);

        //    Si no existe, devolvemos un String vacío ""
        String json = prefs.getString(key, "");
        if (json.isEmpty()) return new ArrayList<>();

        //    Aquí usamos TypeToken para indicarle que es una lista de FoodItem
        //    Esto es necesario porque en Java, la información de "List<FoodItem>" se pierde por el tipo genérico
        Type type = new TypeToken<List<FoodItem>>() {}.getType();
        return new Gson().fromJson(json, type);
    }

    public static void deleteFoodItem(Context context, String mealType, FoodItem itemToDelete) {

        List<FoodItem> list = loadFoodList(context,mealType);

        list.removeIf(item -> item.getId().equals(itemToDelete.getId()));

        saveFoodList(context,mealType, list);
    }

    public static void saveTotalMacros(Context context, MacroInfo macroInfo) {

        SharedPreferences prefs =
                context.getSharedPreferences(PREFS_MEALS, Context.MODE_PRIVATE);

        String json = new Gson().toJson(macroInfo);

        prefs.edit()
                .putString("TOTAL_MACROS", json)
                .apply();
    }

    public static MacroInfo getTotalMacros(Context context) {

        SharedPreferences prefs =
                context.getSharedPreferences(PREFS_MEALS, Context.MODE_PRIVATE);

        String json = prefs.getString("TOTAL_MACROS", null);

        if (json != null) {
            return new Gson().fromJson(json, MacroInfo.class);
        }

        // Si no hay datos guardados devolvemos valores en 0
        return new MacroInfo(0, 0, 0, 0);
    }
}
