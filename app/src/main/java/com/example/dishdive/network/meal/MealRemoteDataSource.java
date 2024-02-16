package com.example.dishdive.network.meal;

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

import java.util.List;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
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


    private MealRemoteDataSource() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        OkHttpClient client = httpClient.build();

        retrofit = new Retrofit.Builder().client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create()).baseUrl(BASE_URL).build();
        mealsServices = retrofit.create(MealsServices.class);
    }

    public static synchronized MealRemoteDataSource getInstance() {
        if (insatance == null) {
            insatance = new MealRemoteDataSource();
        }
        return insatance;
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
                            networkCallBack.onFailure(e.toString());
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
                            networkCallBack.onFailure(e.toString());
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
                            networkCallBack.onFailure(e.toString());
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
                            networkCallBack.onFailure(e.toString());
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
                        e -> networkCallbackAreaMeals.onFailure("Failed to load Area Meals"));
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
                        throwable -> networkCallBack.onFailure("Failed to load Meals"));
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
                        e -> networkCallBack.onFailure("Failed to Load Meals"));
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
                        e -> networkCallBack.onFailure("Failed to load Category Meals"));
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
                        e -> networkCallBackIngredients.onFailure("Failed to fetch Ingredients"));
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
                        e -> networkCallBack.onFailure("Failed to fetch countries"));
    }


}