package com.example.dishdive.network.meal;

import android.content.Context;
import android.util.Log;

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

import java.io.File;
import java.util.List;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.logging.HttpLoggingInterceptor;


public class MealRemoteDataSource {
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static final long CACHE_SIZE = 10 * 1024 * 1024; // 10 MB cache size
    private static MealRemoteDataSource instance;
    private final MealsServices mealsServices;

    private MealRemoteDataSource(Context context) {
        File httpCacheDirectory = new File(context.getCacheDir(), "http-cache");
        Cache cache = new Cache(httpCacheDirectory, CACHE_SIZE);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        mealsServices = retrofit.create(MealsServices.class);
    }

    public static synchronized MealRemoteDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new MealRemoteDataSource(context);
        }
        return instance;
    }

    public void makeNetworkCallForDailyMail(NetworkCallBackRandomMeal networkCallBack) {
        Single<MealResponse> observable = mealsServices.getRandomMealForADay();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (dailyMeal) -> {
                            networkCallBack.dealyMealOnSuccess(dailyMeal.getMeals().get(0));
                        },
                        e -> {
                            networkCallBack.onFailure("Check your Connection");
                        }
                );

    }

    public void makeNetworkCallForPopularMeal(NetworkCallBackPopularMeal networkCallBack) {
        Single<PopularMealResponse> observable = mealsServices.getPopularMeal("Seafood");

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((popularMeals) -> {
                            networkCallBack.popularMealOnSuccess(popularMeals.getMeals());
                        },
                        e -> {
                            networkCallBack.onFailure("Check your Connection");
                        });
    }


    public void makeNetworkCallForGetDetailsOfMeal(String mealID, NetworkCallBackDetailsOfMeal networkCallBack) {
        Single<MealResponse> observable = mealsServices.getMealDetails(mealID);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (mealDetails) -> {
                            networkCallBack.getMealDetailsOnSuccess(mealDetails.getMeals().get(0));
                        },
                        e -> {
                            networkCallBack.onFailure("Check your Connection");
                        });
    }

    public void makeNetworkCallForCategories(NetworkCallBackCategories networkCallBack) {
        Single<CategoryResponse> observable = mealsServices.getCategories();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(categoryResponse -> {
                            networkCallBack.getCategoriesOnSuccess(categoryResponse.getCategories());
                        },
                        e -> {
                            networkCallBack.onFailure("Check your Connection");
                        });
    }

    public void makeNetworkCallForAreaMeals(String areaName, NetworkCallbackAreaMeals networkCallbackAreaMeals) {
        Single<PopularMealResponse> observable = mealsServices.getAreaMeals(areaName);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(popularMealResponse -> {
                            List<PopularMeal> popularMeals = popularMealResponse.getMeals();
                            if (popularMeals != null && !popularMeals.isEmpty()) {
                                networkCallbackAreaMeals.categoryMealsOnSuccess(popularMeals);
                            }
                        },
                        e -> networkCallbackAreaMeals.onFailure("Check your Connection"));
    }



    public void makeNetworkCallForIngredientsMeals(String ingredientName, NetworkCallBackIngredientsMeals networkCallBack) {
        Single<PopularMealResponse> observable = mealsServices.getIngredientsMeals(ingredientName);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(popularMealResponse -> {
                            List<PopularMeal> popularMeals = popularMealResponse.getMeals();
                            if (popularMeals != null && !popularMeals.isEmpty()) {
                                networkCallBack.ingredientsMealsOnSuccess(popularMeals);
                            }
                        },
                        throwable -> networkCallBack.onFailure("Check your Connection"));
    }


    public void makeNetworkCallForSearchByName(String nameOfMeal, NetworkCallBackSearchByName networkCallBack) {
        Single<MealResponse> observable = mealsServices.getMealsByName(nameOfMeal);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mealResponse -> {
                            List<Meal> meals = mealResponse.getMeals();
                            if (meals != null) {
                                networkCallBack.mealOnSuccess(meals);
                            } else {
                                networkCallBack.mealOnSuccess(null);
                            }
                        },
                        e -> networkCallBack.onFailure("Check your Connection"));
    }


    public void makeNetworkCallForCategoryMeals(String categoryName, NetworkCallBackCategoryMeals networkCallBack) {
        Single<PopularMealResponse> observable = mealsServices.getCategoryMeals(categoryName);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(popularMealResponse -> {
                            List<PopularMeal> popularMeals = popularMealResponse.getMeals();
                            if (popularMeals != null && !popularMeals.isEmpty()) {
                                networkCallBack.categoryMealsOnSuccess(popularMeals);
                            }
                        },
                        e -> networkCallBack.onFailure("Check your Connection"));
    }

    public void makeNetworkCallForIngredients(NetworkCallBackIngredients networkCallBackIngredients) {
        Single<IngredientResponse> observable = mealsServices.getIngredients();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ingredientResponse -> {
                            List<Ingredient> ingredients = ingredientResponse.getIngredients();
                            if (ingredients != null) {
                                networkCallBackIngredients.getAllIngredientsOnSuccess(ingredients);
                            }
                        },
                        e -> networkCallBackIngredients.onFailure("Check your Connection"));
    }

    public void makeNetworkCallForCountries(NetworkCallBackCountries networkCallBack) {
        Single<CountryResponse> observable = mealsServices.getCountries();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(countryResponse -> {
                            List<Country> countries = countryResponse.getAreaNames();
                            if (countries != null) {
                                networkCallBack.countryOnSuccess(countries);
                            }
                        },
                        e -> networkCallBack.onFailure("Check your Connection"));
    }


}