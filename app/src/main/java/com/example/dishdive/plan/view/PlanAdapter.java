package com.example.dishdive.plan.view;


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

import com.example.dishdive.model.Meal;


import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.MyViewHolder> {
    List<Meal> meals;

    PlanAdapter.MyViewHolder holder;
    OnItemClickListener listener;
    Context context;
    OnPlanClickListener onPlanClickListener;

    public PlanAdapter(Context context, List<Meal> meals, OnPlanClickListener onPlanClickListener) {
        this.context = context;
        this.meals = meals;
        this.onPlanClickListener = onPlanClickListener;

    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    @NonNull
    @Override
    public PlanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.planitem, parent, false);
        holder = new PlanAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlanAdapter.MyViewHolder holder, int position) {
        Meal meal = meals.get(position);
        Glide.with(this.context).load(meals.get(position).getStrMealThumb().toString()).into(holder.mealImg);
        holder.txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlanClickListener.OnMealsPlanDeleteListener(meal);
            }
        });
        holder.day.setText(meals.get(position).getDay());
        holder.nameOfMeal.setText(meals.get(position).getStrMeal());
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView mealImg;
        TextView day;
        TextView txtDelete;
        TextView nameOfMeal;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImg = itemView.findViewById(R.id.mealPlanImg);
            txtDelete = itemView.findViewById(R.id.btnDelete);
            day = itemView.findViewById(R.id.dayOfPlanMeal);
            nameOfMeal = itemView.findViewById(R.id.mealPlanName);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(Meal meal);
    }

    public void setOnItemClickListener(PlanAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}

