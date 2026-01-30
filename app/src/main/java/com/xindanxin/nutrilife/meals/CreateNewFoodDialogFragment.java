package com.xindanxin.nutrilife.meals;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.xindanxin.nutrilife.R;
import com.xindanxin.nutrilife.util.FoodRepository;
import com.xindanxin.nutrilife.util.MealsStorage;

public class CreateNewFoodDialogFragment extends DialogFragment {

    public interface OnAddNewFoodClickListener{
        void addNewFoodClick(String json);
    }

    OnAddNewFoodClickListener listener;

    public CreateNewFoodDialogFragment(OnAddNewFoodClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // Inflamos el layout del pop up
        View view = getLayoutInflater()
                .inflate(R.layout.fragment_create_food_dialog, null);



        //Logica del boton cancel==========================================================
        Button btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> dismiss());

        //Logica del boton add==========================================================
        Button btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> {

            EditText etNombre = view.findViewById(R.id.etNombre);
            EditText etKcal = view.findViewById(R.id.etKcal);
            EditText etProtein = view.findViewById(R.id.etProtein);
            EditText etCarbs = view.findViewById(R.id.etCarbs);
            EditText etFat = view.findViewById(R.id.etFat);

            if (!TextUtils.isEmpty(etNombre.getText()) &&
                    !TextUtils.isEmpty(etKcal.getText()) &&
                    !TextUtils.isEmpty(etProtein.getText()) &&
                    !TextUtils.isEmpty(etCarbs.getText()) &&
                    !TextUtils.isEmpty(etFat.getText())) {

                String macros = String.format("P:%sg C:%sg F:%sg",etProtein.getText().toString(),etCarbs.getText().toString(),etFat.getText().toString());
                FoodItem newFood = new FoodItem(MealsStorage.getNextFoodId(requireContext()),etNombre.getText().toString(),"08:00",etKcal.getText().toString()+" kcal",macros);
                listener.addNewFoodClick(new Gson().toJson(newFood));
                dismiss();
            }else{
                Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            }
        });

        //devuelve un objeto Dialog que Android mostrará cuando llames a dialog.show().
        return new AlertDialog.Builder(requireContext())
                .setView(view)
                .create();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
