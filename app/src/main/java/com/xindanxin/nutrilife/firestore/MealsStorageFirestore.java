package com.xindanxin.nutrilife.firestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.xindanxin.nutrilife.meals.FoodItem;

public class MealsStorageFirestore extends FirestoreStorage {

    @Override
    protected String getBasePath() {
        return "users/" + getUid() + "/meals/";
    }

    // Guardar un FoodItem
    public void addFoodItem(String mealType, FoodItem item) {
        Map<String, Object> data = new HashMap<>();
        data.put("mealName", item.getMealName());
        data.put("calories", item.getCalories());
        data.put("protein", item.getProtein());
        data.put("carbs", item.getCarbs());
        data.put("fats", item.getFats());
        data.put("timestamp", System.currentTimeMillis());

        db.collection(getBasePath())
                .document(mealType)
                .collection("items")
                .add(data);
    }

    // Leer todos los items
    public void getFoodItems(String mealType, OnSuccessListener<List<FoodItem>> listener) {
        db.collection(getBasePath())
                .document(mealType)
                .collection("items")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<FoodItem> list = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        // Convertimos los valores a int ya que Firebase los guarda como Long
                        int calories = doc.getLong("calories").intValue();
                        int protein = doc.getLong("protein").intValue();
                        int carbs = doc.getLong("carbs").intValue();
                        int fats = doc.getLong("fats").intValue();

                        FoodItem f = new FoodItem(
                                doc.getString("mealName"),
                                calories,
                                protein,
                                carbs,
                                fats
                        );

                        f.setDocId(doc.getId());
                        list.add(f);
                    }
                    listener.onSuccess(list);
                });
    }

    // Eliminar un FoodItem por documentId
    public void deleteFoodItem(String mealType, String docId) {
        db.collection(getBasePath())
                .document(mealType)
                .collection("items")
                .document(docId)
                .delete();
    }
}