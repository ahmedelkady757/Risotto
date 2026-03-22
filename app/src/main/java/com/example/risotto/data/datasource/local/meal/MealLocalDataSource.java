package com.example.risotto.data.datasource.local.meal;

import com.example.risotto.data.model.Meal;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface MealLocalDataSource {
    Completable cacheMeal(Meal meal);
    Single<Meal> getCachedMealById(String id);
}
