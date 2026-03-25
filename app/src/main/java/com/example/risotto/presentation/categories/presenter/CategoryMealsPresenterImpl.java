package com.example.risotto.presentation.categories.presenter;

import com.example.risotto.data.model.Meal;
import com.example.risotto.data.repository.meal.MealRepository;
import com.example.risotto.presentation.categories.views.CategoryMealsView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class CategoryMealsPresenterImpl implements CategoryMealsPresenter {

    private final MealRepository repository;
    private CategoryMealsView view;
    private final CompositeDisposable disposables = new CompositeDisposable();
    
    private List<Meal> allMealsInCategory = new ArrayList<>();
    private final PublishSubject<String> filterSubject = PublishSubject.create();

    public CategoryMealsPresenterImpl(MealRepository repository) {
        this.repository = repository;
        setupFilterDebounce();
    }

    private void setupFilterDebounce() {
        disposables.add(filterSubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::applyFilter));
    }

    @Override
    public void attachView(CategoryMealsView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        this.view = null;
    }

    @Override
    public void loadMealsByCategory(String categoryName) {
        if (view == null) return;

        view.showLoading();
        disposables.add(repository.filterByCategory(categoryName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> {
                            allMealsInCategory = meals;
                            if (view != null) {
                                view.hideLoading();
                                view.showMeals(meals);
                            }
                        },
                        throwable -> {
                            if (view != null) {
                                view.hideLoading();
                                view.showError("Failed to load category meals.");
                            }
                        }
                ));
    }

    @Override
    public void filterMeals(String query) {
        filterSubject.onNext(query);
    }

    private void applyFilter(String query) {
        if (view == null) return;
        
        if (query.isEmpty()) {
            view.showMeals(allMealsInCategory);
            return;
        }

        List<Meal> filtered = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        for (Meal meal : allMealsInCategory) {
            if (meal.getName().toLowerCase().contains(lowerQuery)) {
                filtered.add(meal);
            }
        }
        view.showMeals(filtered);
    }
}
