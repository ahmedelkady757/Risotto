package com.example.risotto.presentation.categories.presenter;

import com.example.risotto.data.repository.meal.MealRepository;
import com.example.risotto.presentation.categories.views.AllCategoriesView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AllCategoriesPresenterImpl implements AllCategoriesPresenter {

    private final MealRepository repository;
    private AllCategoriesView view;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public AllCategoriesPresenterImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public void attachView(AllCategoriesView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        this.view = null;
    }

    @Override
    public void loadCategories() {
        if (view == null) return;

        view.showLoading();
        disposables.add(repository.getCategories()
                .flatMap(categories -> repository.cacheCategories(categories).toSingleDefault(categories))
                .onErrorResumeNext(error -> {
                    return repository.getCachedCategories().firstOrError();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categories -> {
                            if (view != null) {
                                view.hideLoading();
                                view.showCategories(categories);
                            }
                        },
                        throwable -> {
                            if (view != null) {
                                view.hideLoading();
                                view.showError("Failed to load categories. Please try again.");
                            }
                        }
                ));
    }
}
