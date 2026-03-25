package com.example.risotto.data.datasource.local.favorite;

import com.example.risotto.core.helper.FavoriteMealMapper;
import com.example.risotto.data.db.dao.FavoriteDao;
import com.example.risotto.data.db.entity.FavoriteMealEntity;
import com.example.risotto.data.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class FavoriteLocalDataSourceImpl implements FavoriteLocalDataSource {

    private final FavoriteDao favoriteDao;

    public FavoriteLocalDataSourceImpl(FavoriteDao favoriteDao) {
        this.favoriteDao = favoriteDao;
    }

    @Override
    public Completable addFavorite(Meal meal, String userId) {
        FavoriteMealEntity entity = FavoriteMealMapper.toEntity(meal, userId);
        return favoriteDao.insertFavorite(entity);
    }

    @Override
    public Completable removeFavorite(Meal meal, String userId) {
        FavoriteMealEntity entity = FavoriteMealMapper.toEntity(meal, userId);
        return favoriteDao.deleteFavorite(entity);
    }

    @Override
    public Completable removeFavoriteById(String mealId, String userId) {
        return favoriteDao.removeFavoriteById(userId, mealId);
    }

    @Override
    public Observable<List<Meal>> getFavorites(String userId) {
        return favoriteDao.getFavoritesForUser(userId)
                .map(FavoriteMealMapper::toMealList);
    }

    @Override
    public Single<Boolean> isFavorite(String mealId, String userId) {
        return favoriteDao.isFavorite(userId, mealId);
    }

    @Override
    public Completable clearFavorites(String userId) {
        return favoriteDao.clearFavoritesForUser(userId);
    }
}
