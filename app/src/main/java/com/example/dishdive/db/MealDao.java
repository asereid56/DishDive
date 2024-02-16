package com.example.dishdive.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.dishdive.model.Meal;
import io.reactivex.rxjava3.core.Flowable;

import java.util.List;

@Dao
public interface MealDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertMeal(Meal meal);

    @Delete
    public void deleteMeal(Meal meal);

    @Query("SELECT * FROM mealInformation WHERE dbType = 'Fav' AND email = :userEmail")
    Flowable<List<Meal>> getFavMeal(String userEmail);

    @Query("SELECT * FROM mealInformation WHERE dbType ='Plan' AND email = :userEmail")
    Flowable<List<Meal>> getPlannedMeals(String userEmail);

}
