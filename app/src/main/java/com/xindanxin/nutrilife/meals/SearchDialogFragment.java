package com.xindanxin.nutrilife.meals;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class SearchDialogFragment extends DialogFragment {

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

        EditText etSearch = view.findViewById(R.id.etSearch);

        // Referencia al RecyclerView usando la vista inflada
        RecyclerView rvResults = view.findViewById(R.id.rvResults);
        rvResults.setLayoutManager(new LinearLayoutManager(getContext()));

        //lista provisional
        List<FoodItem> foods = FoodRepository.getListaProvisional(requireContext());

        SearchFoodAdapter adapter = new SearchFoodAdapter(foods, food -> {
            //envía el alimento al fragmento padre.
            listener.onFoodSelected(food);

            //cierra el dialogo automáticamente
            dismiss();
        });

        rvResults.setAdapter(adapter);

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
    }
}
