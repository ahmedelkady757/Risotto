package com.example.risotto.data.datasource.local.meal;

import com.example.risotto.data.model.Category;
import com.example.risotto.data.model.Meal;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface MealLocalDataSource {
    Completable cacheMeal(Meal meal);
    Completable cacheCategories(List<com.example.risotto.data.model.Category> categories);
    
    Single<Meal> getCachedMealById(String id);
    Observable<List<Meal>> getCachedTopMeals();
    Single<Meal> getCachedRandomMeal();
    Observable<List<Category>> getCachedCategories();
}
