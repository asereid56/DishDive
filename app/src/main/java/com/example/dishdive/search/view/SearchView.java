package com.example.dishdive.search.view;


import com.example.dishdive.model.Category;
import com.example.dishdive.model.Country;
import com.example.dishdive.model.Ingredient;
import com.example.dishdive.model.Meal;

import java.util.List;

public interface SearchView {
    void showCategories(List<Category> categories);
    void showCountries(List<Country> countries);
    void showIngredients(List<Ingredient> ingredients);
    void showMealsByName(List<Meal> meals);
}
