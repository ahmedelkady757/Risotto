package com.example.risotto.data.datasource.local.meal;

import com.example.risotto.core.helper.CachedMealMapper;
import com.example.risotto.data.db.dao.CachedMealDao;
import com.example.risotto.data.db.entity.CachedMealEntity;
import com.example.risotto.data.model.Meal;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class MealLocalDataSourceImpl implements MealLocalDataSource {

    private final CachedMealDao cachedMealDao;
    private final com.example.risotto.data.db.dao.CachedCategoryDao cachedCategoryDao;

    public MealLocalDataSourceImpl(CachedMealDao cachedMealDao, com.example.risotto.data.db.dao.CachedCategoryDao cachedCategoryDao) {
        this.cachedMealDao = cachedMealDao;
        this.cachedCategoryDao = cachedCategoryDao;
    }

    @Override
    public Completable cacheMeal(Meal meal) {
        return cachedMealDao.insertMeal(CachedMealMapper.toEntity(meal));
    }

    @Override
    public Completable cacheCategories(java.util.List<com.example.risotto.data.model.Category> categories) {
        java.util.List<com.example.risotto.data.db.entity.CachedCategoryEntity> entities = new java.util.ArrayList<>();
        for (com.example.risotto.data.model.Category c : categories) {
            entities.add(com.example.risotto.core.helper.CachedCategoryMapper.toEntity(c));
        }
        return cachedCategoryDao.insertCategories(entities);
    }

    @Override
    public Single<Meal> getCachedMealById(String id) {
        return cachedMealDao.getMealById(id)
                .map(CachedMealMapper::toMeal);
    }

    @Override
    public Single<java.util.List<Meal>> getCachedTopMeals() {
        return cachedMealDao.getTopMeals()
                .map(entities -> {
                    java.util.List<Meal> meals = new java.util.ArrayList<>();
                    for (com.example.risotto.data.db.entity.CachedMealEntity e : entities) {
                        meals.add(CachedMealMapper.toMeal(e));
                    }
                    return meals;
                });
    }

    @Override
    public Single<Meal> getCachedRandomMeal() {
        return cachedMealDao.getRandomMeal()
                .map(CachedMealMapper::toMeal);
    }

    @Override
    public Single<java.util.List<com.example.risotto.data.model.Category>> getCachedCategories() {
        return cachedCategoryDao.getAllCategories()
                .map(entities -> {
                    java.util.List<com.example.risotto.data.model.Category> cats = new java.util.ArrayList<>();
                    for (com.example.risotto.data.db.entity.CachedCategoryEntity e : entities) {
                        cats.add(com.example.risotto.core.helper.CachedCategoryMapper.toCategory(e));
                    }
                    return cats;
                });
    }
}
