package com.example.dishdive.network.meal;

import com.example.dishdive.model.Meal;
import com.example.dishdive.model.PopularMeal;

import java.util.List;

public interface NetworkCallBackRandomMeal {
    public void dealyMealOnSuccess(Meal meal);
    public void onFailure(String msg);

}
