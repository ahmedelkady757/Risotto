package com.example.risotto.data.datasource.local.favorite;

import com.example.risotto.data.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface FavoriteLocalDataSource {
    Completable addFavorite(Meal meal, String userId);
    Completable removeFavorite(Meal meal, String userId);
    Completable removeFavoriteById(String mealId, String userId);
    Observable<List<Meal>> getFavorites(String userId);
    Single<Boolean> isFavorite(String mealId, String userId);
    Completable clearFavorites(String userId);
}
