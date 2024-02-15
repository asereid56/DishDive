package com.example.dishdive.network.meal;

import com.example.dishdive.model.CategoryResponse;
import com.example.dishdive.model.CountryResponse;
import com.example.dishdive.model.IngredientResponse;
import com.example.dishdive.model.MealResponse;
import com.example.dishdive.model.PopularMealResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealsServices {
    public final static String ID = "i";
    @GET("random.php")
    Call<MealResponse> getRandomMealForADay();
    @GET("lookup.php?")
    Call<MealResponse> getMealDetails(@Query(ID) String id);
    @GET("filter.php?")
    Call<PopularMealResponse> getPopularMeal(@Query("c") String categoryName);
    @GET("categories.php")
    Call<CategoryResponse> getCategories();
    @GET("filter.php")
    Call<PopularMealResponse> getCategoryMeals(@Query("c") String categoryName);
    @GET("list.php?a=list")
    Call<CountryResponse> getCountries();

    @GET("filter.php")
    Call<PopularMealResponse> getAreaMeals(@Query("a") String categoryName);

    @GET("list.php?i=list")
    Call<IngredientResponse> getIngredients();

}