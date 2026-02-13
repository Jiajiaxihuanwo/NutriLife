package com.xindanxin.nutrilife.util;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.HashMap;
import java.util.Map;

public class CaloriesViewModel extends ViewModel {

    // Mapa de macros por mealType
    private final MutableLiveData<Map<String, MacroInfo>> macrosMap =
            new MutableLiveData<>(new HashMap<>());

    // LiveData de totales (calorías, proteínas, carbs, fats)
    private final MutableLiveData<MacroInfo> totalMacrosLiveData =
            new MutableLiveData<>(new MacroInfo(0,0,0,0));

    // Devuelve todos los macros por mealType
    public LiveData<Map<String, MacroInfo>> getMacros() {
        return macrosMap;
    }

    // Devuelve los totales de toda la lista
    public LiveData<MacroInfo> getTotalMacros() {
        return totalMacrosLiveData;
    }

    // Setea los macros de un mealType y actualiza totales
    public void setMacros(String mealType, int calories, int protein, int carbs, int fat) {
        Map<String, MacroInfo> current = macrosMap.getValue();
        if (current == null) current = new HashMap<>();

        // Actualiza o agrega el mealType
        current.put(mealType, new MacroInfo(calories, protein, carbs, fat));
        macrosMap.setValue(current);

        // Recalcula totales
        int totalCalories = 0, totalProtein = 0, totalCarbs = 0, totalFat = 0;
        for (MacroInfo m : current.values()) {
            totalCalories += m.getCalories();
            totalProtein += m.getProtein();
            totalCarbs += m.getCarbs();
            totalFat += m.getFats();
        }

        totalMacrosLiveData.setValue(new MacroInfo(totalCalories, totalProtein, totalCarbs, totalFat));
    }
}