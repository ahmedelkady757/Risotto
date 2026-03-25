package com.example.risotto.presentation.search.presenter;

import com.example.risotto.presentation.search.views.MealSearchView;

public interface SearchPresenter {
    void attachView(MealSearchView view);
    void detachView();
    void search(String query);
}
