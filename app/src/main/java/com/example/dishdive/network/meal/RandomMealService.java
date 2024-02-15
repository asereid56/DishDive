package com.example.dishdive.network.meal;

import com.example.dishdive.model.CategoryResponse;
import com.example.dishdive.model.MealResponse;
import com.example.dishdive.model.PopularMealResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RandomMealService {
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

}
