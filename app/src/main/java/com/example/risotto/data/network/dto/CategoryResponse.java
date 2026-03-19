package com.example.risotto.data.network.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class CategoryResponse {

    @SerializedName("categories")
    private List<CategoryDto> categories;

    public List<CategoryDto> getCategories() { return categories; }
}