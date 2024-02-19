package com.example.dishdive.model;

import androidx.lifecycle.LiveData;

import com.example.dishdive.db.MealLocalDataSource;
import com.example.dishdive.network.meal.MealRemoteDataSource;
import com.example.dishdive.network.meal.NetworkCallBackCategories;
import com.example.dishdive.network.meal.NetworkCallBackCategoryMeals;
import com.example.dishdive.network.meal.NetworkCallBackCountries;
import com.example.dishdive.network.meal.NetworkCallBackDetailsOfMeal;
import com.example.dishdive.network.meal.NetworkCallBackIngredients;
import com.example.dishdive.network.meal.NetworkCallBackIngredientsMeals;
import com.example.dishdive.network.meal.NetworkCallBackPopularMeal;
import com.example.dishdive.network.meal.NetworkCallBackRandomMeal;
import com.example.dishdive.network.meal.NetworkCallBackSearchByName;
import com.example.dishdive.network.meal.NetworkCallbackAreaMeals;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class MealRepository {
    MealRemoteDataSource mealRemoteDataSource;
    MealLocalDataSource mealLocalDataSource;
    private static MealRepository mealRepository = null;

    public static MealRepository getInstance(MealLocalDataSource mealLocalDataSource, MealRemoteDataSource mealRemoteDataSource) {
        if (mealRepository == null) {
            mealRepository = new MealRepository(mealLocalDataSource, mealRemoteDataSource);
        }
        return mealRepository;
    }

    private MealRepository(MealLocalDataSource mealLocalDataSource, MealRemoteDataSource mealRemoteDataSource) {
        this.mealRemoteDataSource = mealRemoteDataSource;
        this.mealLocalDataSource = mealLocalDataSource;
    }

    public void getRandomMeal(NetworkCallBackRandomMeal networkCallBackRandomMeal) {
        mealRemoteDataSource.makeNetworkCallForDailyMail(networkCallBackRandomMeal);
    }

    public void getAllIngredients(NetworkCallBackIngredients networkCallBackIngredients) {
        mealRemoteDataSource.makeNetworkCallForIngredients(networkCallBackIngredients);
    }

    public void getCategories(NetworkCallBackCategories networkCallBackCategories) {
        mealRemoteDataSource.makeNetworkCallForCategories(networkCallBackCategories);
    }

    public void getMealsOfCategory(String categoryName, NetworkCallBackCategoryMeals networkCallBackCategoryMeals) {
        mealRemoteDataSource.makeNetworkCallForCategoryMeals(categoryName, networkCallBackCategoryMeals);
    }

    public void getMealsOfIngredients(String ingredientName, NetworkCallBackIngredientsMeals networkCallBack) {
        mealRemoteDataSource.makeNetworkCallForIngredientsMeals(ingredientName, networkCallBack);
    }

    public void getMealsByName(String nameOfMeal, NetworkCallBackSearchByName networkCallBack) {
        mealRemoteDataSource.makeNetworkCallForSearchByName(nameOfMeal, networkCallBack);
    }


    public void getMealsOfArea(String areaName, NetworkCallbackAreaMeals networkCallbackAreaMeals) {
        mealRemoteDataSource.makeNetworkCallForAreaMeals(areaName, networkCallbackAreaMeals);
    }

    public void getCountries(NetworkCallBackCountries networkCallBackCountries) {
        mealRemoteDataSource.makeNetworkCallForCountries(networkCallBackCountries);
    }

    public void getPopularMeals(NetworkCallBackPopularMeal networkCallBackPopularMeal) {
        mealRemoteDataSource.makeNetworkCallForPopularMeal(networkCallBackPopularMeal);
    }

    public void getDetailsOfMeal(String mealID, NetworkCallBackDetailsOfMeal networkCallBackDetailsOfMeal) {
        mealRemoteDataSource.makeNetworkCallForGetDetailsOfMeal(mealID, networkCallBackDetailsOfMeal);
    }

    public void insertMealToFav(Meal meal) {
        mealLocalDataSource.insertToFav(meal);
    }

    public void deleteMealFromFav(Meal meal) {
        mealLocalDataSource.deleteFromFav(meal);
    }

    public Flowable<List<Meal>> getStoredFavMeal(String email) {
        return mealLocalDataSource.getListFavMeals(email);
    }


    public void insertMealToPlan(Meal meal) {
        mealLocalDataSource.insertToPlan(meal);
    }

    public void deleteMealFromPlan(Meal meal) {
        mealLocalDataSource.deleteFromPlan(meal);
    }

    public Flowable<List<Meal>> getStoredPlannedMeals(String email) {
        return mealLocalDataSource.getListPlanMeals(email);
    }
//    public void setLocalDataSource(MealLocalDataSource localDataSource){
//        this.mealLocalDataSource = mealLocalDataSource;
//    }
    public void syncAllFavMeals(String email){
        mealLocalDataSource.syncRealtimeDatabase(email);
    }
}
