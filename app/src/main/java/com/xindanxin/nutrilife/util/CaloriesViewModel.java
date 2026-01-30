package com.xindanxin.nutrilife.util;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.HashMap;
import java.util.Map;

public class CaloriesViewModel extends ViewModel {

    private final MutableLiveData<Map<String, MacroInfo>> macrosMap =
            new MutableLiveData<>(new HashMap<>());

    // Devuelve todos los macros
    public LiveData<Map<String, MacroInfo>> getMacros() {
        return macrosMap;
    }

    // Setea los macros de un mealType
    public void setMacros(String mealType, int calories, int protein, int carbs, int fat) {
        Map<String, MacroInfo> current = macrosMap.getValue();
        if (current == null) current = new HashMap<>();

        current.put(mealType, new MacroInfo(calories, protein, carbs, fat));
        macrosMap.setValue(current);
    }

    // Obtener totales de toda la lista
    public MacroInfo getTotalMacros() {
        int totalCalories = 0;
        int totalProtein = 0;
        int totalCarbs = 0;
        int totalFat = 0;

        Map<String, MacroInfo> current = macrosMap.getValue();
        if (current != null) {
            for (MacroInfo m : current.values()) {
                totalCalories += m.calories;
                totalProtein += m.protein;
                totalCarbs += m.carbs;
                totalFat += m.fat;
            }
        }

        return new MacroInfo(totalCalories, totalProtein, totalCarbs, totalFat);
    }
}
