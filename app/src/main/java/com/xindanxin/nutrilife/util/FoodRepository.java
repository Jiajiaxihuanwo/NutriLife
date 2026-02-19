package com.xindanxin.nutrilife.util;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.xindanxin.nutrilife.meals.FoodItem;

import java.util.ArrayList;
import java.util.List;

public class FoodRepository {

    private static final String COLLECTION_BASE = "foods_base";
    private static FoodRepository instance; // singleton

    private List<FoodItem> cachedFoods = null; // cache en memoria
    private boolean isLoading = false;
    private List<OnFoodsLoadedListener> pendingListeners = new ArrayList<>();

    private FoodRepository() { }

    public static synchronized FoodRepository getInstance() {
        if (instance == null) {
            instance = new FoodRepository();
        }
        return instance;
    }

    /**
     * Obtiene la lista de alimentos. Si ya se cargó, devuelve la cache inmediatamente.
     * Si no, carga desde Firestore y notifica a todos los listeners, esto evita tener que
     * consultar a firebase cada vez que busquemos algo.
     */
    public void getFoods(OnFoodsLoadedListener listener) {
        if (cachedFoods != null) {
            // Ya tenemos cache, devolvemos inmediatamente
            listener.onFoodsLoaded(cachedFoods);
            return;
        }

        // Agregamos listener pendiente
        pendingListeners.add(listener);

        // Si ya está cargando, no hacemos otra petición
        if (isLoading) return;

        isLoading = true;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_BASE)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        cachedFoods = new ArrayList<>();
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                String nombre = doc.getString("nombre");
                                int calorias = doc.getLong("calorias") != null ? doc.getLong("calorias").intValue() : 0;
                                int grasas = doc.getDouble("grasas") != null ? doc.getDouble("grasas").intValue() : 0;
                                int carbohidratos = doc.getDouble("carbohidratos") != null ? doc.getDouble("carbohidratos").intValue() : 0;
                                int proteinas = doc.getDouble("proteinas") != null ? doc.getDouble("proteinas").intValue() : 0;

                                cachedFoods.add(new FoodItem(nombre, calorias, grasas, carbohidratos, proteinas));
                            }
                        }

                        // Notificar a todos los listeners pendientes
                        for (OnFoodsLoadedListener l : pendingListeners) {
                            l.onFoodsLoaded(cachedFoods);
                        }
                        pendingListeners.clear();
                        isLoading = false;
                    }
                });
    }

    // Interfaz de callback
    public interface OnFoodsLoadedListener {
        void onFoodsLoaded(List<FoodItem> foods);
    }

    /**
     * Limpiar cache (opcional)
     */
    public void clearCache() {
        cachedFoods = null;
    }
}