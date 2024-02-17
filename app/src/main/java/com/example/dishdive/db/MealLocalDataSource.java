package com.example.dishdive.db;

import android.content.Context;

import com.example.dishdive.model.Meal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class MealLocalDataSource {
    String email;
    Context context;
    private static MealLocalDataSource mealLocalDataSource = null;
    private MealDao mealDao;
    private Flowable<List<Meal>> mealListFav;
    private Flowable<List<Meal>> mealListPlan;
    List<Meal> firebaseMeals;
    FirebaseAuth auth;
    FirebaseUser user;

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
        mealListFav = mealDao.getFavMeal(email);
        mealListPlan = mealDao.getPlannedMeals(email);
        firebaseMeals = new ArrayList<>();
        if (getCurrentUser() != null) {
            email = getCurrentUser().getEmail();
        }
    }

    public Flowable<List<Meal>> getListFavMeals(String email) {
        return mealDao.getFavMeal(email);
    }

    public void deleteFromFav(Meal meal) {
        meal.setDbType("Fav");
        meal.setEmail(email);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mealDao.deleteMeal(meal);
            }
        }).start();
    }

    public void insertToFav(Meal meal) {
        meal.setDbType("Fav");
        meal.setEmail(email);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mealDao.insertMeal(meal);
            }
        }).start();
    }

    public void insertToPlan(Meal meal) {
        meal.setEmail(email);
        meal.setDbType("Plan");
        new Thread(new Runnable() {
            @Override
            public void run() {
                mealDao.insertMeal(meal);
            }
        }).start();
    }

    public void deleteFromPlan(Meal meal) {
        meal.setDbType("Plan");
        meal.setEmail(email);
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

    private FirebaseUser getCurrentUser() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        return user;
    }
}
