package com.example.risotto.data.repository.meal;

import com.example.risotto.core.utils.AppLogger;
import com.example.risotto.data.datasource.remote.meal.MealRemoteDataSource;
import com.example.risotto.data.model.Category;
import com.example.risotto.data.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MealRepositoryImpl implements MealRepository {

    private final MealRemoteDataSource remoteDataSource;
    private final com.example.risotto.data.datasource.local.meal.MealLocalDataSource localDataSource;

    public MealRepositoryImpl(MealRemoteDataSource remoteDataSource, 
                              com.example.risotto.data.datasource.local.meal.MealLocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }


    @Override
    public Single<Meal> getRandomMeal() {
        AppLogger.d("MealRepository: getRandomMeal");
        return remoteDataSource.getRandomMeal()
                .flatMap(meal -> localDataSource.cacheMeal(meal).toSingleDefault(meal))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<List<Meal>> getLatestMeals() {
        AppLogger.d("MealRepository: getLatestMeals");
        return remoteDataSource.getLatestMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<List<Category>> getCategories() {
        AppLogger.d("MealRepository: getCategories");
        return remoteDataSource.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public Single<Meal> getMealById(String id) {
        AppLogger.d("MealRepository: getMealById → " + id);
        return remoteDataSource.getMealById(id)
                .flatMap(meal -> localDataSource.cacheMeal(meal).toSingleDefault(meal))
                .onErrorResumeNext(error -> {
                    AppLogger.w("MealRepository: Network failed, trying local cache for " + id);
                    return localDataSource.getCachedMealById(id);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public Single<List<Meal>> searchMealsByName(String name) {
        AppLogger.d("MealRepository: searchMealsByName → " + name);
        return remoteDataSource.searchMealsByName(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<List<Meal>> searchMealsByFirstLetter(String letter) {
        AppLogger.d("MealRepository: searchMealsByFirstLetter → " + letter);
        return remoteDataSource.searchMealsByFirstLetter(letter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<List<Meal>> filterByCategory(String category) {
        AppLogger.d("MealRepository: filterByCategory → " + category);
        return remoteDataSource.filterByCategory(category)
                .flatMap(meals -> {
                    return io.reactivex.rxjava3.core.Observable.fromIterable(meals)
                            .flatMapCompletable(meal -> localDataSource.cacheMeal(meal))
                            .toSingleDefault(meals);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<List<Meal>> filterByArea(String area) {
        AppLogger.d("MealRepository: filterByArea → " + area);
        return remoteDataSource.filterByArea(area)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<List<Meal>> filterByIngredient(String ingredient) {
        AppLogger.d("MealRepository: filterByIngredient → " + ingredient);
        return remoteDataSource.filterByIngredient(ingredient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<List<Meal>> listAllAreas() {
        AppLogger.d("MealRepository: listAllAreas");
        return remoteDataSource.listAllAreas()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<List<Meal>> listAllIngredients() {
        AppLogger.d("MealRepository: listAllIngredients");
        return remoteDataSource.listAllIngredients()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}