package com.example.risotto.presentation.categories.presenter;

import com.example.risotto.presentation.categories.views.CategoryMealsView;

public interface CategoryMealsPresenter {
    void attachView(CategoryMealsView view);
    void detachView();
    void loadMealsByCategory(String categoryName);
    void filterMeals(String query);
}
