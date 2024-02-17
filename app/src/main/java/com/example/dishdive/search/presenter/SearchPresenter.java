package com.example.dishdive.search.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.dishdive.model.Category;
import com.example.dishdive.model.Country;
import com.example.dishdive.model.Ingredient;
import com.example.dishdive.model.Meal;
import com.example.dishdive.model.MealRepository;
import com.example.dishdive.model.PopularMeal;
import com.example.dishdive.network.meal.NetworkCallBackCategories;
import com.example.dishdive.network.meal.NetworkCallBackCountries;
import com.example.dishdive.network.meal.NetworkCallBackIngredients;
import com.example.dishdive.network.meal.NetworkCallBackSearchByName;
import com.example.dishdive.network.meal.NetworkCallbackAreaMeals;
import com.example.dishdive.search.view.SearchView;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter implements NetworkCallBackCategories, NetworkCallBackCountries, NetworkCallBackIngredients , NetworkCallBackSearchByName {
    MealRepository mealRepository;
    SearchView view;
    Context context;

    public SearchPresenter(MealRepository mealRepository, SearchView view , Context context) {
        this.mealRepository = mealRepository;
        this.view = view;
        this.context = context;
    }

    public void onChipSelected(String chipText) {
        if (chipText.equals("Category")) {
            mealRepository.getCategories(this);
        } else if (chipText.equals("Ingredient")) {
            mealRepository.getAllIngredients(this);
        } else if (chipText.equals("Country")) {
            mealRepository.getCountries(this);
        }
    }

    @Override
    public void getCategoriesOnSuccess(List<Category> category) {
        view.showCategories(category);
    }

    @Override
    public void countryOnSuccess(List<Country> countries) {
        view.showCountries(countries);
    }

    @Override
    public void getAllIngredientsOnSuccess(List<Ingredient> ingredients) {
        view.showIngredients(ingredients);
    }
    public void searchMealByName(String mealName) {
        mealRepository.getMealsByName(mealName, this);

    }
    @Override
    public void mealOnSuccess(List<Meal> meal) {
        if (meal != null) {
            view.showMealsByName(meal);
        } else {
            Toast.makeText(context, "No Meals Founded", Toast.LENGTH_SHORT).show();
            view.showMealsByName(new ArrayList<>());
        }

    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(context,
                 msg,
                Toast.LENGTH_SHORT).show();
    }
}
