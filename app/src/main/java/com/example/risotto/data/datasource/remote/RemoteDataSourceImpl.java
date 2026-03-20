package com.example.risotto.data.datasource.remote;

import com.example.risotto.core.helper.CategoryMapper;
import com.example.risotto.core.helper.MealMapper;
import com.example.risotto.core.utils.AppLogger;
import com.example.risotto.data.model.Category;
import com.example.risotto.data.model.Meal;
import com.example.risotto.data.network.api.MealDBApiService;

import java.util.List;

import io.reactivex.rxjava3.core.Single;


public class RemoteDataSourceImpl implements RemoteDataSource {

    private final MealDBApiService apiService;

    public RemoteDataSourceImpl(MealDBApiService apiService) {
        this.apiService = apiService;
    }


    @Override
    public Single<Meal> getRandomMeal() {
        AppLogger.d("RemoteDataSource: getRandomMeal");
        return apiService.getRandomMeal()
                .map(response -> {
                    List<Meal> meals = MealMapper.fromDtoList(response.getMeals());
                    if (meals.isEmpty()) throw new Exception("No random meal returned");
                    return meals.get(0);
                });
    }


    @Override
    public Single<List<Meal>> getLatestMeals() {
        AppLogger.d("RemoteDataSource: getLatestMeals");
        return apiService.getLatestMeals()
                .map(response -> MealMapper.fromDtoList(response.getMeals()));
    }


    @Override
    public Single<Meal> getMealById(String id) {
        AppLogger.d("RemoteDataSource: getMealById → " + id);
        return apiService.getMealById(id)
                .map(response -> {
                    List<Meal> meals = MealMapper.fromDtoList(response.getMeals());
                    if (meals.isEmpty()) throw new Exception("Meal not found: " + id);
                    return meals.get(0);
                });
    }


    @Override
    public Single<List<Meal>> searchMealsByName(String name) {
        AppLogger.d("RemoteDataSource: searchMealsByName → " + name);
        return apiService.searchMealsByName(name)
                .map(response -> MealMapper.fromDtoList(response.getMeals()));
    }

    @Override
    public Single<List<Meal>> searchMealsByFirstLetter(String letter) {
        AppLogger.d("RemoteDataSource: searchMealsByFirstLetter → " + letter);
        return apiService.searchMealsByFirstLetter(letter)
                .map(response -> MealMapper.fromDtoList(response.getMeals()));
    }


    @Override
    public Single<List<Category>> getCategories() {
        AppLogger.d("RemoteDataSource: getCategories");
        return apiService.getCategories()
                .map(response -> CategoryMapper.fromDtoList(response.getCategories()));
    }


    @Override
    public Single<List<Meal>> filterByCategory(String category) {
        AppLogger.d("RemoteDataSource: filterByCategory → " + category);
        return apiService.filterByCategory(category)
                .map(response -> MealMapper.fromFilterDtoList(response.getMeals()));
    }

    @Override
    public Single<List<Meal>> filterByArea(String area) {
        AppLogger.d("RemoteDataSource: filterByArea → " + area);
        return apiService.filterByArea(area)
                .map(response -> MealMapper.fromFilterDtoList(response.getMeals()));
    }

    @Override
    public Single<List<Meal>> filterByIngredient(String ingredient) {
        AppLogger.d("RemoteDataSource: filterByIngredient → " + ingredient);
        return apiService.filterByIngredient(ingredient)
                .map(response -> MealMapper.fromFilterDtoList(response.getMeals()));
    }


    @Override
    public Single<List<Meal>> listAllAreas() {
        AppLogger.d("RemoteDataSource: listAllAreas");
        return apiService.listAllAreas("list")
                .map(response -> MealMapper.fromDtoList(response.getMeals()));
    }

    @Override
    public Single<List<Meal>> listAllCategories() {
        AppLogger.d("RemoteDataSource: listAllCategories");
        return apiService.listAllCategories("list")
                .map(response -> MealMapper.fromDtoList(response.getMeals()));
    }

    @Override
    public Single<List<Meal>> listAllIngredients() {
        AppLogger.d("RemoteDataSource: listAllIngredients");
        return apiService.listAllIngredients("list")
                .map(response -> MealMapper.fromDtoList(response.getMeals()));
    }
}