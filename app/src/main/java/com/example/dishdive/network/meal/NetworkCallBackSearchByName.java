package com.example.dishdive.network.meal;

import com.example.dishdive.model.Meal;

import java.util.List;

public interface NetworkCallBackSearchByName {
    public void mealOnSuccess(List<Meal> meal);
    public void onFailure(String msg);
}
