package com.example.dishdive.model;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.dishdive.db.MealLocalDataSource;
import com.example.dishdive.network.meal.MealRemoteDataSource;
import com.example.dishdive.network.meal.NetworkCallBackCategories;
import com.example.dishdive.network.meal.NetworkCallBackCategoryMeals;
import com.example.dishdive.network.meal.NetworkCallBackCountries;
import com.example.dishdive.network.meal.NetworkCallBackDetailsOfMeal;
import com.example.dishdive.network.meal.NetworkCallBackIngredients;
import com.example.dishdive.network.meal.NetworkCallBackIngredientsMeals;
import com.example.dishdive.network.meal.NetworkCallBackPopularMeal;
import com.example.dishdive.network.meal.NetworkCallBackRandomMeal;
import com.example.dishdive.network.meal.NetworkCallBackSearchByName;
import com.example.dishdive.network.meal.NetworkCallbackAreaMeals;

import java.util.List;

public class MealRepository {
    MealRemoteDataSource mealRemoteDataSource;
    MealLocalDataSource mealLocalDataSource;
    private static MealRepository mealRepository = null;

    public static MealRepository getInstance(MealLocalDataSource mealLocalDataSource, MealRemoteDataSource mealRemoteDataSource) {
        if (mealRepository == null) {
            mealRepository = new MealRepository(mealLocalDataSource, mealRemoteDataSource);
        }
        return mealRepository;
    }

    private MealRepository(MealLocalDataSource mealLocalDataSource, MealRemoteDataSource mealRemoteDataSource) {
        this.mealRemoteDataSource = mealRemoteDataSource;
        this.mealLocalDataSource = mealLocalDataSource;
    }

    public void getRandomMeal(NetworkCallBackRandomMeal networkCallBackRandomMeal) {
        mealRemoteDataSource.makeNetworkCallForDailyMail(new NetworkCallBackRandomMeal() {
            @Override
            public void dealyMealOnSuccess(Meal meal) {
                networkCallBackRandomMeal.dealyMealOnSuccess(meal);
            }

            @Override
            public void onFailure(String msg) {
                networkCallBackRandomMeal.onFailure(msg);
            }

        });

    }

    public void getAllIngredients(NetworkCallBackIngredients networkCallBackIngredients) {
        mealRemoteDataSource.makeNetworkCallForIngredients(new NetworkCallBackIngredients() {
            @Override
            public void getAllIngredientsOnSuccess(List<Ingredient> ingredients) {
                networkCallBackIngredients.getAllIngredientsOnSuccess(ingredients);
            }

            @Override
            public void onFailure(String msg) {
                networkCallBackIngredients.onFailure(msg);
            }
        });
    }

    public void getCategories(NetworkCallBackCategories networkCallBackCategories) {
        mealRemoteDataSource.makeNetworkCallForCategories(new NetworkCallBackCategories() {
            @Override
            public void getCategoriesOnSuccess(List<Category> category) {
                networkCallBackCategories.getCategoriesOnSuccess(category);
            }

            @Override
            public void onFailure(String msg) {
                networkCallBackCategories.onFailure(msg);
            }
        });
    }

    public void getMealsOfCategory(String categoryName, NetworkCallBackCategoryMeals networkCallBackCategoryMeals) {
        mealRemoteDataSource.makeNetworkCallForCategoryMeals(categoryName, new NetworkCallBackCategoryMeals() {

            @Override
            public void categoryMealsOnSuccess(List<PopularMeal> meal) {
                networkCallBackCategoryMeals.categoryMealsOnSuccess(meal);
            }

            @Override
            public void onFailure(String msg) {
                networkCallBackCategoryMeals.onFailure(msg);
            }
        });
    }

