package com.xindanxin.nutrilife.firestore;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeightStorageFirestore extends FirestoreStorage {

    private static final List<String> DAY_OF_WEEK =
            List.of("Mon","Tue","Wed","Thu","Fri","Sat","Sun");

    @Override
    protected String getBasePath() {
        return "users/" + getUid() + "/weights/";
    }

    public void getWeights(OnSuccessListener<List<Integer>> listener) {
        DocumentReference ref = db.document(getBasePath() + "weekly");
        ref.get().addOnSuccessListener(documentSnapshot -> {
            List<Integer> weights = new ArrayList<>();
            if(documentSnapshot.exists()) {
                for(String day: DAY_OF_WEEK){
                    Long val = documentSnapshot.getLong(day);
                    weights.add(val != null ? val.intValue() : 0);
                }
            } else {
                for(int i=0;i<7;i++) weights.add(0);
            }
            listener.onSuccess(weights);
        });
    }

    public void save(List<Integer> list) {
        if(list.size() < 7) return;
        Map<String,Object> data = new HashMap<>();
        for(int i=0;i<7;i++){
            data.put(DAY_OF_WEEK.get(i), list.get(i));
        }
        db.document(getBasePath()+"weekly").set(data);
    }
}
