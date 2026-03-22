package com.example.risotto.presentation.favorites.views;

import com.example.risotto.data.model.Meal;

import java.util.List;

public interface FavoritesView {
    void showLoading();
    void hideLoading();
    void showFavorites(List<Meal> meals);
    void showError(String error);
    void showEmptyState();
}
