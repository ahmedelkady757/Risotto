package com.example.risotto.data.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.risotto.data.db.entity.CachedMealEntity;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface CachedMealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertMeal(CachedMealEntity meal);

    @Query("SELECT * FROM cached_meals WHERE id = :mealId LIMIT 1")
    Single<CachedMealEntity> getMealById(String mealId);
}
