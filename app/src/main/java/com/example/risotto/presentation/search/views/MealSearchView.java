package com.example.risotto.presentation.search.views;

import com.example.risotto.data.model.Meal;
import java.util.List;

public interface MealSearchView {
    void showResults(List<Meal> meals);
    void showLoading();
    void hideLoading();
    void showError(String message);
    void showEmptyState(String message);
    void hideEmptyState();
}
