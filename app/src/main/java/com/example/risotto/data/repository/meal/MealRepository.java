package com.example.risotto.data.repository.meal;

import com.example.risotto.data.model.Category;
import com.example.risotto.data.model.Meal;
import com.example.risotto.data.model.Country;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;


public interface MealRepository {

    Single<Meal> getRandomMeal();
    Single<List<Category>> getCategories();
    Single<List<Country>> getCountries();

    Single<Meal> getMealById(String id);

    Single<List<Meal>> searchMealsByName(String name);
    
    Single<List<Meal>> filterByArea(String area);
    Single<List<Meal>> filterByCategory(String category);
    Single<List<Meal>> getTopMeals();

    Completable cacheMeal(Meal meal);
    Completable cacheCategories(List<Category> categories);
    Single<Meal> getCachedRandomMeal();
    Single<Meal> getCachedMealById(String id);
    Observable<List<Meal>> getCachedTopMeals();
    Observable<List<Category>> getCachedCategories();
}