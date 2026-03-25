package com.example.risotto.data.repository.meal;

import com.example.risotto.data.datasource.local.meal.MealLocalDataSource;
import com.example.risotto.data.datasource.remote.meal.MealRemoteDataSource;
import com.example.risotto.data.model.Category;
import com.example.risotto.data.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
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
        return remoteDataSource.getRandomMeal();
    }

    @Override
    public Single<List<Category>> getCategories() {
        return remoteDataSource.getCategories();
    }

    @Override
    public Single<Meal> getMealById(String id) {
        return remoteDataSource.getMealById(id);
    }

    @Override
    public Single<List<Meal>> searchMealsByName(String name) {
        return remoteDataSource.searchMealsByName(name);
    }

    @Override
    public Single<List<Meal>> filterByCategory(String category) {
        return remoteDataSource.filterByCategory(category);
    }

    @Override
    public Single<List<Meal>> getTopMeals() {
        return filterByCategory("Seafood")
                .flatMap(meals -> {
                    return io.reactivex.rxjava3.core.Observable.fromIterable(meals)
                            .flatMapCompletable(this::cacheMeal)
                            .toSingleDefault(meals);
                })
                .onErrorResumeNext(error -> {
                    return getCachedTopMeals().firstOrError();
                })
                .map(meals -> meals.size() > 10 ? meals.subList(0, 10) : meals);
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
    public Observable<List<Meal>> getCachedTopMeals() {
        return localDataSource.getCachedTopMeals();
    }

    @Override
    public Observable<List<Category>> getCachedCategories() {
        return localDataSource.getCachedCategories();
    }
}
