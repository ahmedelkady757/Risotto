package com.example.risotto.presentation.favorites.presenter;

import com.example.risotto.data.model.Meal;
import com.example.risotto.presentation.favorites.views.FavoritesView;

public interface FavoritesPresenter {
    void attachView(FavoritesView view);
    void detachView();
    void loadFavorites();
    void removeFavorite(Meal meal);
}
