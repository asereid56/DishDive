package com.example.dishdive.home.view;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.airbnb.lottie.LottieAnimationView;
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

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        popularAdapter = new MostPopularAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(popularAdapter);

        if (!isInternetConnected()) {
            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToSearchFragment("");
                }
            });
            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

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

    private boolean isInternetConnected() {
        ConnectivityManager cm = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }

}
