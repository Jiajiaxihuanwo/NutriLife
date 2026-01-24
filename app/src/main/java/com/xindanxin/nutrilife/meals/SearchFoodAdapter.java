package com.xindanxin.nutrilife.meals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xindanxin.nutrilife.R;

import java.util.List;

public class SearchFoodAdapter extends RecyclerView.Adapter<SearchFoodAdapter.ViewHolder> {

    public interface OnFoodClickListener {
        void onFoodClick(FoodItem food);
    }

    private List<FoodItem> foods;
    private OnFoodClickListener listener;

    public SearchFoodAdapter(List<FoodItem> foods, OnFoodClickListener listener) {
        this.foods = foods;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchFoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false); // Layout simple
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchFoodAdapter.ViewHolder holder, int position) {
        FoodItem food = foods.get(position);
        holder.textView.setText(food.getMealName()); // Muestra el nombre de la comida
        holder.itemView.setOnClickListener(v -> listener.onFoodClick(food));
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}