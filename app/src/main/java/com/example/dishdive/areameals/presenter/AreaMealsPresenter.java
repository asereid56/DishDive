package com.example.dishdive.areameals.presenter;

import android.content.Context;
import android.widget.Toast;

import com.example.dishdive.areameals.view.AreaMealView;
import com.example.dishdive.model.MealRepository;
import com.example.dishdive.model.PopularMeal;
import com.example.dishdive.network.meal.NetworkCallbackAreaMeals;

import java.util.List;

public class AreaMealsPresenter implements NetworkCallbackAreaMeals {
    MealRepository mealRepository;
    AreaMealView areaMealView;
    Context context;

    public AreaMealsPresenter(MealRepository mealRepository, AreaMealView areaMealView) {
        this.areaMealView = areaMealView;
        this.mealRepository = mealRepository;
    }

    @Override
    public void categoryMealsOnSuccess(List<PopularMeal> meal) {
        areaMealView.showAreaMeals(meal);
    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(context,
                 msg,
                Toast.LENGTH_SHORT).show();
    }
    public void fetchCategoryMeals(String name) {
        mealRepository.getMealsOfArea(name, this);

    }
}
