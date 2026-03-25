package com.example.risotto.data.repository.meal;

import com.example.risotto.core.utils.AppLogger;
import com.example.risotto.data.datasource.local.meal.MealLocalDataSource;
import com.example.risotto.data.datasource.remote.meal.MealRemoteDataSource;
import com.example.risotto.data.model.Category;
import com.example.risotto.data.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class MealRepositoryImpl implements MealRepository {

    private final MealRemoteDataSource remoteDataSource;
    private final MealLocalDataSource localDataSource;

    public MealRepositoryImpl(MealRemoteDataSource remoteDataSource, MealLocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    @Override
    public Single<Meal> getRandomMeal() {
        AppLogger.d("MealRepository: getRandomMeal (Remote Only)");
        return remoteDataSource.getRandomMeal();
    }

    @Override
    public Single<List<Category>> getCategories() {
        AppLogger.d("MealRepository: getCategories (Remote Only)");
        return remoteDataSource.getCategories();
    }

    @Override
    public Single<Meal> getMealById(String id) {
        AppLogger.d("MealRepository: getMealById (Remote Only) → " + id);
        return remoteDataSource.getMealById(id);
    }

    @Override
    public Single<List<Meal>> searchMealsByName(String name) {
        AppLogger.d("MealRepository: searchMealsByName → " + name);
        return remoteDataSource.searchMealsByName(name);
    }

    @Override
    public Single<List<Meal>> filterByCategory(String category) {
        AppLogger.d("MealRepository: filterByCategory → " + category);
        return remoteDataSource.filterByCategory(category);
    }

    @Override
    public Completable cacheMeal(Meal meal) {
        return localDataSource.cacheMeal(meal);
    }

    @Override
    public Completable cacheCategories(List<Category> categories) {
        return localDataSource.cacheCategories(categories);
    }

    @Override
    public Single<Meal> getCachedRandomMeal() {
        return localDataSource.getCachedRandomMeal();
    }

    @Override
    public Single<Meal> getCachedMealById(String id) {
        return localDataSource.getCachedMealById(id);
    }

    @Override
    public Flowable<List<Meal>> getCachedTopMeals() {
        return localDataSource.getCachedTopMeals();
    }

    @Override
    public Flowable<List<Category>> getCachedCategories() {
        return localDataSource.getCachedCategories();
    }
}