    public void getMealsOfIngredients(String ingredientName, NetworkCallBackIngredientsMeals networkCallBack) {
        mealRemoteDataSource.makeNetworkCallForIngredientsMeals(ingredientName, new NetworkCallBackIngredientsMeals() {
            @Override
            public void ingredientsMealsOnSuccess(List<PopularMeal> meal) {
                networkCallBack.ingredientsMealsOnSuccess(meal);
            }

            @Override
            public void onFailure(String msg) {
                networkCallBack.onFailure(msg);
            }
        });
    }

    public void getMealsByName(String nameOfMeal, NetworkCallBackSearchByName networkCallBack) {
        mealRemoteDataSource.makeNetworkCallForSearchByName(nameOfMeal, new NetworkCallBackSearchByName() {
            @Override
            public void mealOnSuccess(List<Meal> meals) {
                networkCallBack.mealOnSuccess(meals);
            }

            @Override
            public void onFailure(String msg) {
                networkCallBack.onFailure(msg);
            }
        });
    }


    public void getMealsOfArea(String areaName, NetworkCallbackAreaMeals networkCallbackAreaMeals) {
        mealRemoteDataSource.makeNetworkCallForAreaMeals(areaName, new NetworkCallbackAreaMeals() {
            @Override
            public void categoryMealsOnSuccess(List<PopularMeal> meal) {
                networkCallbackAreaMeals.categoryMealsOnSuccess(meal);
            }

            @Override
            public void onFailure(String msg) {
                networkCallbackAreaMeals.onFailure(msg);
            }
        });
    }

    public void getCountries(NetworkCallBackCountries networkCallBackCountries) {
        mealRemoteDataSource.makeNetworkCallForCountries(new NetworkCallBackCountries() {
            @Override
            public void countryOnSuccess(List<Country> countries) {
                networkCallBackCountries.countryOnSuccess(countries);
                Log.i("Aser", "countryOnSuccess: " + countries);
            }

            @Override
            public void onFailure(String msg) {
                networkCallBackCountries.onFailure(msg);
            }
        });
    }

    public void getPopularMeals(NetworkCallBackPopularMeal networkCallBackPopularMeal) {
        mealRemoteDataSource.makeNetworkCallForPopularMeal(new NetworkCallBackPopularMeal() {
            @Override
            public void popularMealOnSuccess(List<PopularMeal> meal) {
                if (meal != null) {
                    networkCallBackPopularMeal.popularMealOnSuccess(meal);
                }
            }

            @Override
            public void onFailure(String msg) {
                networkCallBackPopularMeal.onFailure(msg);
            }
        });
    }

    public void getDetailsOfMeal(String mealID, NetworkCallBackDetailsOfMeal networkCallBackDetailsOfMeal) {
        mealRemoteDataSource.makeNetworkCallForGetDetailsOfMeal(mealID, new NetworkCallBackDetailsOfMeal() {
            @Override
            public void getMealDetailsOnSuccess(Meal meal) {
                if (meal != null) {
                    networkCallBackDetailsOfMeal.getMealDetailsOnSuccess(meal);
                }
            }

            @Override
            public void onFailure(String msg) {
                networkCallBackDetailsOfMeal.onFailure(msg);
            }
        });
    }

    public void insertMealToFav(Meal meal) {
        mealLocalDataSource.insertToFav(meal);
    }

    public void deleteMealFromFav(Meal meal) {
        mealLocalDataSource.deleteFromFav(meal);
    }

    public LiveData<List<Meal>> getStoredFavMeal(String email) {
        return mealLocalDataSource.getListLiveFavMeals(email);
    }


    public void insertMealToPlan(Meal meal) {
        Log.i("TAG", "onClick: entered to the repoo to add ");
        mealLocalDataSource.insertToPlan(meal);
    }

    public void deleteMealFromPlan(Meal meal) {
        mealLocalDataSource.deleteFromPlan(meal);
    }

    public LiveData<List<Meal>> getStoredPlannedMeals(String email) {
        return mealLocalDataSource.getListLivePlanMeals(email);
    }
}
