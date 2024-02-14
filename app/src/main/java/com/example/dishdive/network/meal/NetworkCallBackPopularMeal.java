package com.example.dishdive.network.meal;

import com.example.dishdive.model.PopularMeal;

import java.util.List;

public interface NetworkCallBackPopularMeal {
    public void popularMealOnSuccess(List<PopularMeal> meal);

    public void onFailure(String msg);
}
