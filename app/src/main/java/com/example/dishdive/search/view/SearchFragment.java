package com.example.dishdive.search.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListAdapter;

import com.example.dishdive.R;
import com.example.dishdive.db.MealLocalDataSource;
import com.example.dishdive.model.Category;
import com.example.dishdive.model.Country;
import com.example.dishdive.model.Ingredient;
import com.example.dishdive.model.Meal;
import com.example.dishdive.model.MealRepository;
import com.example.dishdive.network.meal.MealRemoteDataSource;
import com.example.dishdive.search.presenter.SearchPresenter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class SearchFragment extends Fragment implements SearchView {

    private RecyclerView recyclerView;
    EditText searchText;
    SearchPresenter searchPresenter;
    CategoryAdapter categoryAdapter;
    CountryAdapter countryAdapter;
    IngredientAdapter ingredientAdapter;
    SearchAdapter searchAdapter;
    ChipGroup chipGroup;
    private Disposable disposable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycleView);
        searchText = view.findViewById(R.id.searchText);

        searchPresenter = new SearchPresenter(MealRepository.getInstance(MealLocalDataSource.getInstance(getContext()), MealRemoteDataSource.getInstance()), this , getContext());

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        categoryAdapter = new CategoryAdapter(getContext(), new ArrayList<>());
        countryAdapter = new CountryAdapter(getContext(), new ArrayList<>());
        ingredientAdapter = new IngredientAdapter(getContext(), new ArrayList<>());
        searchAdapter = new SearchAdapter(getContext(), new ArrayList<>());

        chipGroup = view.findViewById(R.id.chipGroup);
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, int checkedId) {
                Chip chip = group.findViewById(checkedId);
                if (chip != null) {
                    String chipText = chip.getText().toString();
                    if (chipText.equals("Category")) {
                        categoryAdapter.notifyDataSetChanged();
                        categoryAdapter.setCategories(categoryAdapter.categories);
                        recyclerView.setAdapter(categoryAdapter);
                    } else if (chipText.equals("Country")) {
                        countryAdapter.notifyDataSetChanged();
                        countryAdapter.setCountries(countryAdapter.countries);
                        recyclerView.setAdapter(countryAdapter);
                    } else if (chipText.equals("Ingredient")) {
                        ingredientAdapter.notifyDataSetChanged();
                        ingredientAdapter.setIngredients(ingredientAdapter.ingredients);
                        recyclerView.setAdapter(ingredientAdapter);
                    }
                    searchPresenter.onChipSelected(chipText);
                }
            }
        });
        setupEditSearch();

        categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Category category) {
                navigateToCategoryMealsFragment(category);
            }
        });
        countryAdapter.setOnItemClickListener(new CountryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Country country) {
                navigateToAreaMealsFragment(country);
            }
        });
        ingredientAdapter.setOnItemClickListener(new IngredientAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Ingredient ingredient) {
                navigateToIngredientMealsFragment(ingredient);
            }
        });
        searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Meal meal) {
                navigateToDetailsMealFragment(meal);
            }
        });
    }

    private void setupEditSearch() {
        searchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    chipGroup.setVisibility(View.INVISIBLE);
                } else {
                    if (searchText.getText().toString().trim().isEmpty()) {
                        chipGroup.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        searchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (searchText.getText().toString().trim().isEmpty()) {
                        chipGroup.setVisibility(View.VISIBLE);
                    }
                }
                return false;
            }
        });


        disposable = Observable.create((ObservableOnSubscribe<String>) emitter -> {
                    searchText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            String mealName = s.toString().trim();
                            if(!mealName.isEmpty()){
                                emitter.onNext(mealName);
                            }else{
                                searchAdapter.setMeals(new ArrayList<>());
                                searchAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                })
                .subscribeOn(Schedulers.io())
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mealName ->  searchPresenter.searchMealByName(mealName));

    }


    @Override
    public void showMealsByName(List<Meal> meals) {
//        searchAdapter.setMeals(meals);
//        recyclerView.setAdapter(searchAdapter);
        if (searchText.getText().toString().trim().isEmpty()) {
            chipGroup.setVisibility(View.VISIBLE);
            recyclerView.swapAdapter(searchAdapter, true);
        } else {
            chipGroup.setVisibility(View.INVISIBLE);
            searchAdapter.setMeals(meals);
            recyclerView.setAdapter(searchAdapter);
        }
    }


    private void navigateToDetailsMealFragment(Meal meal) {
        if (meal != null) {
            SearchFragmentDirections.ActionSearchFragmentToDetailsMealFragment action =
                    SearchFragmentDirections.actionSearchFragmentToDetailsMealFragment(
                            meal.getIdMeal(), meal.getStrMeal(), meal.getStrMealThumb());

            NavHostFragment.findNavController(this).navigate(action);
        }
    }

    private void navigateToIngredientMealsFragment(Ingredient ingredient) {
        SearchFragmentDirections.ActionSearchFragmentToIngredientsMealsFragment action =
                SearchFragmentDirections.actionSearchFragmentToIngredientsMealsFragment(ingredient.getStrIngredient());
        NavHostFragment.findNavController(this).navigate(action);
    }

    @Override
    public void showCategories(List<Category> categories) {
        categoryAdapter.setCategories(categories);
    }

    @Override
    public void showCountries(List<Country> countries) {
        countryAdapter.setCountries(countries);
    }

    @Override
    public void showIngredients(List<Ingredient> ingredients) {
        ingredientAdapter.setIngredients(ingredients);
    }

    private void navigateToCategoryMealsFragment(Category category) {
        SearchFragmentDirections.ActionSearchFragmentToCategoryMealsFragment action =
                SearchFragmentDirections.actionSearchFragmentToCategoryMealsFragment(
                        category.getStrCategory());

        NavHostFragment.findNavController(this).navigate(action);
    }

    private void navigateToAreaMealsFragment(Country country) {
        SearchFragmentDirections.ActionSearchFragmentToAreaMealsFragment action =
                SearchFragmentDirections.actionSearchFragmentToAreaMealsFragment(
                        country.getStrArea());

        NavHostFragment.findNavController(this).navigate(action);
    }
}
