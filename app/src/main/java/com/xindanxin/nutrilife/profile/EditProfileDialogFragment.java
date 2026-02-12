package com.xindanxin.nutrilife.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.widget.AppCompatButton;

import com.xindanxin.nutrilife.R;

public class EditProfileDialogFragment extends DialogFragment {

    public interface OnProfileUpdatedListener {
        void onProfileUpdated(String name, String weight, String height, String age, String activity);
    }

    private final OnProfileUpdatedListener listener;

    public EditProfileDialogFragment(OnProfileUpdatedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflamos el layout que me enviaste
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        // EditTexts del layout
        EditText etUserName = view.findViewById(R.id.etUserName);
        EditText etWeight = view.findViewById(R.id.etWeight);
        EditText etHeight = view.findViewById(R.id.etHeight);
        EditText etAge = view.findViewById(R.id.etAge);
        Spinner spinnerActivity = view.findViewById(R.id.spinnerActivity);

        // Botones
        AppCompatButton btnSave = view.findViewById(R.id.btnSaveProfile);
        AppCompatButton btnCancel = view.findViewById(R.id.btnCancelProfile);

        // Cargamos datos desde SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
        etUserName.setText(prefs.getString("name", ""));
        etWeight.setText(prefs.getString("weight", ""));
        etHeight.setText(prefs.getString("height", ""));
        etAge.setText(prefs.getString("age", ""));

        // Spinner de actividad
        String[] activities = {"Sedentary", "Light", "Moderate", "Active", "Very Active"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, activities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerActivity.setAdapter(adapter);
        String currentActivity = prefs.getString("activity", "Moderate");
        spinnerActivity.setSelection(adapter.getPosition(currentActivity));

        // Guardar cambios
        btnSave.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("name", etUserName.getText().toString());
            editor.putString("weight", etWeight.getText().toString());
            editor.putString("height", etHeight.getText().toString());
            editor.putString("age", etAge.getText().toString());
            editor.putString("activity", spinnerActivity.getSelectedItem().toString());
            editor.apply();

            if (listener != null) {
                listener.onProfileUpdated(
                        etUserName.getText().toString(),
                        etWeight.getText().toString(),
                        etHeight.getText().toString(),
                        etAge.getText().toString(),
                        spinnerActivity.getSelectedItem().toString()
                );
            }

            dismiss();
        });

        btnCancel.setOnClickListener(v -> dismiss());



        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            // Hacer que ocupe casi todo el ancho de la pantalla
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            int height = ViewGroup.LayoutParams.WRAP_CONTENT; // altura automática
            getDialog().getWindow().setLayout(width, height);
        }
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}