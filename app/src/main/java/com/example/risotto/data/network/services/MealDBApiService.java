package com.example.risotto.data.network.services;

import com.example.risotto.data.network.dto.CategoryResponse;
import com.example.risotto.data.network.dto.FilterResponse;
import com.example.risotto.data.network.dto.MealResponse;
import com.example.risotto.data.network.dto.AreaResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface MealDBApiService {



    @GET("random.php")
    Single<MealResponse> getRandomMeal();


    @GET("lookup.php")
    Single<MealResponse> getMealById(@Query("i") String id);


    @GET("search.php")
    Single<MealResponse> searchMealsByName(@Query("s") String name);


    @GET("categories.php")
    Single<CategoryResponse> getCategories();


    @GET("filter.php")
    Single<FilterResponse> filterByCategory(@Query("c") String category);

    @GET("list.php?a=list")
    Single<AreaResponse> getAreas();
    
    @GET("filter.php")
    Single<FilterResponse> filterByArea(@Query("a") String area);

}