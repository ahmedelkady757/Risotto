package com.example.risotto.presentation.categories.views;

import com.example.risotto.data.model.Meal;
import java.util.List;

public interface CategoryMealsView {
    void showMeals(List<Meal> meals);
    void showLoading();
    void hideLoading();
    void showError(String message);
}
