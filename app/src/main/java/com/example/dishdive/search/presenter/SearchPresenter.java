package com.example.dishdive.search.presenter;

import android.content.Context;
import android.widget.Toast;

import com.example.dishdive.model.Category;
import com.example.dishdive.model.MealRepository;
import com.example.dishdive.network.meal.MealRemoteDataSource;
import com.example.dishdive.network.meal.NetworkCallBackCategories;
import com.example.dishdive.search.view.SearchView;

import java.util.List;

public class SearchPresenter implements NetworkCallBackCategories {
    MealRepository mealRepository;
    SearchView view;
    Context context;
    public SearchPresenter(MealRepository mealRepository , SearchView view){
        this.mealRepository = mealRepository;
        this.view = view;
    }
    public void onChipSelected(String chipText){
        if(chipText.equals("Category")){
            mealRepository.getCategories(this);
        }else if(chipText.equals("Ingredient")){

        } else if (chipText.equals("Country")) {

        }
    }

    @Override
    public void getCategoriesOnSuccess(List<Category> category) {
      view.showCategories(category);
    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(context,
                "Failed to load meal: " + msg,
                Toast.LENGTH_SHORT).show();
    }
}
