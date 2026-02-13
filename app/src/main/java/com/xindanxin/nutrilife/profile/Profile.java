package com.xindanxin.nutrilife.profile;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.xindanxin.nutrilife.R;
import com.xindanxin.nutrilife.auth.Login;
import com.xindanxin.nutrilife.firestore.UserProfileFirestore;
import com.xindanxin.nutrilife.firestore.DailyGoalsFirestore;

import java.util.Map;

public class Profile extends Fragment {

    private TextView tvName, tvWeight, tvAge, tvHeight, tvActivity;
    private EditText etCal, etProtein, etCarbs, etFats, etWater;
    private DailyGoalsFirestore dailyGoalsFirestore;

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // TextViews de perfil
        tvName = view.findViewById(R.id.userName);
        tvWeight = view.findViewById(R.id.DatoPeso);
        tvAge = view.findViewById(R.id.DatoEdad);
        tvHeight = view.findViewById(R.id.DatoAltura);
        tvActivity = view.findViewById(R.id.DatoActividad);

        // EditTexts Daily Goals
        etCal = view.findViewById(R.id.etDailyCal);
        etProtein = view.findViewById(R.id.etProteinGoal);
        etCarbs = view.findViewById(R.id.etCarbsGoal);
        etFats = view.findViewById(R.id.etFatGoals);
        etWater = view.findViewById(R.id.etWaterGoals);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        UserProfileFirestore firestore = new UserProfileFirestore(uid);
        dailyGoalsFirestore = new DailyGoalsFirestore(uid);

        // Sign out
        AppCompatButton btnSignOut = view.findViewById(R.id.btnSingOut);
        btnSignOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(requireContext(), Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Edit profile
        AppCompatButton btnEdit = view.findViewById(R.id.btnEditProfile);
        btnEdit.setOnClickListener(v -> {
            new EditProfileDialogFragment((name, weight, height, age, activity) -> {
                tvName.setText(name);
                tvWeight.setText(weight);
                tvHeight.setText(height);
                tvAge.setText(age);
                tvActivity.setText(activity);
            }).show(getParentFragmentManager(), "editProfileDialog");
        });

        // Guardar Daily Goals
        AppCompatButton btnSaveGoals = view.findViewById(R.id.btnSave);
        btnSaveGoals.setOnClickListener(v -> saveDailyGoals());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserDataFromFirestore();
        loadDailyGoalsFromFirestore();
    }

    // Cargar datos del perfil
    private void loadUserDataFromFirestore() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        UserProfileFirestore firestore = new UserProfileFirestore(uid);

        firestore.getProfile(profile -> {
            tvName.setText(profile.get("name"));
            tvWeight.setText(profile.get("weight"));
            tvHeight.setText(profile.get("height"));
            tvAge.setText(profile.get("age"));
            tvActivity.setText(profile.get("activity"));
        });
    }

    // Cargar Daily Goals
    private void loadDailyGoalsFromFirestore() {
        dailyGoalsFirestore.getGoals(goals -> {
            etCal.setText(String.valueOf(goals.get("Cal")));
            etProtein.setText(String.valueOf(goals.get("Protein")));
            etCarbs.setText(String.valueOf(goals.get("Carbs")));
            etFats.setText(String.valueOf(goals.get("Fats")));
            etWater.setText(String.valueOf(goals.get("Water")));
        });
    }

    // Guardar Daily Goals en Firestore
    private void saveDailyGoals() {
        try {
            int cal = Integer.parseInt(etCal.getText().toString());
            int protein = Integer.parseInt(etProtein.getText().toString());
            int carbs = Integer.parseInt(etCarbs.getText().toString());
            int fats = Integer.parseInt(etFats.getText().toString());
            int water = Integer.parseInt(etWater.getText().toString());

            dailyGoalsFirestore.saveGoals(cal, carbs, protein, fats, water);
            Toast.makeText(requireContext(), "Daily Goals updated!", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }
}