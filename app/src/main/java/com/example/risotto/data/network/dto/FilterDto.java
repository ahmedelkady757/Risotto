package com.example.risotto.data.network.dto;

import com.google.gson.annotations.SerializedName;


public class FilterDto {

    @SerializedName("idMeal")
    private String idMeal;

    @SerializedName("strMeal")
    private String strMeal;

    @SerializedName("strMealThumb")
    private String strMealThumb;


    public String getIdMeal()       { return idMeal; }
    public String getStrMeal()      { return strMeal; }
    public String getStrMealThumb() { return strMealThumb; }
}