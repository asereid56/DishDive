package com.example.dishdive.db;

import android.content.Context;
import android.util.Log;


import com.example.dishdive.model.Meal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

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
    //String validEmail;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    // private ValueEventListener favValueEventListener;

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

//            setupFirebaseListener();
//            startFirebaseListener();
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
        Flowable.fromCallable(() -> {
            mealDao.insertMeal(meal);
            return true;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(result -> {
                    databaseReference.child(email.replace(".", "_")).child("Fav").child(meal.getIdMeal()).setValue(meal)
                            .addOnSuccessListener(aVoid -> Log.d("TAG", "Meal added to favorites successfully"))
                            .addOnFailureListener(e -> Log.e("TAG", "Error adding meal to favorites: " + e.getMessage()));

                },
                error -> {
                    // Handle error
                    Log.e("TAG", "Error inserting saved meal: " + error.getMessage());
                });
    }

    public void insertToPlan(Meal meal) {
        meal.setEmail(email);
        meal.setDbType("Plan");
        Flowable.fromCallable(() -> {
            mealDao.insertMeal(meal);
            return true;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(result -> {
            databaseReference.child(email.replace(".", "_")).child("Plan").child(meal.getDay()).child(meal.getIdMeal()).setValue(meal);
        }, e -> {
            Log.i("TAG", "Error to insertToPlan: " + e.getMessage());
        });
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
//    private void setupFirebaseListener() {
//        favValueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<Meal> updatedMeals = new ArrayList<>();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    // Parse Meal data from dataSnapshot
//                    Meal meal = dataSnapshot.getValue(Meal.class);
//                    if (meal != null) {
//                        updatedMeals.add(meal);
//                    }
//                }
//
//                // Update local database with the updated meals
//                Flowable.fromCallable(() -> {
//                    mealDao.deleteMeal(email); // Delete all previous favorite meals
//                    mealDao.insertMeal(updatedMeals); // Insert updated favorite meals
//                    return true;
//                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(result -> {
//                    // Update UI if necessary
//                }, e -> {
//                    Log.e("TAG", "Error updating local database: " + e.getMessage());
//                });
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("TAG", "Firebase onDataChange cancelled: " + error.getMessage());
//            }
//        };
//    }
//
//    private void startFirebaseListener() {
//        databaseReference.child(email.replace(".", "_")).child("Fav").addValueEventListener(favValueEventListener);
//    }
//
//    private void stopFirebaseListener() {
//        if (favValueEventListener != null) {
//            databaseReference.child(email.replace(".", "_")).child("Fav").removeEventListener(favValueEventListener);
//        }
//    }

}
