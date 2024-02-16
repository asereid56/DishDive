package com.example.dishdive.db;

import android.content.Context;

import com.example.dishdive.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class MealLocalDataSource {
    String email;
    Context context;
    private static MealLocalDataSource mealLocalDataSource = null;
    private MealDao mealDao;
    private Flowable<List<Meal>> mealList;

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
        mealList = mealDao.getFavMeal(email);
    }

    public Flowable<List<Meal>> getListFavMeals(String email) {
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

    public Flowable<List<Meal>> getListPlanMeals(String email) {
        return mealDao.getPlannedMeals(email);
    }
}
