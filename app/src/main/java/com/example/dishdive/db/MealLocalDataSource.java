package com.example.dishdive.db;

import android.content.Context;
import android.util.Log;


import androidx.lifecycle.LiveData;

import com.example.dishdive.model.Meal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

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
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

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
        firebaseMeals = new ArrayList<>();
        if (getCurrentUser() != null) {
            email = getCurrentUser().getEmail();
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("meals");
            mealListFav = mealDao.getFavMeal(email);
            mealListPlan = mealDao.getPlannedMeals(email);

        }
    }

    public Flowable<List<Meal>> getListFavMeals(String email) {
        return mealDao.getFavMeal(email);
    }

    public void deleteFromFav(Meal meal) {
        meal.setDbType("Fav");
        meal.setEmail(email);
        Flowable.fromCallable(() -> {
            mealDao.deleteMeal(meal);
            return true;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(result -> {
            databaseReference.child(email.replace(".", "_")).child("Fav").child(meal.getIdMeal()).removeValue();
        }, e -> {
            Log.i("TAG", " Error in deleteFromFav: " + e.getMessage());
        });
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

//        Flowable.fromCallable(() -> {
//            mealDao.insertMeal(meal);
//            return true;
//        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(result -> {
//                    databaseReference.child(email.replace(".", "_")).child("Fav").child(meal.getIdMeal()).setValue(meal)
//                            .addOnSuccessListener(aVoid -> Log.d("TAG", "Meal added to favorites successfully"))
//                            .addOnFailureListener(e -> Log.e("TAG", "Error adding meal to favorites: " + e.getMessage()));
//
//                },
//                error -> {
//                    // Handle error
//                    Log.e("TAG", "Error inserting saved meal: " + error.getMessage());
//                });
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
        Flowable.fromCallable(() -> {
            mealDao.deleteMeal(meal);
            return true;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(result -> {
            databaseReference.child(email.replace(".", "_")).child("Plan").child(meal.getDay()).child(meal.getIdMeal()).removeValue();
        }, e -> {
            Log.i("TAG", "Error in deleteFromPlan: " + e.getMessage());
        });
    }

    public Flowable<List<Meal>> getListPlanMeals(String email) {
        return mealDao.getPlannedMeals(email);
    }

    private FirebaseUser getCurrentUser() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        return user;
    }

    public void syncRealtimeDatabase(String email) {
        DatabaseReference userFavRef = databaseReference.child(email.replace(".", "_")).child("Fav");
        DatabaseReference userPlanRef = databaseReference.child(email.replace(".", "_")).child("Plan");


        Flowable<List<Meal>> localFavMeals = mealDao.getFavMeal(email);
        localFavMeals.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(meals -> {
                    for (Meal meal : meals) {
                        userFavRef.child(meal.getIdMeal()).setValue(meal);
                    }
                }, throwable -> {
                    Log.e("TAG", "Error syncing favorite meals to Realtime Database", throwable);
                });


        Flowable<List<Meal>> localPlanMeals = mealDao.getPlannedMeals(email);
        localPlanMeals.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(meals -> {
                    for (Meal meal : meals) {
                        userPlanRef.child(meal.getDay()).child(meal.getIdMeal()).setValue(meal);
                    }
                }, throwable -> {
                    Log.e("TAG", "Error syncing Plan meals to Realtime Database", throwable);
                });


    }

}
