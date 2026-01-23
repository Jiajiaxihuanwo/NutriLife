package com.xindanxin.nutrilife.meals;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.view.Gravity;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xindanxin.nutrilife.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
        String eatName = items.get(position);
        holder.title.setText(eatName);

        //asignamos los iconos correspondientes
        Icon icon;
        int color;
        switch (eatName){
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
                color = holder.itemView.getContext().getColor(R.color.defaultMeals);
        }
        holder.icon.setImageIcon(icon);
        holder.cardHeader.setBackgroundColor(color);

        //Inyectamos la comida guardada
        List<FoodItem> savedItems = loadFoodList("foodList_"+eatName);
        for (FoodItem f : savedItems) {
            LinearLayout itemContent = createItemLayout(eatName,f);
            holder.expandableContent.addView(itemContent);
        }

        // Inicializamos el contenido como oculto
        holder.expandableContent.setVisibility(View.GONE);

        // Toggle expandir / comprimir
        holder.btnToggle.setOnClickListener(v -> {
            if (holder.expandableContent.getVisibility() == View.GONE) {
                holder.expandableContent.setVisibility(View.VISIBLE);
                holder.btnToggle.setImageIcon(Icon.createWithResource(context,R.drawable.ic_toggle_on));
            } else {
                holder.expandableContent.setVisibility(View.GONE);
                holder.btnToggle.setImageIcon(Icon.createWithResource(context,R.drawable.ic_toggle_off));
            }
        });

        // Añadir elemento dinámicamente
        holder.btnAdd.setOnClickListener(v -> {
            // Crear nuevo item
            FoodItem newItem = new FoodItem("Grilled Chicken Salad", "12:30 PM", "420 cal", "P:35g C:25g F:18g");

            // Añadir visualmente
            LinearLayout itemContent = createItemLayout(eatName,newItem);
            holder.expandableContent.addView(itemContent);

            // Guardar persistencia
            List<FoodItem> currentList = loadFoodList("foodList_"+eatName);
            currentList.add(newItem);
            saveFoodList("foodList_"+eatName,currentList);
        });
    }

    //metodo para crear el layout que contiene la informacion del food
    private LinearLayout createItemLayout(String mealType,FoodItem item) {
        // Layout principal
        LinearLayout itemContent = new LinearLayout(context);
        itemContent.setOrientation(LinearLayout.HORIZONTAL);
        itemContent.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        int paddingInPx = (int) (16 * context.getResources().getDisplayMetrics().density);
        itemContent.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx);

        // Columna izquierda
        LinearLayout leftColumn = new LinearLayout(context);
        leftColumn.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams leftParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
        );
        leftColumn.setLayoutParams(leftParams);

        TextView tvMealName = new TextView(context);
        tvMealName.setText(item.getMealName());
        tvMealName.setTextSize(14);
        tvMealName.setTextColor(ContextCompat.getColor(context, R.color.black));
        leftColumn.addView(tvMealName);

        TextView tvMealTime = new TextView(context);
        tvMealTime.setText(item.getMealName());
        tvMealTime.setTextSize(12);
        tvMealTime.setTextColor(ContextCompat.getColor(context, R.color.grey));
        leftColumn.addView(tvMealTime);

        // Columna derecha
        LinearLayout rightColumn = new LinearLayout(context);
        rightColumn.setOrientation(LinearLayout.VERTICAL);
        rightColumn.setGravity(Gravity.END);
        LinearLayout.LayoutParams rightParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        rightColumn.setLayoutParams(rightParams);

        TextView tvCalories = new TextView(context);
        tvCalories.setText(item.getCalories());
        tvCalories.setTextSize(14);
        tvCalories.setTextColor(ContextCompat.getColor(context, R.color.black));
        tvCalories.setGravity(Gravity.END);
        rightColumn.addView(tvCalories);

        TextView tvMacros = new TextView(context);
        tvMacros.setText(item.getMacros());
        tvMacros.setTextSize(12);
        tvMacros.setTextColor(ContextCompat.getColor(context, R.color.grey));
        rightColumn.addView(tvMacros);

        //añadir boton de eliminar con columna eliminar
        // Columna eliminar
        LinearLayout trashColumn = new LinearLayout(context);
        trashColumn.setOrientation(LinearLayout.VERTICAL);
        trashColumn.setGravity(Gravity.END);
        LinearLayout.LayoutParams trashParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        trashColumn.setLayoutParams(rightParams);

        ImageButton btnDelete = new ImageButton(context);
        btnDelete.setImageResource(R.drawable.ic_trash);
        btnDelete.setBackgroundColor(Color.TRANSPARENT);

        btnDelete.setOnClickListener(v -> {

            deleteFoodItem(mealType, item);

            ViewGroup parent = (ViewGroup) itemContent.getParent();
            if (parent != null) {
                parent.removeView(itemContent);
            }
        });

        trashColumn.addView(btnDelete);

        // Añadir columnas al layout principal
        itemContent.addView(leftColumn);
        itemContent.addView(rightColumn);
        itemContent.addView(trashColumn);

        return itemContent;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    //metodos de persistencia con ShharedPreferences

    /**
     * Guarda la lista de alimentos en SharedPreferences como JSON.
     * Esto permite que los datos persistan aunque cierres la app.
     */
    private void saveFoodList(String key,List<FoodItem> foodList) {
        // Obtenemos el SharedPreferences con nombre "MealsPrefs" en modo privado
        SharedPreferences prefs = context.getSharedPreferences("MealsPrefs", Context.MODE_PRIVATE);
        // Creamos un editor para modificar el SharedPreferences
        SharedPreferences.Editor editor = prefs.edit();
        //    Gson es una librería que convierte objetos Java en JSON (texto) y viceversa
        Gson gson = new Gson();
        String json = gson.toJson(foodList);
        editor.putString(key, json);
        editor.apply();
    }

    private List<FoodItem> loadFoodList(String key) {
        SharedPreferences prefs = context.getSharedPreferences("MealsPrefs", Context.MODE_PRIVATE);
        //    Si no existe, devolvemos un String vacío ""
        String json = prefs.getString(key, "");
        if (json.isEmpty()) return new ArrayList<>();

        //    Aquí usamos TypeToken para indicarle que es una lista de FoodItem
        //    Esto es necesario porque en Java, la información de "List<FoodItem>" se pierde por el tipo genérico
        Type type = new TypeToken<List<FoodItem>>() {}.getType();
        return new Gson().fromJson(json, type);
    }

    private void deleteFoodItem(String mealType, FoodItem itemToDelete) {

        List<FoodItem> list = loadFoodList(mealType);

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(itemToDelete.getId())) {
                list.remove(i);
                break;
            }
        }

        saveFoodList(mealType, list);
    }
}
