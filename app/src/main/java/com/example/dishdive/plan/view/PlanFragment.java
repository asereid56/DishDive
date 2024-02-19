package com.example.dishdive.plan.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dishdive.R;
import com.example.dishdive.db.MealLocalDataSource;
import com.example.dishdive.login.view.LoginScreen;
import com.example.dishdive.mealdetails.view.DetailsMealFragment;
import com.example.dishdive.model.Meal;
import com.example.dishdive.model.MealRepository;
import com.example.dishdive.network.meal.MealRemoteDataSource;
import com.example.dishdive.plan.presenter.PlanPresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class PlanFragment extends Fragment implements PlanView, OnPlanClickListener {
    PlanPresenter planPresenter;
    private RecyclerView recyclerView;
    private PlanAdapter planAdapter;
    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = getActivity().getSharedPreferences("AuthState", Context.MODE_PRIVATE);
        if (preferences.getBoolean("isLoggedIn", false)) {
            setupPlannedFragmentFragment();
        } else {
            showLoginDialog();
        }
    }

    private void setupPlannedFragmentFragment() {
        Context applicationContext = requireContext().getApplicationContext();
        planPresenter = new PlanPresenter(this, MealRepository.getInstance(MealLocalDataSource.getInstance(getContext()), MealRemoteDataSource.getInstance(applicationContext)));
        recyclerView = getView().findViewById(R.id.recycleView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        planAdapter = new PlanAdapter(getContext(), new ArrayList<>(), this);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(planAdapter);

        planPresenter.getPlanLiveData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updatePlanMeals,
                        e -> {
                            Toast.makeText(getContext(), "Nothing to show", Toast.LENGTH_SHORT).show();
                        });
        planAdapter.setOnItemClickListener(new PlanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Meal meal) {
                navigateToDetailsFragment(meal);
            }
        });

    }

    public void updatePlanMeals(List<Meal> meals) {
        planAdapter.setMeals(meals);
        planAdapter.notifyDataSetChanged();
    }


    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Login Required");
        builder.setMessage("Some features are available only when you are logged in.");
        builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Navigate to login screen
                Intent intent = new Intent(getActivity(), LoginScreen.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        builder.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                getActivity().onBackPressed();
            }
        });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void OnMealsPlanDeleteListener(Meal meal) {
        Toast.makeText(getContext(), "Removed", Toast.LENGTH_SHORT).show();
        planPresenter.deleteFromPlanMeals(meal);
        updateDayFlags(meal.getDay());

    }

    private void updateDayFlags(String day) {
        SharedPreferences.Editor editor = preferences.edit();
        switch (day) {
            case "Saturday":
                editor.putBoolean("addedToSaturday", false);
                break;
            case "Sunday":
                editor.putBoolean("addedToSunday", false);
                break;
            case "Monday":
                editor.putBoolean("addedToMonday", false);
                break;
            case "Tuesday":
                editor.putBoolean("addedToTuesday", false);
                break;
            case "Wednesday":
                editor.putBoolean("addedToWednesday", false);
                break;
            case "Thursday":
                editor.putBoolean("addedToThursday", false);
                break;
            case "Friday":
                editor.putBoolean("addedToFriday", false);
                break;
        }
        editor.apply();
    }

    @Override
    public void showPlanMeal(List<Meal> meal) {
        planAdapter.setMeals(meal);
        planAdapter.notifyDataSetChanged();

    }

    private void navigateToDetailsFragment(Meal meal) {
        PlanFragmentDirections.ActionPlanFragmentToDetailsMealFragment action =
                PlanFragmentDirections.actionPlanFragmentToDetailsMealFragment(
                        meal.getIdMeal(), meal.getStrMeal(), meal.getStrMealThumb());

        NavHostFragment.findNavController(this).navigate(action);
    }
}