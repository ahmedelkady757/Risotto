package com.example.risotto.data.network.api;

import com.example.risotto.data.network.dto.CategoryResponse;
import com.example.risotto.data.network.dto.FilterResponse;
import com.example.risotto.data.network.dto.MealResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface MealDBApiService {



    @GET("random.php")
    Single<MealResponse> getRandomMeal();


    @GET("latest.php")
    Single<MealResponse> getLatestMeals();


    @GET("lookup.php")
    Single<MealResponse> getMealById(@Query("i") String id);


    @GET("search.php")
    Single<MealResponse> searchMealsByName(@Query("s") String name);


    @GET("search.php")
    Single<MealResponse> searchMealsByFirstLetter(@Query("f") String letter);


    @GET("categories.php")
    Single<CategoryResponse> getCategories();


    @GET("list.php")
    Single<MealResponse> listAllAreas(@Query("a") String list);


    @GET("list.php")
    Single<MealResponse> listAllCategories(@Query("c") String list);


    @GET("list.php")
    Single<MealResponse> listAllIngredients(@Query("i") String list);


    @GET("filter.php")
    Single<FilterResponse> filterByCategory(@Query("c") String category);


    @GET("filter.php")
    Single<FilterResponse> filterByArea(@Query("a") String area);


    @GET("filter.php")
    Single<FilterResponse> filterByIngredient(@Query("i") String ingredient);
}