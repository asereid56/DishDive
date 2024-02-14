package com.example.dishdive.network.meal;

import com.example.dishdive.model.Meal;

public interface NetworkCallBackDetailsOfMeal {
    public void getMealDetailsOnSuccess(Meal meal);

    public void onFailure(String msg);

}
