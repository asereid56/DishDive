package com.example.dishdive.db;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.dishdive.model.Meal;

import java.util.List;

public class MealLocalDataSource {
    String email;
    Context context;
    private static MealLocalDataSource mealLocalDataSource = null;
    private MealDao mealDao;
    private LiveData<List<Meal>> listLiveMeals;

    public static MealLocalDataSource getInstance(Context context) {
        if (mealLocalDataSource == null) {
            mealLocalDataSource = new MealLocalDataSource(context);
        }
        return mealLocalDataSource;
    }

    private MealLocalDataSource(Context context) {
        this.context = context;
        MealDataBase db = MealDataBase.getInstance(context.getApplicationContext());
        mealDao = db.getMealDao();
        listLiveMeals = mealDao.getFavMeal(email);
    }

    public LiveData<List<Meal>> getListLiveFavMeals(String email) {
        return mealDao.getFavMeal(email);
    }

    public void deleteFromFav(Meal meal) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mealDao.deleteMeal(meal);
            }
        }).start();
    }

    public void insertToFav(Meal meal) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                meal.setDbType("Fav");
                mealDao.insertMeal(meal);
            }
        }).start();
    }

    public void insertToPlan(Meal meal) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                meal.setDbType("Plan");
                mealDao.insertMeal(meal);
            }
        }).start();
    }

    public void deleteFromPlan(Meal meal) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mealDao.deleteMeal(meal);
            }
        }).start();
    }

    public LiveData<List<Meal>> getListLivePlanMeals(String email) {
        return mealDao.getPlannedMeals(email);
    }
}
