package com.example.dishdive.network.meal;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

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
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;



public class MealRemoteDataSource {
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static Retrofit retrofit = null;
    private static MealRemoteDataSource insatance = null;
    RandomMealService randomMealService;
    MutableLiveData<Meal> mealDetailsLiveData;


    private MealRemoteDataSource() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        OkHttpClient client = httpClient.build();

        retrofit = new Retrofit.Builder().client(client).addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build();
        randomMealService = retrofit.create(RandomMealService.class);
    }

    public static synchronized MealRemoteDataSource getInstance() {
        if (insatance == null) {
            insatance = new MealRemoteDataSource();
        }
        return insatance;
    }

    public void makeNetworkCallForDailyMail(NetworkCallBackRandomMeal networkCallBack) {
        Call<MealResponse> call = randomMealService.getRandomMealForADay();
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
        Call<PopularMealResponse> call = randomMealService.getPopularMeal("Seafood");
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

    public void makeNetworkCallForGetDetailsOfMeal(String mealID,NetworkCallBackDetailsOfMeal networkCallBack) {
        Call<MealResponse> call = randomMealService.getMealDetails(mealID);
        call.enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i("Meal", "onResponse: " + response.body().getMeals());
                    Meal meal = response.body().getMeals().get(0);
                    networkCallBack.getMealDetailsOnSuccess(meal);
                } else {
                    return;
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                networkCallBack.onFailure("failed to load meal");
            }
        });
    }
}

