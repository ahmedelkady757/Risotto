package com.example.risotto.data.datasource.local.meal;

import com.example.risotto.core.helper.CachedCategoryMapper;
import com.example.risotto.core.helper.CachedMealMapper;
import com.example.risotto.data.db.dao.CachedCategoryDao;
import com.example.risotto.data.db.dao.CachedMealDao;
import com.example.risotto.data.model.Category;
import com.example.risotto.data.model.Meal;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class MealLocalDataSourceImpl implements MealLocalDataSource {

    private final CachedMealDao cachedMealDao;
    private final CachedCategoryDao cachedCategoryDao;

    public MealLocalDataSourceImpl(CachedMealDao cachedMealDao, CachedCategoryDao cachedCategoryDao) {
        this.cachedMealDao = cachedMealDao;
        this.cachedCategoryDao = cachedCategoryDao;
    }

    @Override
    public Completable cacheMeal(Meal meal) {
        return cachedMealDao.insertMeal(CachedMealMapper.toEntity(meal));
    }

    @Override
    public Completable cacheCategories(List<Category> categories) {
        return cachedCategoryDao.insertCategories(CachedCategoryMapper.toEntityList(categories));
    }

    @Override
    public Single<Meal> getCachedMealById(String id) {
        return cachedMealDao.getMealById(id)
                .map(CachedMealMapper::toMeal);
    }

    @Override
    public Observable<List<Meal>> getCachedTopMeals() {
        return cachedMealDao.getTopMeals()
                .map(CachedMealMapper::toMealList);
    }

    @Override
    public Single<Meal> getCachedRandomMeal() {
        return cachedMealDao.getRandomMeal()
                .map(CachedMealMapper::toMeal);
    }

    @Override
    public Observable<List<Category>> getCachedCategories() {
        return cachedCategoryDao.getAllCategories()
                .map(CachedCategoryMapper::toCategoryList);
    }
}
