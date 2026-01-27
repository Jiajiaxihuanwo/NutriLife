package com.xindanxin.nutrilife.meals;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xindanxin.nutrilife.R;
import com.xindanxin.nutrilife.util.MealsStorage;

import java.util.ArrayList;
import java.util.List;
public class Meals extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos el layout del fragment
        View view = inflater.inflate(R.layout.fragment_meals, container, false);

        // Referencia al RecyclerView usando la vista inflada
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Crear lista de ejemplo
        List<String> itemList = new ArrayList<>();
        itemList.add("Breakfast");
        itemList.add("Lunch");
        itemList.add("Dinner");
        itemList.add("Snacks");
        itemList.add("Default");


        // Crear adaptador y asignarlo
        MealsAdapter adapter = new MealsAdapter(
                requireContext(),
                itemList,
                mealType -> {

                    // AQUÍ abrimos el pop-up buscador
                    SearchDialogFragment dialog =
                            new SearchDialogFragment(selectedFood -> {

                                // Guardar el alimento
                                List<FoodItem> list =
                                        MealsStorage.loadFoodList(requireContext(), mealType);
                                list.add(selectedFood);
                                MealsStorage.saveFoodList(requireContext(), mealType, list);

                                // Refrescar RecyclerView
                                recyclerView.getAdapter().notifyDataSetChanged();
                            });

                    // mostramos el dialog
                    dialog.show(getParentFragmentManager(), "searchDialog");
                }
        );
        recyclerView.setAdapter(adapter);
        return view;

    }
}