package com.xindanxin.nutrilife.firestore;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeightStorageFirestore {

    private static final List<String> DAY_OF_WEEK =
            List.of("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String uid;

    public WeightStorageFirestore(String uid) {
        this.uid = uid;
    }

    // Leer pesos
    public void getWeights(OnSuccessListener<List<Integer>> listener) {
        db.collection("users")
                .document(uid)
                .collection("weights")
                .document("daily")
                .get()
                .addOnSuccessListener(document -> {
                    List<Integer> weights = new ArrayList<>();
                    if (document.exists()) {
                        for (String day : DAY_OF_WEEK) {
                            Long val = document.getLong(day);
                            weights.add(val != null ? val.intValue() : 0);
                        }
                    } else {
                        // Si no existe, inicializamos con 0
                        for (int i = 0; i < 7; i++) weights.add(0);
                    }
                    listener.onSuccess(weights);
                });
    }

    // Guardar pesos
    public void saveWeights(List<Integer> list) {
        if (list.size() < 7) return;

        Map<String, Object> data = new HashMap<>();
        for (int i = 0; i < 7; i++) {
            data.put(DAY_OF_WEEK.get(i), list.get(i));
        }

        db.collection("users")
                .document(uid)
                .collection("weights")
                .document("daily")
                .set(data);
    }
}