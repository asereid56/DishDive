package com.example.dishdive.network.meal;

import com.example.dishdive.model.Category;

import java.util.List;


public interface NetworkCallBackCategories {
    public void getCategoriesOnSuccess(List<Category> category);

    public void onFailure(String msg);
}
