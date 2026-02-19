package com.xindanxin.nutrilife.firestore;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public abstract class FirestoreStorage {

    protected FirebaseFirestore db;

    protected FirestoreStorage() {
        db = FirebaseFirestore.getInstance();
    }

    protected String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    protected abstract String getBasePath();
}
