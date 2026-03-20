package com.example.risotto.presentation.home.presenter;

import com.example.risotto.data.model.Category;
import com.example.risotto.data.model.Meal;
import com.example.risotto.presentation.home.views.HomeView;

public interface HomePresenter {

    void attachView(HomeView view);

    void detachView();

    void loadMealOfDay();

    void loadCategories();

    void loadTopMeals();

    void onCategorySelected(Category category);

    void onMealOfDaySelected(Meal meal);
}