package com.example.dishdive.ingredientmeals.view;

import android.os.Bundle;

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
import com.example.dishdive.categorymeals.view.CategoryMealsAdapter;
import com.example.dishdive.categorymeals.view.CategoryMealsFragmentDirections;
import com.example.dishdive.db.MealLocalDataSource;
import com.example.dishdive.ingredientmeals.presenter.IngredientMealsPresenter;
import com.example.dishdive.model.MealRepository;
import com.example.dishdive.model.PopularMeal;
import com.example.dishdive.network.meal.MealRemoteDataSource;

import java.util.ArrayList;
import java.util.List;

public class IngredientsMealsFragment extends Fragment implements IngredientMealsView {
    IngredientsMealsAdapter ingredientsMealsAdapter;
    IngredientMealsPresenter presenter;
    String ingredientName;
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ingredients_meals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycleView);
        getArgsFromSearchFragment();

        presenter = new IngredientMealsPresenter(MealRepository.getInstance(MealLocalDataSource.getInstance(getContext()), MealRemoteDataSource.getInstance()), this);
        ingredientsMealsAdapter = new IngredientsMealsAdapter(getContext(), new ArrayList<>());

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(ingredientsMealsAdapter);
        presenter.fetchIngredientMeals(ingredientName);

        ingredientsMealsAdapter.setOnItemClickListener(new CategoryMealsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PopularMeal meal) {
                navigateToDetailsFragment(meal);
            }
        });


    }

    private void getArgsFromSearchFragment() {
        IngredientsMealsFragmentArgs args = IngredientsMealsFragmentArgs.fromBundle(getArguments());
        ingredientName = args.getIngredientName();
    }

    private void navigateToDetailsFragment(PopularMeal meal) {
        if (meal != null) {
            IngredientsMealsFragmentDirections.ActionIngredientsMealsFragmentToDetailsMealFragment action =
                    IngredientsMealsFragmentDirections.actionIngredientsMealsFragmentToDetailsMealFragment(
                            meal.getIdMeal(), meal.getStrMeal(), meal.getStrMealThumb());

            NavHostFragment.findNavController(this).navigate(action);
        }
    }

    @Override
    public void showIngredientMeals(List<PopularMeal> meals) {
        ingredientsMealsAdapter.setIngredientMeals(meals);
    }
}