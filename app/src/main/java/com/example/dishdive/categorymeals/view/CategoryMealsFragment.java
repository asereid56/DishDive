package com.example.dishdive.categorymeals.view;

import android.content.Context;
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
import com.example.dishdive.categorymeals.presenter.CategoryMealsPresenter;
import com.example.dishdive.db.MealLocalDataSource;
import com.example.dishdive.model.MealRepository;
import com.example.dishdive.model.PopularMeal;
import com.example.dishdive.network.meal.MealRemoteDataSource;

import java.util.ArrayList;
import java.util.List;


public class CategoryMealsFragment extends Fragment implements CategoryMealsView {
    String category;
    RecyclerView recyclerView;
    CategoryMealsAdapter categoryMealsAdapter;
    CategoryMealsPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_meals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycleView);
        getInformationOfCategoryFromTheArgs();

        Context applicationContext = requireContext().getApplicationContext();
        presenter = new CategoryMealsPresenter(MealRepository.getInstance(MealLocalDataSource.getInstance(getContext()), MealRemoteDataSource.getInstance(applicationContext)), this);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        categoryMealsAdapter = new CategoryMealsAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(categoryMealsAdapter);

        presenter.fetchCategoryMeals(category);
        categoryMealsAdapter.setOnItemClickListener(new CategoryMealsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PopularMeal meal) {
                navigateToDetailsFragment(meal);
            }
        });
    }

    @Override
    public void showCategoriesMeals(List<PopularMeal> meals) {
        categoryMealsAdapter.setCategoryMeals(meals);
    }

    private void navigateToDetailsFragment(PopularMeal meal) {
        if (meal != null) {
            CategoryMealsFragmentDirections.ActionCategoryMealsFragmentToDetailsMealFragment action =
                    CategoryMealsFragmentDirections.actionCategoryMealsFragmentToDetailsMealFragment(
                            meal.getIdMeal(), meal.getStrMeal(), meal.getStrMealThumb());

            NavHostFragment.findNavController(this).navigate(action);
        }
    }

    private void getInformationOfCategoryFromTheArgs() {
        CategoryMealsFragmentArgs args = CategoryMealsFragmentArgs.fromBundle(getArguments());
        category = args.getCategoryName();
    }
}