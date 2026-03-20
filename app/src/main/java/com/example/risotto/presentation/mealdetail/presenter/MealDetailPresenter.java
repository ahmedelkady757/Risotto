package com.example.risotto.presentation.mealdetail.presenter;

import com.example.risotto.data.model.Meal;
import com.example.risotto.presentation.mealdetail.views.MealDetailView;

public interface MealDetailPresenter {

    void attachView(MealDetailView view);

    void detachView();

    void loadMealDetail(String mealId);

    void toggleFavorite(Meal meal);
}
