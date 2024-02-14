
package com.example.dishdive.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PopularMealResponse{

    @SerializedName("meals")
    @Expose
    private List<PopularMeal> meals;

    public List<PopularMeal> getMeals() {
        return meals;
    }

    public void setMeals(List<PopularMeal> meals) {
        this.meals = meals;
    }

}
