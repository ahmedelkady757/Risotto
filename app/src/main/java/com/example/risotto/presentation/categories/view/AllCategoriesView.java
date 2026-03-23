package com.example.risotto.presentation.categories.view;

import com.example.risotto.data.model.Category;
import java.util.List;

public interface AllCategoriesView {
    void showCategories(List<Category> categories);
    void showLoading();
    void hideLoading();
    void showError(String message);
}
