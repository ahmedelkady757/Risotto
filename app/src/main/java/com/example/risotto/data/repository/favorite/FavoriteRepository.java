package com.example.risotto.data.repository.favorite;

import com.example.risotto.data.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface FavoriteRepository {
    Completable addFavorite(Meal meal);
    Completable removeFavorite(Meal meal);
    Completable removeFavoriteById(String mealId);
    Flowable<List<Meal>> getFavorites();
    Single<Boolean> isFavorite(String mealId);
    Completable clearFavorites();
}
