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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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


public class PlanFragment extends Fragment implements PlanView,OnPlanClickListener{
    PlanPresenter planPresenter;
    private RecyclerView recyclerView;
    private LiveData<List<Meal>> planMeals;
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
        planPresenter = new PlanPresenter(this, MealRepository.getInstance(MealLocalDataSource.getInstance(getContext()), MealRemoteDataSource.getInstance()));
        recyclerView = getView().findViewById(R.id.recycleView);

        // Initialize LinearLayoutManager with vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        planAdapter = new PlanAdapter(getContext(), new ArrayList<>(), this);

        // Set the layout manager to RecyclerView
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(planAdapter);

        try {
            planMeals = planPresenter.getPlanLiveData();
            planMeals.observe(getViewLifecycleOwner(), new Observer<List<Meal>>() {
                @Override
                public void onChanged(List<Meal> meals) {
                    showPlanMeal(meals);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed to Load Meals", Toast.LENGTH_SHORT).show();
        }
        planAdapter.setOnItemClickListener(new PlanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Meal meal) {
                navigateToDetailsFragment(meal);
            }
        });
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
        DetailsMealFragment.isDayAdded = false;
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