package com.example.risotto.data.db.converter;

import androidx.room.TypeConverter;

import com.example.risotto.data.model.Ingredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class IngredientListConverter {

    @TypeConverter
    public String fromIngredientList(List<Ingredient> ingredients) {
        if (ingredients == null) return null;
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>() {}.getType();
        return gson.toJson(ingredients, type);
    }

    @TypeConverter
    public List<Ingredient> toIngredientList(String ingredientString) {
        if (ingredientString == null) return null;
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>() {}.getType();
        return gson.fromJson(ingredientString, type);
    }
}
