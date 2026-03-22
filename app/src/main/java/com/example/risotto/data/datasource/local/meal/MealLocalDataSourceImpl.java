package com.example.risotto.data.datasource.local.meal;

import com.example.risotto.core.helper.CachedMealMapper;
import com.example.risotto.data.db.dao.CachedMealDao;
import com.example.risotto.data.db.entity.CachedMealEntity;
import com.example.risotto.data.model.Meal;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class MealLocalDataSourceImpl implements MealLocalDataSource {

    private final CachedMealDao dao;

    public MealLocalDataSourceImpl(CachedMealDao dao) {
        this.dao = dao;
    }

    @Override
    public Completable cacheMeal(Meal meal) {
        CachedMealEntity entity = CachedMealMapper.toEntity(meal);
        if (entity == null) return Completable.complete();
        return dao.insertMeal(entity);
    }

    @Override
    public Single<Meal> getCachedMealById(String id) {
        return dao.getMealById(id)
                .map(CachedMealMapper::toMeal);
    }
}
