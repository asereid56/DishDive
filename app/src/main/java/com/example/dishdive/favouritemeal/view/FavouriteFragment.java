package com.example.dishdive.favouritemeal.view;
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
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dishdive.R;
import com.example.dishdive.db.MealLocalDataSource;
import com.example.dishdive.favouritemeal.presenter.FavouritePresenter;
import com.example.dishdive.home.view.HomeFragmentDirections;
import com.example.dishdive.home.view.MostPopularAdapter;
import com.example.dishdive.login.view.LoginScreen;
import com.example.dishdive.model.Meal;
import com.example.dishdive.model.MealRepository;
import com.example.dishdive.model.PopularMeal;
import com.example.dishdive.network.meal.MealRemoteDataSource;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment implements FavouriteView, OnFavClickListener {
    private FavouritePresenter favouritePresenter;
    private RecyclerView recyclerView;
    private LiveData<List<Meal>> favMeal;
    private FavouriteAdapter favouriteAdapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = getActivity().getSharedPreferences("AuthState", Context.MODE_PRIVATE);
        if (preferences.getBoolean("isLoggedIn", false)) {
            // User is logged in, show favorite meals
            setupFavouriteFragment();
        } else {
            // User is not logged in, show custom dialog
            showLoginDialog();
        }
//        favouriteAdapter.OnItemClickListenerToDetails(new FavouriteAdapter.OnItemClickListenerToDetails() {
//            @Override
//            public void onItemClick(Meal meal) {
//                navigateToDetailsFragment(meal);
//            }
//        });
    }

    private void setupFavouriteFragment() {
        favouritePresenter = new FavouritePresenter(this, MealRepository.getInstance(MealLocalDataSource.getInstance(getContext()), MealRemoteDataSource.getInstance()));
        recyclerView = getView().findViewById(R.id.recycleView);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        favouriteAdapter = new FavouriteAdapter(getContext(), new ArrayList<>(), this);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(favouriteAdapter);

        try {
            favMeal = favouritePresenter.showFavMeals();
            favMeal.observe(getViewLifecycleOwner(), new Observer<List<Meal>>() {
                @Override
                public void onChanged(List<Meal> meals) {
//                    favouriteAdapter.setMeals(meals);
//                    favouriteAdapter.notifyDataSetChanged();
                    showFavMeal(meals);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed to Load Meals", Toast.LENGTH_SHORT).show();
        }
        favouriteAdapter.OnItemClickListenerToDetails(new FavouriteAdapter.OnItemClickListenerToDetails() {
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
    public void showFavMeal(List<Meal> meal) {
        favouriteAdapter.setMeals(meal);
        favouriteAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnMealsFavListener(Meal meal) {
        Toast.makeText(getContext(), "Removed", Toast.LENGTH_SHORT).show();
        favouritePresenter.deleteFromFav(meal);
    }
    private void navigateToDetailsFragment(Meal meal) {
        FavouriteFragmentDirections.ActionFavouriteFragmentToDetailsMealFragment action =
                FavouriteFragmentDirections.actionFavouriteFragmentToDetailsMealFragment(
                        meal.getIdMeal(), meal.getStrMeal(), meal.getStrMealThumb());

        NavHostFragment.findNavController(this).navigate(action);
    }
}
