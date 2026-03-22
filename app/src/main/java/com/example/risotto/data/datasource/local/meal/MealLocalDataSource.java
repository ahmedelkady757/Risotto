package com.example.risotto.data.datasource.local.meal;

import com.example.risotto.data.model.Meal;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface MealLocalDataSource {
    Completable cacheMeal(Meal meal);
    Completable cacheCategories(java.util.List<com.example.risotto.data.model.Category> categories);
    
    Single<Meal> getCachedMealById(String id);
    Single<java.util.List<Meal>> getCachedTopMeals();
    Single<Meal> getCachedRandomMeal();
    Single<java.util.List<com.example.risotto.data.model.Category>> getCachedCategories();
}
