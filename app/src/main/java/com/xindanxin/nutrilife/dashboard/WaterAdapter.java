package com.xindanxin.nutrilife.dashboard;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.xindanxin.nutrilife.R;

import java.util.List;

public class WaterAdapter extends RecyclerView.Adapter<WaterAdapter.WaterViewHolder>{
    List<Boolean> items;

    public WaterAdapter(List<Boolean> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public WaterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.water_item_layout,parent,false);
        return new WaterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WaterViewHolder holder, int position) {
        boolean haBebido = items.get(position);
        if(haBebido){
            holder.icon.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(),R.color.blueWaterDeshboard_ic));
            holder.icon.setBackgroundTintList(
                    ColorStateList.valueOf(
                            ContextCompat.getColor(
                                    holder.itemView.getContext(),
                                    R.color.blueWaterDeshboard
                            )
                    )
            );

        }else {
            holder.icon.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(),R.color.grey));
            holder.icon.setBackgroundTintList(
                    ColorStateList.valueOf(
                            ContextCompat.getColor(
                                    holder.itemView.getContext(),
                                    R.color.light_grey
                            )
                    )
            );

        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class WaterViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;

        public WaterViewHolder(@NonNull View itemView) {
            super(itemView);
            this.icon = itemView.findViewById(R.id.icono);
        }
    }


}
