package com.example.dishdive.home.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dishdive.R;
import com.example.dishdive.model.PopularMeal;

import java.util.List;

public class MostPopularAdapter extends RecyclerView.Adapter<MostPopularAdapter.MyPopularViewHolder> {
    List<PopularMeal> popularMeals;
    MyPopularViewHolder holder;
    Context context;
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(PopularMeal meal);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MostPopularAdapter(Context context, List<PopularMeal> popularMeals) {
        this.context = context;
        this.popularMeals = popularMeals;
    }


    @NonNull
    @Override
    public MyPopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.popular_item, parent, false);
        holder = new MyPopularViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyPopularViewHolder holder, int position) {
        this.holder = holder;
        PopularMeal meal = popularMeals.get(position);

        Glide.with(this.context).load(popularMeals.get(position).getStrMealThumb().toString()).into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(popularMeals.get(position));
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return popularMeals.size();
    }

    public void setPopularMeals(List<PopularMeal> meals) {
        this.popularMeals = meals;
        notifyDataSetChanged();
    }

    public class MyPopularViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        View view;

        public MyPopularViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            img = view.findViewById(R.id.meal_popular_img);
        }
    }
}
