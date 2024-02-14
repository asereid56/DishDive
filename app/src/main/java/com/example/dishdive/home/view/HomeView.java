package com.example.dishdive.home.view;

import com.example.dishdive.model.Meal;
import com.example.dishdive.model.PopularMeal;

import java.util.List;

public interface HomeView {
    void displayMealImage(String imageUrl , Meal meal);
    void showPopularImage(List<PopularMeal> meal);
}