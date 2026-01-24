package com.xindanxin.nutrilife.meals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xindanxin.nutrilife.R;

import java.util.ArrayList;
import java.util.List;

public class SearchFoodAdapter extends RecyclerView.Adapter<SearchFoodAdapter.FoodViewHolder> {
    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView mealName;
        TextView calories;
        TextView macros;
        FoodViewHolder(View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.tvMealName);
            calories = itemView.findViewById(R.id.tvCalories);
            macros = itemView.findViewById(R.id.tvMacros);
        }
    }

    public interface OnFoodClickListener {
        void onFoodClick(FoodItem food);
    }

    private List<FoodItem> foods = new ArrayList<>();
    private OnFoodClickListener listener;

    public SearchFoodAdapter(List<FoodItem> foods, OnFoodClickListener listener) {
        this.listener = listener;

        //guardamos una copia mutable de la lista que nos pasen
        updateList(foods);
    }

    // metodo para actualizar la lista
    public void updateList(List<FoodItem> newFoods){
        this.foods.clear();
        this.foods.addAll(newFoods);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_item_layout, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem food = foods.get(position);
        holder.mealName.setText(food.getMealName()); // Muestra el nombre de la comida
        holder.calories.setText(food.getCalories());
        holder.macros.setText(food.getMacros());

        //itemView corresponde a toda la linea, por lo tanto doy una accion al presionar una linea
        holder.itemView.setOnClickListener(v -> listener.onFoodClick(food));
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

}