package com.example.dishdive.home.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.dishdive.R;
import com.example.dishdive.db.MealLocalDataSource;
import com.example.dishdive.home.presenter.HomePresenter;
import com.example.dishdive.model.Meal;
import com.example.dishdive.model.MealRepository;
import com.example.dishdive.model.PopularMeal;
import com.example.dishdive.network.meal.MealRemoteDataSource;


import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeView {
    ImageView randomDailyImage;
    Meal randomMeal;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    MostPopularAdapter popularAdapter;
    HomePresenter presenter;
    ImageView btnSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context applicationContext = requireContext().getApplicationContext();
        presenter = new HomePresenter(this, MealRepository.getInstance(MealLocalDataSource.getInstance(getContext()), MealRemoteDataSource.getInstance(applicationContext)));
        randomDailyImage = view.findViewById(R.id.dailyMeal);
        recyclerView = view.findViewById(R.id.popularRecycleView);
        btnSearch = view.findViewById(R.id.btnSearch);

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        popularAdapter = new MostPopularAdapter(getContext(), new ArrayList<>());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(popularAdapter);

        presenter.getRandomDailyMeal();
        presenter.getMealLiveData().observe(getViewLifecycleOwner(), new Observer<Meal>() {
            @Override
            public void onChanged(Meal meal) {
                if (meal != null) {
                    displayMealImage(meal.getStrMealThumb(), meal);
                }
            }
        });

        randomDailyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRandomMealCLick(v);
            }
        });

        presenter.getPopularMeals();
        presenter.getPopularMealLiveData().observe(getViewLifecycleOwner(), new Observer<PopularMeal>() {
            @Override
            public void onChanged(PopularMeal popularMeal) {
                if (popularMeal != null) {
                    showPopularImage((List<PopularMeal>) popularMeal);
                }
            }
        });
        popularAdapter.setOnItemClickListener(new MostPopularAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PopularMeal meal) {
                if (meal != null) {
                    navigateToDetailsFragment(meal);
                }
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSearchFragment("");
            }
        });

    }

    @Override
    public void displayMealImage(String imageUrl, Meal meal) {
        loadImage(getContext(), imageUrl);
        randomMeal = meal;
    }

    @Override
    public void showPopularImage(List<PopularMeal> meals) {
        popularAdapter.setPopularMeals(meals);
    }


    public void loadImage(Context context, String url) {
        Glide.with(context).load(url).into(randomDailyImage);
    }

    public void onRandomMealCLick(View v) {
        if (randomMeal != null) {
            HomeFragmentDirections.ActionHomeFragmentToDetailsMealFragment action =
                    HomeFragmentDirections.actionHomeFragmentToDetailsMealFragment(randomMeal.getIdMeal(),
                            randomMeal.getStrMeal(),
                            randomMeal.getStrMealThumb());

            NavHostFragment.findNavController(this).navigate(action);
        } else {
            Log.d("HomeFragment", "onRandomMealCLick: meals not found ");
        }
    }

    private void navigateToDetailsFragment(PopularMeal meal) {
        HomeFragmentDirections.ActionHomeFragmentToDetailsMealFragment action =
                HomeFragmentDirections.actionHomeFragmentToDetailsMealFragment(
                        meal.getIdMeal(), meal.getStrMeal(), meal.getStrMealThumb());

        NavHostFragment.findNavController(this).navigate(action);
    }

    private void navigateToSearchFragment(String name) {
        HomeFragmentDirections.ActionHomeFragmentToSearchFragment action =
                HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                        name);

        NavHostFragment.findNavController(this).navigate(action);
    }
}
