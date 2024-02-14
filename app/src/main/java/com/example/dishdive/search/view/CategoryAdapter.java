package com.example.dishdive.search.view;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dishdive.R;
import com.example.dishdive.model.Category;
import com.example.dishdive.model.PopularMeal;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    List<Category> categories;
    MyViewHolder holder;
    Context context;
    //OnItemClickListener listener;

//    public interface OnItemClickListener {
//        void onItemClick(Category category);
//    }

//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.listener = listener;
//    }

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
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
        Category category = categories.get(position);
        holder.categoryName.setText(category.getStrCategory());
        Glide.with(this.context).load(categories.get(position).getStrCategoryThumb().toString()).into(holder.img);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (listener != null) {
//                    int position = holder.getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        listener.onItemClick(popularMeals.get(position));
//                    }
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        View view;
        TextView categoryName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            img = view.findViewById(R.id.categoryimg);
            categoryName = view.findViewById(R.id.categoryName);
        }
    }
}
