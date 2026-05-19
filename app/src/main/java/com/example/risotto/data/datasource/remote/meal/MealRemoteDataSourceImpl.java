package com.example.risotto.data.datasource.remote.meal;

import com.example.risotto.core.helper.CategoryMapper;
import com.example.risotto.core.helper.MealMapper;
import com.example.risotto.data.model.Category;
import com.example.risotto.data.model.Meal;
import com.example.risotto.data.network.services.MealDBApiService;

import java.util.List;

import io.reactivex.rxjava3.core.Single;


public class MealRemoteDataSourceImpl implements MealRemoteDataSource {

    private final MealDBApiService apiService;

    public MealRemoteDataSourceImpl(MealDBApiService apiService) {
        this.apiService = apiService;
    }


    @Override
    public Single<Meal> getRandomMeal() {
        return apiService.getRandomMeal()
                .map(response -> {
                    List<Meal> meals = MealMapper.fromDtoList(response.getMeals());
                    if (meals.isEmpty()) throw new Exception("No random meal returned");
                    return meals.get(0);
                });
    }


    @Override
    public Single<Meal> getMealById(String id) {
        return apiService.getMealById(id)
                .map(response -> {
                    List<Meal> meals = MealMapper.fromDtoList(response.getMeals());
                    if (meals.isEmpty()) throw new Exception("Meal not found: " + id);
                    return meals.get(0);
                });
    }


    @Override
    public Single<List<Meal>> searchMealsByName(String name) {
        return apiService.searchMealsByName(name)
                .map(response -> MealMapper.fromDtoList(response.getMeals()));
    }




    @Override
    public Single<List<Category>> getCategories() {
        return apiService.getCategories()
                .map(response -> CategoryMapper.fromDtoList(response.getCategories()));
    }

    @Override
    public Single<List<com.example.risotto.data.model.Country>> getCountries() {
        return apiService.getAreas()
                .map(response -> com.example.risotto.core.helper.AreaMapper.map(response.getAreas()));
    }


    @Override
    public Single<List<Meal>> filterByCategory(String category) {
        return apiService.filterByCategory(category)
                .map(response -> MealMapper.fromFilterDtoList(response.getMeals()));
    }



}