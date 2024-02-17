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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavouriteFragment extends Fragment implements FavouriteView, OnFavClickListener {
    private FavouritePresenter favouritePresenter;
    private RecyclerView recyclerView;
    private Flowable<List<Meal>> favMeal;
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
            setupFavouriteFragment();
        } else {
            showLoginDialog();
        }
    }

    private void setupFavouriteFragment() {
        Context applicationContext = requireContext().getApplicationContext();
        favouritePresenter = new FavouritePresenter(this, MealRepository.getInstance(MealLocalDataSource.getInstance(getContext()), MealRemoteDataSource.getInstance(applicationContext)));
        recyclerView = getView().findViewById(R.id.recycleView);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        favouriteAdapter = new FavouriteAdapter(getContext(), new ArrayList<>(), this);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(favouriteAdapter);

        favouritePresenter.showFavMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::updateFavProduct,
                        e -> {
                            Toast.makeText(getContext(), "Nothing to Show", Toast.LENGTH_SHORT).show();
                        }
                );
        favouriteAdapter.OnItemClickListenerToDetails(new FavouriteAdapter.OnItemClickListenerToDetails() {
            @Override
            public void onItemClick(Meal meal) {
                if (meal != null) {
                    navigateToDetailsFragment(meal);
                }
            }
        });
    }

    public void updateFavProduct(List<Meal> meals) {
        favouriteAdapter.setMeals(meals);
        favouriteAdapter.notifyDataSetChanged();
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
