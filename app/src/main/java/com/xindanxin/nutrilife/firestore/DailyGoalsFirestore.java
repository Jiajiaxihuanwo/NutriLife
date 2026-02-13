package com.xindanxin.nutrilife.firestore;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DailyGoalsFirestore {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String uid;
    private static final String DOC_NAME = "dailyGoals";

    public DailyGoalsFirestore(String uid) {
        this.uid = uid;
    }

    // Leer metas diarias
    public void getGoals(@NonNull OnSuccessListener<Map<String, Integer>> listener) {
        db.collection("users")
                .document(uid)
                .collection("goals")
                .document(DOC_NAME)
                .get()
                .addOnSuccessListener(document -> {
                    Map<String, Integer> goals = new HashMap<>();
                    if (document.exists()) {
                        goals.put("Cal", document.getLong("Cal") != null ? document.getLong("Cal").intValue() : 0);
                        goals.put("Carbs", document.getLong("Carbs") != null ? document.getLong("Carbs").intValue() : 0);
                        goals.put("Protein", document.getLong("Protein") != null ? document.getLong("Protein").intValue() : 0);
                        goals.put("Fats", document.getLong("Fats") != null ? document.getLong("Fats").intValue() : 0);
                        goals.put("Water", document.getLong("Water") != null ? document.getLong("Water").intValue() : 0);
                    } else {
                        // Inicializamos con 0 si no existe
                        goals.put("Cal", 0);
                        goals.put("Carbs", 0);
                        goals.put("Protein", 0);
                        goals.put("Fats", 0);
                        goals.put("Water", 0);
                    }
                    listener.onSuccess(goals);
                });
    }

    // Guardar metas diarias
    public void saveGoals(int cal, int carbs, int protein, int fats, int water) {
        Map<String, Object> data = new HashMap<>();
        data.put("Cal", cal);
        data.put("Carbs", carbs);
        data.put("Protein", protein);
        data.put("Fats", fats);
        data.put("Water", water);

        db.collection("users")
                .document(uid)
                .collection("goals")
                .document(DOC_NAME)
                .set(data);
    }
}