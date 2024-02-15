package com.example.dishdive.categorymeals.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.example.dishdive.categorymeals.view.CategoryMealsView;
import com.example.dishdive.model.Meal;
import com.example.dishdive.model.MealRepository;
import com.example.dishdive.model.PopularMeal;
import com.example.dishdive.network.meal.NetworkCallBackCategoryMeals;
import com.example.dishdive.network.meal.NetworkCallBackCountries;

import java.util.List;
public class CategoryMealsPresenter implements NetworkCallBackCategoryMeals {
    MealRepository mealRepository;
    CategoryMealsView view;
    Context context;
    public CategoryMealsPresenter(MealRepository mealRepository , CategoryMealsView view){
        this.mealRepository = mealRepository;
        this.view = view;
    }

    @Override
    public void categoryMealsOnSuccess(List<PopularMeal> meal) {
        view.showCategoriesMeals(meal);
    }

    public void fetchCategoryMeals(String name) {
        mealRepository.getMealsOfCategory(name, this);

    }


    @Override
    public void onFailure(String msg) {
        Toast.makeText(context,
                "Failed to load meal: " + msg,
                Toast.LENGTH_SHORT).show();
    }
}
