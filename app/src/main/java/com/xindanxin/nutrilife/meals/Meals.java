package com.xindanxin.nutrilife.meals;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xindanxin.nutrilife.R;
import com.xindanxin.nutrilife.firestore.MealsStorageFirestore;
import com.xindanxin.nutrilife.util.CaloriesViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class Meals extends Fragment {

    private CaloriesViewModel caloriesViewModel;
    private MealsStorageFirestore firestoreStorage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos el layout del fragment
        View view = inflater.inflate(R.layout.fragment_meals, container, false);

        // Referencia al RecyclerView usando la vista inflada
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Crear lista de ejemplo
        List<String> items = Arrays.asList("breakfast", "lunch", "dinner", "snacks");

        caloriesViewModel = new ViewModelProvider(requireActivity())
                .get(CaloriesViewModel.class);


        // Inicializamos Firestore
        firestoreStorage = new MealsStorageFirestore();

        // Crear adaptador y asignarlo
        MealsAdapter adapter = new MealsAdapter(
                requireContext(),
                items,
                caloriesViewModel,
                mealType -> {

                    // AQUÍ abrimos el pop-up buscador
                    SearchDialogFragment dialog =
                            new SearchDialogFragment(selectedFood -> {

                                // GUARDAR EN FIRESTORE
                                firestoreStorage.addFoodItem(mealType, selectedFood);

                                // LEER DE FIRESTORE PARA ACTUALIZAR
                                firestoreStorage.getFoodItems(mealType, foodList -> {
                                    // Aquí puedes actualizar tu RecyclerView o ViewModel
                                    // Por ejemplo, notificar al adaptador
                                    recyclerView.getAdapter().notifyDataSetChanged();
                                });
                            });

                    // mostramos el dialog
                    dialog.show(getParentFragmentManager(), "searchDialog");
                }
        );
        recyclerView.setAdapter(adapter);
        return view;

    }
}