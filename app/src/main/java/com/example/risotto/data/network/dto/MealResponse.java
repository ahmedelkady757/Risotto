package com.example.risotto.data.network.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class MealResponse {

    @SerializedName("meals")
    private List<MealDto> meals;

    public List<MealDto> getMeals() { return meals; }
}