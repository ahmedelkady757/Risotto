package com.example.risotto.data.network.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class FilterResponse {

    @SerializedName("meals")
    private List<FilterDto> meals;

    public List<FilterDto> getMeals() { return meals; }
}