package com.xindanxin.nutrilife.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.xindanxin.nutrilife.R;
import com.xindanxin.nutrilife.auth.Login;

public class Profile extends Fragment {

    private TextView tvName, tvWeight, tvAge, tvHeight, tvActivity;

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

        // Botón Sign Out
        AppCompatButton btnSignOut = view.findViewById(R.id.btnSingOut);
        btnSignOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(requireContext(), Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Botón Edit Profile
        AppCompatButton btnEdit = view.findViewById(R.id.btnEditProfile);

        btnEdit.setOnClickListener(v -> {
            new EditProfileDialogFragment((name, weight, height, age, activity) -> {
                TextView tvName = getView().findViewById(R.id.userName);
                TextView tvWeight = getView().findViewById(R.id.DatoPeso);
                TextView tvHeight = getView().findViewById(R.id.DatoAltura);
                TextView tvAge = getView().findViewById(R.id.DatoEdad);
                TextView tvActivity = getView().findViewById(R.id.DatoActividad);

                tvName.setText(name);
                tvWeight.setText(weight);
                tvHeight.setText(height);
                tvAge.setText(age);
                tvActivity.setText(activity);
            }).show(getParentFragmentManager(), "editProfileDialog");
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserData();
    }

    private void loadUserData() {
        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("UserProfile", Context.MODE_PRIVATE);

        tvName.setText(prefs.getString("name", "MiniChen"));
        tvWeight.setText(prefs.getString("weight", "68lb"));
        tvAge.setText(prefs.getString("age", "25 years"));
        tvHeight.setText(prefs.getString("height", "1.75"));
        tvActivity.setText(prefs.getString("activity", "Moderate"));
    }
}