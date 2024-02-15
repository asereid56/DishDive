package com.example.dishdive.network.meal;

import com.example.dishdive.model.Ingredient;

import java.util.List;

public interface NetworkCallBackIngredients {
    public void getAllIngredientsOnSuccess (List<Ingredient> ingredients);
    public void onFailure(String msg);
}
