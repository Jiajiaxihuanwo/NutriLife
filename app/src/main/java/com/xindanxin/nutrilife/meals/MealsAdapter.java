package com.xindanxin.nutrilife.meals;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.xindanxin.nutrilife.R;
import com.xindanxin.nutrilife.util.CaloriesViewModel;
import com.xindanxin.nutrilife.util.MealsStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //interfaz callback
    public interface OnAddFoodClickListener {
        void onAddFoodClicked(String mealType);
    }
    private OnAddFoodClickListener addFoodListener;

    //mapa que guarda el estado de las cartas, expandido o no expandido
    private final Map<String, Boolean> expandedStates = new HashMap<>();

    private List<String> items;
    private Context context;    // Contexto para crear TextView dinámicos

    private CaloriesViewModel viewModel;

    public MealsAdapter(Context context, List<String> items, CaloriesViewModel viewModel, OnAddFoodClickListener listener) {
        this.context = context;
        this.items = items;
        this.viewModel = viewModel;
        this.addFoodListener = listener;
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

        TextView tvTotalCalories;

        public MealsViewHolder(@NonNull View itemView) {
            super(itemView);
            cardHeader = itemView.findViewById(R.id.cardHeader);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            btnToggle = itemView.findViewById(R.id.btnToggle);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            expandableContent = itemView.findViewById(R.id.expandableContent);
            tvTotalCalories = itemView.findViewById(R.id.tvTotalCalories);
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
        String mealType = items.get(position);
        holder.title.setText(mealType);

        //asignamos los iconos correspondientes
        Icon icon;
        int color;
        int dColor;
        switch (mealType){
            case "Breakfast":
                icon = Icon.createWithResource(context,R.drawable.ic_breakfast);
                color = holder.itemView.getContext().getColor(R.color.whiteMeals);
                dColor = holder.itemView.getContext().getColor(R.color.d_whiteMeals);
                break;
            case "Lunch":
                icon = Icon.createWithResource(context,R.drawable.ic_lunch);
                color = holder.itemView.getContext().getColor(R.color.yellowMeals);
                dColor = holder.itemView.getContext().getColor(R.color.d_yellowMeals);
                break;
            case "Dinner":
                icon = Icon.createWithResource(context,R.drawable.ic_dinner);
                color = holder.itemView.getContext().getColor(R.color.purpleMeals);
                dColor = holder.itemView.getContext().getColor(R.color.d_purpleMeals);
                break;
            case "Snacks":
                icon = Icon.createWithResource(context,R.drawable.ic_snacks);
                color = holder.itemView.getContext().getColor(R.color.blueMeals);
                dColor = holder.itemView.getContext().getColor(R.color.d_blueMeals);
                break;
            default:
                icon = Icon.createWithResource(context,R.drawable.ic_meals);
                color = holder.itemView.getContext().getColor(R.color.defaultMeals);
                dColor = holder.itemView.getContext().getColor(R.color.black);
        }
        holder.icon.setImageIcon(icon);
        holder.cardHeader.setBackgroundColor(color);
        holder.btnAdd.setImageTintList(ColorStateList.valueOf(dColor));

        //Inyectamos la comida guardada limpiandolo anteriormente para que no se repita los alimentos
        holder.expandableContent.removeAllViews();

        List<FoodItem> savedItems = MealsStorage.loadFoodList(context,mealType);
        for (FoodItem f : savedItems) {
            LinearLayout itemContent = createItemLayout(mealType,f);
            holder.expandableContent.addView(itemContent);
        }

        // Inicializamos el contenido como oculto
        holder.expandableContent.setVisibility(View.GONE);

        // Toggle expandir / comprimir
        holder.btnToggle.setOnClickListener(v -> {
            boolean currentlyExpanded = holder.expandableContent.getVisibility() == View.VISIBLE;
            boolean newState = !currentlyExpanded;

            holder.expandableContent.setVisibility(newState ? View.VISIBLE : View.GONE);
            holder.btnToggle.setImageIcon(
                    Icon.createWithResource(context, newState ? R.drawable.ic_toggle_on : R.drawable.ic_toggle_off)
            );

            expandedStates.put(mealType, newState); // guardar el estado
        });

        // Calcular las calorias totales de todos los alimentos
        int totalCalories = calculateTotalCalories(mealType);
        holder.tvTotalCalories.setText(totalCalories + " kcal");

        this.viewModel.setCalories(mealType, totalCalories); //actualizamos los datos al padre

        // Añadir elemento dinámicamente
        holder.btnAdd.setOnClickListener(v -> {
            if(addFoodListener != null){
                // Aquí llamamos al métod0 del callback que implementa la clase que creó el Adapter
                // Pasamos "mealType" para que el receptor sepa a qué seccion se le ha pulsado el botón
                // En este caso, el FragmentMeals implementa el comportamiento: abrir el pop-up buscador
                addFoodListener.onAddFoodClicked(mealType);
            }

            //tambien expandimos por defecto el card
            holder.expandableContent.setVisibility(View.VISIBLE);
            //lo seteamos como true en visivility
            expandedStates.put(mealType,true);
        });

        //boleano que establece si el toggle esta espandido o no
        boolean isExpanded = expandedStates.getOrDefault(mealType, false);
        holder.expandableContent.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.btnToggle.setImageIcon(
                Icon.createWithResource(context, isExpanded ? R.drawable.ic_toggle_on : R.drawable.ic_toggle_off)
        );
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

        // Columna eliminar
        LinearLayout trashColumn = new LinearLayout(context);
        trashColumn.setOrientation(LinearLayout.VERTICAL);
        trashColumn.setGravity(Gravity.END);
        LinearLayout.LayoutParams trashParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        trashParams.setMarginStart((int)(15 * context.getResources().getDisplayMetrics().density));
        trashColumn.setLayoutParams(trashParams);

        ImageButton btnDelete = new ImageButton(context);
        btnDelete.setImageResource(R.drawable.ic_trash);
        btnDelete.setBackgroundColor(Color.TRANSPARENT);

        btnDelete.setOnClickListener(v -> {

            MealsStorage.deleteFoodItem(context,mealType, item);

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

    //metodo para calcular las calorias totales de una tarjeta
    private int calculateTotalCalories(String mealType) {
        List<FoodItem> foods = MealsStorage.loadFoodList(context, mealType);
        int total = 0;

        for (FoodItem item : foods) {
            total += Integer.parseInt(item.getCalories().split(" ")[0]);
        }
        return total;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


}
