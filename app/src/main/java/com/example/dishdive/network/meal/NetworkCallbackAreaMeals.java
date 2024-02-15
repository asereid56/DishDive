package com.example.dishdive.network.meal;

import com.example.dishdive.model.PopularMeal;

import java.util.List;

public interface NetworkCallbackAreaMeals {
    public void categoryMealsOnSuccess(List<PopularMeal> meal);

    public void onFailure(String msg);
}
