package com.xindanxin.nutrilife.meals;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xindanxin.nutrilife.R;

import java.util.List;

/**
 * Adapter para el RecyclerView de Meals
 * Cada item es una tarjeta expandible con:
 * - icono
 * - título
 * - botón de expandir/comprimir
 * - botón de añadir elemento
 * - contenido expandible (lista de elementos)
 */
public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.MealsViewHolder> {

    private List<String> items;
    private Context context;    // Contexto para crear TextView dinámicos

    public MealsAdapter(Context context, List<String> items) {
        this.context = context;
        this.items = items;
    }

    /**
     * ViewHolder de cada item del RecyclerView
     * Guarda las referencias a las vistas del item para optimizar el rendimiento
     */
    public static class MealsViewHolder extends RecyclerView.ViewHolder {

        LinearLayout cardHeader;
        ImageView icon;
        TextView title;
        ImageButton btnToggle, btnAdd;
        LinearLayout expandableContent;

        public MealsViewHolder(@NonNull View itemView) {
            super(itemView);
            cardHeader = itemView.findViewById(R.id.cardHeader);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            btnToggle = itemView.findViewById(R.id.btnToggle);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            expandableContent = itemView.findViewById(R.id.expandableContent);
        }
    }

    //Inflar el layout de cada item y crear un ViewHolder
    @NonNull
    @Override
    public MealsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meals_item_layout, parent, false);
        return new MealsViewHolder(view);
    }

    /**
     * Asignar los datos a cada item cuando aparece en pantalla
     */
    @Override
    public void onBindViewHolder(@NonNull MealsViewHolder holder, int position) {
        String item = items.get(position);
        holder.title.setText(item);

        //asignamos los iconos correspondientes
        Icon icon;
        int color;
        switch (item){
            case "Breakfast":
                icon = Icon.createWithResource(context,R.drawable.ic_breakfast);
                color = holder.itemView.getContext().getColor(R.color.whiteMeals);
                break;
            case "Lunch":
                icon = Icon.createWithResource(context,R.drawable.ic_lunch);
                color = holder.itemView.getContext().getColor(R.color.yellowMeals);
                break;
            case "Dinner":
                icon = Icon.createWithResource(context,R.drawable.ic_dinner);
                color = holder.itemView.getContext().getColor(R.color.purpleMeals);
                break;
            case "Snacks":
                icon = Icon.createWithResource(context,R.drawable.ic_snacks);
                color = holder.itemView.getContext().getColor(R.color.blueMeals);
                break;
            default:
                icon = Icon.createWithResource(context,R.drawable.ic_meals);
                color = holder.itemView.getContext().getColor(R.color.verdeProgress1Deshboard);
        }
        holder.icon.setImageIcon(icon);
        holder.cardHeader.setBackgroundColor(color);

        // Inicializamos el contenido como oculto
        holder.expandableContent.setVisibility(View.GONE);

        // Toggle expandir / comprimir
        holder.btnToggle.setOnClickListener(v -> {
            if (holder.expandableContent.getVisibility() == View.GONE) {
                holder.expandableContent.setVisibility(View.VISIBLE);
            } else {
                holder.expandableContent.setVisibility(View.GONE);
            }
        });

        // Añadir elemento dinámicamente
        holder.btnAdd.setOnClickListener(v -> {
            TextView newItem = new TextView(context);
            newItem.setText("Nuevo elemento");
            newItem.setPadding(8, 8, 8, 8);
            holder.expandableContent.addView(newItem);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
