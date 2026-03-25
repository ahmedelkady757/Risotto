package com.example.risotto.presentation.search.presenter;

import com.example.risotto.data.model.Meal;
import com.example.risotto.data.repository.meal.MealRepository;
import com.example.risotto.presentation.search.views.MealSearchView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class SearchPresenterImpl implements SearchPresenter {

    private final android.content.Context context;
    private final MealRepository repository;
    private MealSearchView view;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private final BehaviorSubject<String> searchSubject = BehaviorSubject.create();
    private static List<Meal> lastResults;
    private static String lastSuccessfulQuery = null;

    public SearchPresenterImpl(android.content.Context context, MealRepository repository) {
        this.context = context;
        this.repository = repository;
    }

    private void setupSearchDebounce() {
        disposables.add(searchSubject
                .debounce(500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(query -> {
                    if (view != null) {
                        view.showLoading();
                        view.hideEmptyState();
                    }
                })
                .observeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                .switchMapSingle(query -> {
                    if (query.isEmpty()) {
                        return repository.getTopMeals()
                                .doOnSuccess(meals -> {
                                    lastResults = meals;
                                    lastSuccessfulQuery = "";
                                })
                                .onErrorReturnItem(java.util.Collections.emptyList());
                    }
                    if (query.equalsIgnoreCase(lastSuccessfulQuery) && lastResults != null) {
                        return io.reactivex.rxjava3.core.Single.just(lastResults);
                    }
                    return repository.searchMealsByName(query)
                            .doOnSuccess(meals -> {
                                lastResults = (List<com.example.risotto.data.model.Meal>) meals;
                                lastSuccessfulQuery = query;
                            })
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
                                    view.showEmptyState(context.getString(com.example.risotto.R.string.search_no_results));
                                }
                            }
                        },
                        throwable -> {
                            if (view != null) {
                                view.hideLoading();
                                view.showError(context.getString(com.example.risotto.R.string.search_error));
                            }
                        }));
    }

    @Override
    public void attachView(MealSearchView view) {
        this.view = view;
        if (lastResults != null && !lastResults.isEmpty()) {
            view.showResults(lastResults);
            view.hideLoading();
            view.hideEmptyState();
        } else {
            search("");
        }
        if (disposables.size() == 0) {
            setupSearchDebounce();
        }
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
