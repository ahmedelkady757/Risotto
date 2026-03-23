package com.example.risotto.presentation.search.presenter;

import com.example.risotto.core.utils.AppLogger;
import com.example.risotto.data.repository.meal.MealRepository;
import com.example.risotto.presentation.search.view.MealSearchView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class SearchPresenterImpl implements SearchPresenter {

    private final MealRepository repository;
    private MealSearchView view;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final PublishSubject<String> searchSubject = PublishSubject.create();

    public SearchPresenterImpl(MealRepository repository) {
        this.repository = repository;
        setupSearchDebounce();
    }

    private void setupSearchDebounce() {
        disposables.add(searchSubject
                .debounce(500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(query -> {
                    if (view != null) {
                        if (query.isEmpty()) {
                            view.showResults(java.util.Collections.emptyList());
                            view.showEmptyState("Start typing to find delicious meals");
                        } else {
                            view.showLoading();
                            view.hideEmptyState();
                        }
                    }
                })
                .observeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                .switchMapSingle(query -> {
                    if (query.isEmpty()) {
                        return io.reactivex.rxjava3.core.Single
                                .just(java.util.Collections.<com.example.risotto.data.model.Meal>emptyList());
                    }
                    return repository.searchMealsByName(query)
                            .onErrorReturnItem(java.util.Collections.<com.example.risotto.data.model.Meal>emptyList());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> {
                            if (view != null) {
                                view.hideLoading();
                                List<com.example.risotto.data.model.Meal> mealList = (List<com.example.risotto.data.model.Meal>) meals;
                                view.showResults(mealList);
                                if (mealList.isEmpty()) {
                                    view.showEmptyState("No meals found for this query");
                                }
                            }
                        },
                        throwable -> {
                            AppLogger.e("SearchPresenter: Error in search flow: " + throwable.getMessage());
                            if (view != null) {
                                view.hideLoading();
                                view.showError("An error occurred during search");
                            }
                        }));
    }

    @Override
    public void attachView(MealSearchView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        this.view = null;
    }

    @Override
    public void search(String query) {
        searchSubject.onNext(query.trim());
    }
}
