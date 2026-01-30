package com.xindanxin.nutrilife.util;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.Map;

public class CaloriesViewModel extends ViewModel {

    private final MutableLiveData<Map<String, Integer>> caloriesMap =
            new MutableLiveData<>(new HashMap<>());

    public LiveData<Map<String, Integer>> getCalories() {
        return caloriesMap;
    }

    public void setCalories(String mealType, int calories) {
        Map<String, Integer> current = caloriesMap.getValue();
        if (current == null) current = new HashMap<>();

        current.put(mealType, calories);
        caloriesMap.setValue(current);
    }

    public int getTotalCalories() {
        int total = 0;
        Map<String, Integer> current = caloriesMap.getValue();
        if (current != null) {
            for (int c : current.values()) total += c;
        }
        return total;
    }
}
