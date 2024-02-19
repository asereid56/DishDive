package com.example.dishdive.profile.presenter;

import com.example.dishdive.db.MealLocalDataSource;
import com.example.dishdive.model.MealRepository;

public class ProfilePresenter {
    MealRepository mealRepository;
    public ProfilePresenter(MealRepository mealRepository){
        this.mealRepository = mealRepository;
    }
    public void SyncFav(String email){
        mealRepository.syncAllFavMeals(email);
    }

}
