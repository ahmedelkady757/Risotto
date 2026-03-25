package com.example.risotto.data.repository.favorite;

import com.example.risotto.data.datasource.local.favorite.FavoriteLocalDataSource;
import com.example.risotto.data.model.Meal;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class FavoriteRepositoryImpl implements FavoriteRepository {

    private final FavoriteLocalDataSource localDataSource;
    private final FirebaseAuth firebaseAuth;

    public FavoriteRepositoryImpl(FavoriteLocalDataSource localDataSource) {
        this.localDataSource = localDataSource;
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    private String getCurrentUserId() {
        if (firebaseAuth.getCurrentUser() != null) {
            return firebaseAuth.getCurrentUser().getUid();
        }
        return "GUEST_USER";
    }

    @Override
    public Completable addFavorite(Meal meal) {
        return localDataSource.addFavorite(meal, getCurrentUserId());
    }


    @Override
    public Completable removeFavoriteById(String mealId) {
        return localDataSource.removeFavoriteById(mealId, getCurrentUserId());
    }

    @Override
    public Observable<List<Meal>> getFavorites() {
        return localDataSource.getFavorites(getCurrentUserId());
    }

    @Override
    public Single<Boolean> isFavorite(String mealId) {
        return localDataSource.isFavorite(mealId, getCurrentUserId());
    }

    @Override
    public Single<Meal> getFavoriteById(String mealId) {
        return localDataSource.getFavoriteById(mealId, getCurrentUserId());
    }

    @Override
    public Completable clearFavorites() {
        return localDataSource.clearFavorites(getCurrentUserId());
    }
}
