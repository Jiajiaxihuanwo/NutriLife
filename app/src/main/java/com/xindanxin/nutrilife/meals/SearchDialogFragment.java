package com.xindanxin.nutrilife.meals;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xindanxin.nutrilife.R;
import com.xindanxin.nutrilife.util.FoodRepository;
import com.xindanxin.nutrilife.util.MealsStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchDialogFragment extends DialogFragment {

    private SearchFoodAdapter adapter;
    private List<FoodItem> allFoods;

    public interface OnFoodSelectedListener {
        void onFoodSelected(FoodItem food);
    }

    private OnFoodSelectedListener listener;

    public SearchDialogFragment(OnFoodSelectedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // Inflamos el layout del pop up
        View view = getLayoutInflater()
                .inflate(R.layout.fragment_search_dialog, null);

        //Logica del buscador =============================================================

        EditText etSearch = view.findViewById(R.id.etSearch);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // no se necesita
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // no se necesita
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // destacamos que s es el contenido del texto
                buscarComida(s.toString().trim());
            }
        });


        //Logica del recycler view ========================================================

        // Referencia al RecyclerView usando la vista inflada
        RecyclerView rvResults = view.findViewById(R.id.rvResults);
        rvResults.setLayoutManager(new LinearLayoutManager(getContext()));

        //lista provisional
        allFoods = FoodRepository.getListaProvisional(requireContext());

        adapter = new SearchFoodAdapter(allFoods, food -> {
            //envía el alimento al fragmento padre.
            listener.onFoodSelected(food);

            //cierra el dialogo automáticamente
            dismiss();
        });

        rvResults.setAdapter(adapter);

        //Logica del boton cancel==========================================================
        Button btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> dismiss());

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

    private void buscarComida(String query) {

        if(query.isEmpty()) {
            adapter.updateList(allFoods);
            return;
        }

        List<FoodItem> filteredList = allFoods
                .stream().filter(f -> f.getMealName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());

        adapter.updateList(filteredList);
    }
}
