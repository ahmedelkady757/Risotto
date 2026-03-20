package com.example.risotto.presentation.mealdetail.views;

import com.example.risotto.data.model.Meal;

public interface MealDetailView {

    void showMealDetail(Meal meal);

    void showError(String message);

    void showLoading();

    void hideLoading();

    void updateFavoriteState(boolean isFavorite);

    void showFavoriteAdded();

    void showFavoriteRemoved();
}
