package com.example.dishdive.network.meal;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.dishdive.model.Category;
import com.example.dishdive.model.CategoryResponse;
import com.example.dishdive.model.Country;
import com.example.dishdive.model.CountryResponse;

import com.example.dishdive.model.Ingredient;
import com.example.dishdive.model.IngredientResponse;
import com.example.dishdive.model.Meal;
import com.example.dishdive.model.MealResponse;
import com.example.dishdive.model.PopularMeal;
import com.example.dishdive.model.PopularMealResponse;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.logging.HttpLoggingInterceptor;


public class MealRemoteDataSource {
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static Retrofit retrofit = null;
    private static MealRemoteDataSource insatance = null;
    MealsServices mealsServices;
    MutableLiveData<Meal> mealDetailsLiveData;


    private MealRemoteDataSource() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        OkHttpClient client = httpClient.build();

        retrofit = new Retrofit.Builder().client(client).addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build();
        mealsServices = retrofit.create(MealsServices.class);
    }

    public static synchronized MealRemoteDataSource getInstance() {
        if (insatance == null) {
            insatance = new MealRemoteDataSource();
        }
        return insatance;
    }

    public void makeNetworkCallForDailyMail(NetworkCallBackRandomMeal networkCallBack) {
        Call<MealResponse> call = mealsServices.getRandomMealForADay();
        call.enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Meal> meals = response.body().getMeals();
                    if (meals != null && !meals.isEmpty()) {
                        Meal meal = meals.get(0);
                        networkCallBack.dealyMealOnSuccess(meal);
                    } else {
                        networkCallBack.onFailure("No meals found");
                    }
                } else {
                    networkCallBack.onFailure("Failed to fetch meals");
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                networkCallBack.onFailure("failed to load meal");
            }
        });
    }

    public void makeNetworkCallForPopularMeal(NetworkCallBackPopularMeal networkCallBack) {
        Call<PopularMealResponse> call = mealsServices.getPopularMeal("Seafood");
        call.enqueue(new Callback<PopularMealResponse>() {
            @Override
            public void onResponse(Call<PopularMealResponse> call, Response<PopularMealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PopularMeal> popularMeals = response.body().getMeals();
                    if (popularMeals != null && !popularMeals.isEmpty()) {
                        networkCallBack.popularMealOnSuccess(popularMeals);
                    }
                }
            }

            @Override
            public void onFailure(Call<PopularMealResponse> call, Throwable t) {
                networkCallBack.onFailure("failed to load meals");
            }
        });
    }

    public void makeNetworkCallForGetDetailsOfMeal(String mealID, NetworkCallBackDetailsOfMeal networkCallBack) {
        Call<MealResponse> call = mealsServices.getMealDetails(mealID);
        call.enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i("Meal", "onResponse: " + response.body().getMeals());
                    Meal meal = response.body().getMeals().get(0);
                    networkCallBack.getMealDetailsOnSuccess(meal);
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                networkCallBack.onFailure("failed to load meal");
            }
        });
    }

    public void makeNetworkCallForCategories(NetworkCallBackCategories networkCallBack) {
        Call<CategoryResponse> call = mealsServices.getCategories();
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body().getCategories();
                    networkCallBack.getCategoriesOnSuccess(categories);
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                networkCallBack.onFailure("failed to load meals");
            }
        });
    }

    public void makeNetworkCallForAreaMeals(String areaName, NetworkCallbackAreaMeals networkCallbackAreaMeals) {
        Call<PopularMealResponse> call = mealsServices.getAreaMeals(areaName);
        call.enqueue(new Callback<PopularMealResponse>() {
            @Override
            public void onResponse(Call<PopularMealResponse> call, Response<PopularMealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PopularMeal> popularMeals = response.body().getMeals();
                    if (popularMeals != null && !popularMeals.isEmpty()) {
                        networkCallbackAreaMeals.categoryMealsOnSuccess(popularMeals);
                    }
                }
            }

            @Override
            public void onFailure(Call<PopularMealResponse> call, Throwable t) {
                networkCallbackAreaMeals.onFailure("Failed to load Meals");
            }
        });
    }

    public void makeNetworkCallForIngredientsMeals(String ingredientName, NetworkCallBackIngredientsMeals networkCallBack) {
        Call<PopularMealResponse> call = mealsServices.getIngredientsMeals(ingredientName);
        call.enqueue(new Callback<PopularMealResponse>() {
            @Override
            public void onResponse(Call<PopularMealResponse> call, Response<PopularMealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PopularMeal> popularMeals = response.body().getMeals();
                    if (popularMeals != null && !popularMeals.isEmpty()) {
                        networkCallBack.ingredientsMealsOnSuccess(popularMeals);
                    }
                }
            }

            @Override
            public void onFailure(Call<PopularMealResponse> call, Throwable t) {
                networkCallBack.onFailure("Failed to load Meals");
            }
        });
    }

    public void makeNetworkCallForSearchByName(String nameOfMeal, NetworkCallBackSearchByName networkCallBack) {
        Call<MealResponse> call = mealsServices.getMealsByName(nameOfMeal);
        call.enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Meal> meals = response.body().getMeals();
                    if (meals != null && !meals.isEmpty()) {
                        networkCallBack.mealOnSuccess(meals);
                    }
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                networkCallBack.onFailure("Failed to Load Meals");
            }
        });
    }

    public void makeNetworkCallForCategoryMeals(String categoryName, NetworkCallBackCategoryMeals networkCallBack) {
        Call<PopularMealResponse> call = mealsServices.getCategoryMeals(categoryName);
        call.enqueue(new Callback<PopularMealResponse>() {
            @Override
            public void onResponse(Call<PopularMealResponse> call, Response<PopularMealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PopularMeal> popularMeals = response.body().getMeals();
                    if (popularMeals != null && !popularMeals.isEmpty()) {
                        networkCallBack.categoryMealsOnSuccess(popularMeals);
                    }
                }
            }

            @Override
            public void onFailure(Call<PopularMealResponse> call, Throwable t) {
                networkCallBack.onFailure("Failed to load Meals");
            }
        });
    }

    public void makeNetworkCallForIngredients(NetworkCallBackIngredients networkCallBackIngredients) {
        Call<IngredientResponse> call = mealsServices.getIngredients();
        call.enqueue(new Callback<IngredientResponse>() {
            @Override
            public void onResponse(Call<IngredientResponse> call, Response<IngredientResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Ingredient> ingredients = response.body().getIngredients();
                    Log.i("TAG", "onResponse: " + ingredients);
                    networkCallBackIngredients.getAllIngredientsOnSuccess(ingredients);
                }
            }

            @Override
            public void onFailure(Call<IngredientResponse> call, Throwable t) {
                networkCallBackIngredients.onFailure("Failed to fetch Ingredients");
            }
        });
    }

    public void makeNetworkCallForCountries(NetworkCallBackCountries networkCallBack) {
        Call<CountryResponse> call = mealsServices.getCountries();
        call.enqueue(new Callback<CountryResponse>() {
            @Override
            public void onResponse(Call<CountryResponse> call, Response<CountryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Country> countries = response.body().getAreaNames();
                    Log.i("Aser", "onResponse: " + countries);
                    networkCallBack.countryOnSuccess(countries); // Pass the countries list to the callback
                } else {
                    networkCallBack.onFailure("Failed to fetch countries");
                }
            }

            @Override
            public void onFailure(Call<CountryResponse> call, Throwable t) {
                networkCallBack.onFailure("Failed to fetch countries");
            }
        });
    }


}