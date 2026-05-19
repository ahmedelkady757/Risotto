package com.example.risotto.presentation.categories.presenter;

import com.example.risotto.data.model.Meal;
import com.example.risotto.data.repository.meal.MealRepository;
import com.example.risotto.presentation.categories.views.AreaMealsView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AreaMealsPresenterImpl implements AreaMealsPresenter {

    private final MealRepository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private AreaMealsView view;
    private List<Meal> allMeals = new ArrayList<>();

    public AreaMealsPresenterImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public void attachView(AreaMealsView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        disposables.clear();
    }

    @Override
    public void loadMealsByArea(String areaName) {
        if (view == null) return;

        view.showLoading();
        Disposable disposable = repository.filterByArea(areaName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> {
                            if (view == null) return;
                            allMeals = meals;
                            view.hideLoading();
                            view.showMeals(meals);
                        },
                        error -> {
                            if (view != null) {
                                view.hideLoading();
                                view.showError(error.getMessage());
                            }
                        }
                );
        disposables.add(disposable);
    }

    @Override
    public void filterMeals(String query) {
        if (allMeals == null) return;
        if (query == null || query.trim().isEmpty()) {
            if (view != null) view.showMeals(allMeals);
            return;
        }
        String lower = query.toLowerCase().trim();
        List<Meal> filtered = new ArrayList<>();
        for (Meal meal : allMeals) {
            if (meal.getName() != null && meal.getName().toLowerCase().contains(lower)) {
                filtered.add(meal);
            }
        }
        if (view != null) view.showMeals(filtered);
    }
}
