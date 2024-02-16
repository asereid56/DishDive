package com.example.dishdive.network.meal;

import com.example.dishdive.model.CategoryResponse;
import com.example.dishdive.model.CountryResponse;
import com.example.dishdive.model.IngredientResponse;
import com.example.dishdive.model.MealResponse;
import com.example.dishdive.model.PopularMeal;
import com.example.dishdive.model.PopularMealResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealsServices {
    @GET("random.php")
    Single<MealResponse> getRandomMealForADay();

    @GET("lookup.php?")
    Single<MealResponse> getMealDetails(@Query("i") String id);

    @GET("filter.php?")
    Single<PopularMealResponse> getPopularMeal(@Query("c") String categoryName);

    @GET("categories.php")
    Single<CategoryResponse> getCategories();

    @GET("filter.php")
    Single<PopularMealResponse> getCategoryMeals(@Query("c") String categoryName);

    @GET("list.php?a=list")
    Single<CountryResponse> getCountries();

    @GET("filter.php")
    Single<PopularMealResponse> getAreaMeals(@Query("a") String categoryName);

    @GET("list.php?i=list")
    Single<IngredientResponse> getIngredients();

    @GET("filter.php")
    Single<PopularMealResponse> getIngredientsMeals(@Query("i") String ingredientName);

    @GET("search.php")
    Single<MealResponse> getMealsByName(@Query("s") String mealName);

}
