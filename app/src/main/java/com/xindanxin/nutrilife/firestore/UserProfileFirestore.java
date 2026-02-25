package com.xindanxin.nutrilife.firestore;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserProfileFirestore {
    //instancia conecta con la base de datos
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String uid;
    private static final String DOC_NAME = "profile";

    public UserProfileFirestore(String uid) {
        this.uid = uid;
    }

    // Leer perfil
    public void getProfile(@NonNull OnSuccessListener<Map<String, String>> listener) {
        db.collection("users")
                .document(uid)
                .collection("userData")
                .document(DOC_NAME)
                .get()
                .addOnSuccessListener(document -> {
                    Map<String, String> profile = new HashMap<>();
                    if (document.exists()) {
                        profile.put("name", document.getString("name") != null ? document.getString("name") : "");
                        profile.put("weight", document.getString("weight") != null ? document.getString("weight") : "");
                        profile.put("height", document.getString("height") != null ? document.getString("height") : "");
                        profile.put("age", document.getString("age") != null ? document.getString("age") : "");
                        profile.put("activity", document.getString("activity") != null ? document.getString("activity") : "Moderate");
                    } else {
                        // Inicializamos con valores vacíos
                        profile.put("name", "");
                        profile.put("weight", "");
                        profile.put("height", "");
                        profile.put("age", "");
                        profile.put("activity", "Moderate");
                    }
                    //listener para devolver los datos(profile o Dialog Profile
                    listener.onSuccess(profile);
                });
    }

    // Guardar perfil
    public void saveProfile(String name, String weight, String height, String age, String activity) {
        Map<String, Object> data = new HashMap<>();
        //cGuarda datos
        data.put("name", name);
        data.put("weight", weight);
        data.put("height", height);
        data.put("age", age);
        data.put("activity", activity);
        //Guarda en FireBase
        db.collection("users")
                .document(uid)
                .collection("userData")
                .document(DOC_NAME)
                .set(data);
    }
}