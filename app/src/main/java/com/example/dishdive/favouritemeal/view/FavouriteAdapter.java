package com.example.dishdive.favouritemeal.view;

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

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.MyViewHolder> {
    List<Meal> meals;

    FavouriteAdapter.MyViewHolder holder;
    Context context;
    OnFavClickListener onFavClickListener;
    OnItemClickListenerToDetails listener;
    public interface OnItemClickListenerToDetails {
        void onItemClick(Meal meal);
    }
    public void OnItemClickListenerToDetails(OnItemClickListenerToDetails listener) {
        this.listener = listener;
    }

    public FavouriteAdapter(Context context, List<Meal> meals , OnFavClickListener onFavClickListener) {
        this.context = context;
        this.meals = meals;
        this.onFavClickListener = onFavClickListener;

    }
    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    @NonNull
    @Override
    public FavouriteAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favourite_item, parent, false);
        holder = new FavouriteAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteAdapter.MyViewHolder holder, int position) {
        Meal meal = meals.get(position);
        Glide.with(this.context).load(meals.get(position).getStrMealThumb().toString()).into(holder.mealImg);
        holder.txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFavClickListener.OnMealsFavListener(meal);
            }
        });
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
        TextView txtDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImg = itemView.findViewById(R.id.meal_fav_img);
            txtDelete = itemView.findViewById(R.id.deleteMeal);
        }
    }

}
