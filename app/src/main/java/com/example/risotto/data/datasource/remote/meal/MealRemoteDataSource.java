package com.example.risotto.data.datasource.remote.meal;

import com.example.risotto.data.model.Category;
import com.example.risotto.data.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Single;


public interface MealRemoteDataSource {

    Single<Meal> getRandomMeal();


    Single<Meal> getMealById(String id);

    Single<List<Meal>> searchMealsByName(String name);

    Single<List<Category>> getCategories();
    
    Single<List<com.example.risotto.data.model.Country>> getCountries();

    Single<List<Meal>> filterByCategory(String category);

}