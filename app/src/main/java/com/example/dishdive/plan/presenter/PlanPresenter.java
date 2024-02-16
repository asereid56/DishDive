package com.example.dishdive.plan.presenter;

import androidx.lifecycle.LiveData;

import com.example.dishdive.model.Meal;
import com.example.dishdive.model.MealRepository;
import com.example.dishdive.plan.view.PlanView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class PlanPresenter {
    MealRepository mealRepository;
    PlanView planView;
    String email;
    FirebaseAuth auth;
    FirebaseUser user;

    public PlanPresenter(PlanView planView ,MealRepository mealRepository) {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        email = user.getEmail();
        this.mealRepository = mealRepository;
        this.planView = planView;
    }

    public Flowable<List<Meal>> getPlanLiveData() {

        return mealRepository.getStoredPlannedMeals(email);
    }

    public void deleteFromPlanMeals(Meal meal) {
        mealRepository.deleteMealFromPlan(meal);
    }
}
