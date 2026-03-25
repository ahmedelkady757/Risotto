package com.example.risotto.presentation.home.views;

import com.example.risotto.data.model.Category;
import com.example.risotto.data.model.Meal;

import java.util.List;

public interface HomeView {

    void showMealOfDay(Meal meal);
    void showMealOfDayError(String message);

    void showCategories(List<Category> categories);
    void showCategoriesError(String message);

    void showTopMeals(List<Meal> topMeals);
    void showTopMealsError(String message);

    void showLoading();
    void hideLoading();
    void hideRefresh();
}
