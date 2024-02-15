
package com.example.dishdive.model;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IngredientResponse {
    @SerializedName("meals")
    private List<Ingredient> ingredients;

    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> value) { this.ingredients = value; }
}
