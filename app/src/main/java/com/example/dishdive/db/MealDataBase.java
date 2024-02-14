package com.example.dishdive.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.dishdive.model.Meal;

@Database(entities = {Meal.class} , version = 1 )
public abstract class MealDataBase extends RoomDatabase {
    private static MealDataBase instance = null;
    public abstract MealDao getMealDao();
    public static synchronized MealDataBase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), MealDataBase.class, "mealdb").build();

        }
        return instance ;
    }
}
