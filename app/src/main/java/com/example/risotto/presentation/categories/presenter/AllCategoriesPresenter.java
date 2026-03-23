package com.example.risotto.presentation.categories.presenter;

import com.example.risotto.presentation.categories.view.AllCategoriesView;

public interface AllCategoriesPresenter {
    void attachView(AllCategoriesView view);
    void detachView();
    void loadCategories();
}
