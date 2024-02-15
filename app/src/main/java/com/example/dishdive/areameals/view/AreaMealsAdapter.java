package com.example.dishdive.areameals.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dishdive.R;
import com.example.dishdive.model.PopularMeal;

import java.util.List;

public class AreaMealsAdapter extends RecyclerView.Adapter<AreaMealsAdapter.MyViewHolder> {
    List<PopularMeal> meals;
    MyViewHolder holder;
    Context context;
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(PopularMeal meal);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public AreaMealsAdapter(Context context, List<PopularMeal> meals) {
        this.context = context;
        this.meals = meals;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
        holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        this.holder = holder;
        PopularMeal meal = meals.get(position);
        holder.areaName.setText(meal.getStrMeal());
        Glide.with(this.context).load(meals.get(position).getStrMealThumb().toString()).into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(meals.get(position));
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public void setAreaMeals(List<PopularMeal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        View view;
        TextView areaName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            img = view.findViewById(R.id.categoryimg);
            areaName = view.findViewById(R.id.categoryName);
        }
    }
}
