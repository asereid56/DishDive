package com.example.dishdive.favouritemeal.presenter;

import androidx.lifecycle.LiveData;

import com.example.dishdive.favouritemeal.view.FavouriteView;
import com.example.dishdive.model.Meal;
import com.example.dishdive.model.MealRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class FavouritePresenter {
    String email;
    FirebaseAuth auth;
    FirebaseUser user;
    private FavouriteView view;
    private MealRepository repository;

    public FavouritePresenter(FavouriteView view, MealRepository repository) {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        email = user.getEmail();
        this.view = view;
        this.repository = repository;
    }

    public Flowable<List<Meal>> showFavMeals() {
        return repository.getStoredFavMeal(email);
    }

    public void deleteFromFav(Meal meal) {
        repository.deleteMealFromFav(meal);
    }
}
