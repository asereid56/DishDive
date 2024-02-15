package com.example.dishdive.network.meal;

import com.example.dishdive.model.Country;
import com.example.dishdive.model.Meal;

import java.util.List;

public interface NetworkCallBackCountries {
    public void countryOnSuccess(List<Country> countries);

    public void onFailure(String msg);
}

