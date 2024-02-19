package com.example.dishdive.areameals.view;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dishdive.R;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dishdive.R;
import com.example.dishdive.areameals.presenter.AreaMealsPresenter;
import com.example.dishdive.categorymeals.presenter.CategoryMealsPresenter;
import com.example.dishdive.categorymeals.view.CategoryMealsAdapter;
import com.example.dishdive.categorymeals.view.CategoryMealsFragmentArgs;
import com.example.dishdive.categorymeals.view.CategoryMealsFragmentDirections;
import com.example.dishdive.categorymeals.view.CategoryMealsView;
import com.example.dishdive.db.MealLocalDataSource;
import com.example.dishdive.model.MealRepository;
import com.example.dishdive.model.PopularMeal;
import com.example.dishdive.network.meal.MealRemoteDataSource;

import java.util.ArrayList;
import java.util.List;


public class AreaMealsFragment extends Fragment implements AreaMealView {
    String area;
    RecyclerView recyclerView;
    AreaMealsAdapter areaMealsAdapter;
    AreaMealsPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_area_meals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycleView);
        getInformationOfAreaFromTheArgs();

        Context applicationContext = requireContext().getApplicationContext();
        presenter = new AreaMealsPresenter(MealRepository.getInstance(MealLocalDataSource.getInstance(getContext()), MealRemoteDataSource.getInstance(applicationContext)) , this);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        areaMealsAdapter = new AreaMealsAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(areaMealsAdapter);

        presenter.fetchCategoryMeals(area);

        areaMealsAdapter.setOnItemClickListener(new AreaMealsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PopularMeal meal) {
                navigateToDetailsFragment(meal);
            }
        });
    }

    @Override
    public void showAreaMeals(List<PopularMeal> meals) {
        areaMealsAdapter.setAreaMeals(meals);
    }

    private void navigateToDetailsFragment(PopularMeal meal) {
        if (meal != null) {
            AreaMealsFragmentDirections.ActionAreaMealsFragmentToDetailsMealFragment action =
                    AreaMealsFragmentDirections.actionAreaMealsFragmentToDetailsMealFragment(
                            meal.getIdMeal(), meal.getStrMeal(), meal.getStrMealThumb());

            NavHostFragment.findNavController(this).navigate(action);
        }
    }

    private void getInformationOfAreaFromTheArgs() {
        AreaMealsFragmentArgs args = AreaMealsFragmentArgs.fromBundle(getArguments());
        area = args.getAreaName();
    }
}