package com.xindanxin.nutrilife.firestore;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.xindanxin.nutrilife.meals.FoodItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserFoodsFirestore {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String uid;
    private static final String COLLECTION_USER_FOODS = "foods"; // colección de alimentos del usuario

    public UserFoodsFirestore(String uid) {
        this.uid = uid;
    }

    /**
     * Guarda un nuevo alimento del usuario
     */
    public void addFood(FoodItem food) {
        Map<String, Object> data = new HashMap<>();
        data.put("nombre", food.getMealName());
        data.put("calorias", food.getCalories());
        data.put("grasas", food.getFats());
        data.put("carbohidratos", food.getCarbs());
        data.put("proteinas", food.getProtein());

        db.collection("users")
                .document(uid)
                .collection(COLLECTION_USER_FOODS)
                .add(data); // Firestore asigna un ID automáticamente
    }

    /**
     * Obtiene todos los alimentos del usuario
     */
    public void getFoods(OnSuccessListener<List<FoodItem>> listener) {
        db.collection("users")
                .document(uid)
                .collection(COLLECTION_USER_FOODS)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<FoodItem> foods = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        String nombre = doc.getString("nombre");
                        int calorias = doc.getLong("calorias") != null ? doc.getLong("calorias").intValue() : 0;
                        int grasas = doc.getDouble("grasas") != null ? doc.getDouble("grasas").intValue() : 0;
                        int carbohidratos = doc.getDouble("carbohidratos") != null ? doc.getDouble("carbohidratos").intValue() : 0;
                        int proteinas = doc.getDouble("proteinas") != null ? doc.getDouble("proteinas").intValue() : 0;

                        foods.add(new FoodItem(nombre, calorias, grasas, carbohidratos, proteinas));
                    }
                    listener.onSuccess(foods);
                });
    }
}