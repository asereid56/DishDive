package com.example.dishdive.ingredientmeals.presenter;

import android.util.Log;
import android.widget.Toast;

import com.example.dishdive.ingredientmeals.view.IngredientMealsView;
import com.example.dishdive.model.MealRepository;
import com.example.dishdive.model.PopularMeal;
import com.example.dishdive.network.meal.NetworkCallBackIngredientsMeals;

import java.util.List;

public class IngredientMealsPresenter implements NetworkCallBackIngredientsMeals {
    MealRepository mealRepository;
    IngredientMealsView view;

    public IngredientMealsPresenter(MealRepository mealRepository , IngredientMealsView view){
        this.mealRepository = mealRepository;
        this.view = view;
    }
    public void fetchIngredientMeals(String name){
        mealRepository.getMealsOfIngredients(name , this);
    }

    @Override
    public void ingredientsMealsOnSuccess(List<PopularMeal> meal) {
        view.showIngredientMeals(meal);
    }

    @Override
    public void onFailure(String msg) {
        Log.i("TAG", "onFailure: " + msg);
    }
}
