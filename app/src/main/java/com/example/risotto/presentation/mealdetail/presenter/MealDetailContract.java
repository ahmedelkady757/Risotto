package com.example.risotto.presentation.mealdetail.presenter;

import com.example.risotto.data.model.Meal;

public interface MealDetailContract {


    interface View {

        void showMealDetail(Meal meal);

        void showError(String message);

        void showLoading();
        void hideLoading();

        void updateFavoriteState(boolean isFavorite);

        void showFavoriteAdded();
        void showFavoriteRemoved();
    }


    interface Presenter {

        void attachView(View view);

        void detachView();

        void loadMealDetail(String mealId);

        void toggleFavorite(Meal meal);
    }
}