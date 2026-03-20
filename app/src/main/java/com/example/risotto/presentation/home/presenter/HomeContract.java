package com.example.risotto.presentation.home.presenter;

import com.example.risotto.data.model.Category;
import com.example.risotto.data.model.Meal;

import java.util.List;


public interface HomeContract {


    interface View {

        void showMealOfDay(Meal meal);
        void showMealOfDayError(String message);

        void showCategories(List<Category> categories);
        void showCategoriesError(String message);

        void showTopMeals(List<Meal> topMeals);
        void showTopMealsError(String message);

        void showLoading();
        void hideLoading();
    }


    interface Presenter {

        void attachView(View view);

        void detachView();

        void loadMealOfDay();

        void loadCategories();

        void loadTopMeals();

        void onCategorySelected(Category category);

        void onMealOfDaySelected(Meal meal);
    }
}