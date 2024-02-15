package com.example.dishdive.search.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dishdive.R;
import com.example.dishdive.model.Ingredient;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.MyViewHolder> {
    List<Ingredient> ingredients;
    MyViewHolder holder;
    Context context;
    OnItemClickListener listener;

    public IngredientAdapter(Context context, List<Ingredient> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }


    public interface OnItemClickListener {
        public void onItemClick(Ingredient ingredient);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.country_item, parent, false);
        holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        this.holder = holder;
        Ingredient ingredient = ingredients.get(position);
        holder.ingredientName.setText(ingredient.getStrIngredient());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(ingredients.get(position));
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.i("TAG", "getItemCount: " +ingredients.size());
        return ingredients.size();

    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView ingredientName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ingredientName = view.findViewById(R.id.countryItem);
        }
    }
}
