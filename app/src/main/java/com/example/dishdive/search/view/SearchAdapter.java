package com.example.dishdive.search.view;

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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder>{
    List<Meal> meals;
    MyViewHolder holder;
    Context context;
    OnItemClickListener listener;

    public SearchAdapter(Context context, List<Meal> meals) {
        this.context = context;
        this.meals = meals;
    }


    public interface OnItemClickListener {
        public void onItemClick(Meal meal);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
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
        Meal meal = meals.get(position);
        holder.ingredientName.setText(meal.getStrMeal());
        Glide.with(this.context).load(meals.get(position).getStrMealThumb().toString()).into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView ingredientName;
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ingredientName = view.findViewById(R.id.countryItem);
            img = view.findViewById(R.id.categoryimg);
        }
    }
}

