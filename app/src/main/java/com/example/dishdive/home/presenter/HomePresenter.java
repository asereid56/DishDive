package com.example.dishdive.home.presenter;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.dishdive.home.view.HomeView;
import com.example.dishdive.model.Meal;
import com.example.dishdive.model.MealRepository;
import com.example.dishdive.model.PopularMeal;
import com.example.dishdive.network.meal.NetworkCallBackPopularMeal;
import com.example.dishdive.network.meal.NetworkCallBackRandomMeal;

import java.util.List;

public class HomePresenter implements NetworkCallBackRandomMeal, NetworkCallBackPopularMeal {
    MealRepository mealRepository;
    HomeView view;
    Context context;
    MutableLiveData<Meal> mealMutableLiveData = new MutableLiveData<>();
    MutableLiveData<PopularMeal> popularmealMutableLiveData = new MutableLiveData<>();

    public HomePresenter(HomeView view, MealRepository mealRepository) {
        this.view = view;
        this.mealRepository = mealRepository;
    }

    public LiveData<Meal> getMealLiveData() {
        return mealMutableLiveData;
    }

    public LiveData<PopularMeal> getPopularMealLiveData() {
        return popularmealMutableLiveData;
    }

    public void getRandomDailyMeal() {
        mealRepository.getRandomMeal(this);
    }

    public void getPopularMeals() {
        mealRepository.getPopularMeals(this);
    }

    @Override
    public void dealyMealOnSuccess(Meal meal) {
        mealMutableLiveData.postValue(meal);
    }

    @Override
    public void popularMealOnSuccess(List<PopularMeal> meal) {
        view.showPopularImage(meal);
    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public void syncALlMealToLocalDataBase(String email) {
        mealRepository.syncALlMealToLocalDataBase(email);
    }

}

