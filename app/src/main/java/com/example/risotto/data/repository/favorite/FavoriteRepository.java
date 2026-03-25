package com.example.risotto.data.repository.favorite;

import com.example.risotto.data.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface FavoriteRepository {
    Completable addFavorite(Meal meal);
    Completable removeFavoriteById(String mealId);
    Observable<List<Meal>> getFavorites();
    Single<Boolean> isFavorite(String mealId);
    Single<Meal> getFavoriteById(String mealId);
    Completable clearFavorites();
}
