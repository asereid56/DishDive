package com.example.dishdive.mealdetails.presenter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.dishdive.db.MealLocalDataSource;
import com.example.dishdive.mealdetails.view.DetailsMealFragment;
import com.example.dishdive.mealdetails.view.MealDetailsView;
import com.example.dishdive.model.Meal;
import com.example.dishdive.model.MealRepository;
import com.example.dishdive.network.meal.MealRemoteDataSource;
import com.example.dishdive.network.meal.NetworkCallBackDetailsOfMeal;
import com.google.firebase.auth.FirebaseAuth;

public class MealDetailsPresenter implements NetworkCallBackDetailsOfMeal {
    MealRepository mealRepository;
    MealDetailsView view;
    Context context;

    public MealDetailsPresenter(DetailsMealFragment detailsMealFragment, MealRepository mealRepository) {
        this.view = detailsMealFragment;
        this.mealRepository = mealRepository;
    }

    public void getMealDetails(String id) {
        mealRepository.getDetailsOfMeal(id, this);
    }

    @Override
    public void getMealDetailsOnSuccess(Meal meal) {
        view.setMealDetails(meal);
    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public void addToFav(Meal meal) {
        mealRepository.insertMealToFav(meal);
    }

    public void addToPlan(Meal meal) {
        mealRepository.insertMealToPlan(meal);
    }
}
