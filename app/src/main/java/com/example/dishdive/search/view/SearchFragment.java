package com.example.dishdive.search.view;

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
import com.example.dishdive.db.MealLocalDataSource;
import com.example.dishdive.model.Category;
import com.example.dishdive.model.Country;
import com.example.dishdive.model.Ingredient;
import com.example.dishdive.model.MealRepository;
import com.example.dishdive.network.meal.MealRemoteDataSource;
import com.example.dishdive.search.presenter.SearchPresenter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements SearchView {

    private RecyclerView recyclerView;
    SearchPresenter searchPresenter;
    CategoryAdapter categoryAdapter;
    CountryAdapter countryAdapter;
    IngredientAdapter ingredientAdapter;
    ChipGroup chipGroup;

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

        searchPresenter = new SearchPresenter(MealRepository.getInstance(MealLocalDataSource.getInstance(getContext()), MealRemoteDataSource.getInstance()), this);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        categoryAdapter = new CategoryAdapter(getContext(), new ArrayList<>());
        countryAdapter = new CountryAdapter(getContext(), new ArrayList<>());
        ingredientAdapter = new IngredientAdapter(getContext(), new ArrayList<>());
        // recyclerView.setAdapter(categoryAdapter);

        chipGroup = view.findViewById(R.id.chipGroup);
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, int checkedId) {
                Chip chip = group.findViewById(checkedId);
                if (chip != null) {
                    String chipText = chip.getText().toString();
                    if (chipText.equals("Category")) {
                        recyclerView.setAdapter(categoryAdapter);
                    } else if (chipText.equals("Country")) {
                        recyclerView.setAdapter(countryAdapter);
                    } else if (chipText.equals("Ingredient")) {
                        recyclerView.setAdapter(ingredientAdapter);
                    }
                    searchPresenter.onChipSelected(chipText);
                }
            }
        });
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
