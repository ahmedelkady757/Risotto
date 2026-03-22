package com.example.risotto.data.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.risotto.data.db.entity.FavoriteMealEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertFavorite(FavoriteMealEntity favoriteMealEntity);

    @Delete
    Completable deleteFavorite(FavoriteMealEntity favoriteMealEntity);

    @Query("SELECT * FROM favorite_meals WHERE userId = :userId")
    Flowable<List<FavoriteMealEntity>> getFavoritesForUser(String userId);

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_meals WHERE userId = :userId AND id = :mealId LIMIT 1)")
    Single<Boolean> isFavorite(String userId, String mealId);

    @Query("DELETE FROM favorite_meals WHERE userId = :userId AND id = :mealId")
    Completable removeFavoriteById(String userId, String mealId);

    @Query("DELETE FROM favorite_meals WHERE userId = :userId")
    Completable clearFavoritesForUser(String userId);
}
