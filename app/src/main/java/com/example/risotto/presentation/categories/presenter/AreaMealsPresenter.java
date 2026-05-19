package com.example.risotto.presentation.categories.presenter;

import com.example.risotto.presentation.categories.views.AreaMealsView;

public interface AreaMealsPresenter {
    void attachView(AreaMealsView view);
    void detachView();
    void loadMealsByArea(String areaName);
    void filterMeals(String query);
}
