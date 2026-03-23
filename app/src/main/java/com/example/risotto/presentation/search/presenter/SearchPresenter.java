package com.example.risotto.presentation.search.presenter;

import com.example.risotto.presentation.search.view.MealSearchView;

public interface SearchPresenter {
    void attachView(MealSearchView view);
    void detachView();
    void search(String query);
}
