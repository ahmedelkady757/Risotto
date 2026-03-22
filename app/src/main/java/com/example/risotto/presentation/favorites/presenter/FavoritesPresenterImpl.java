package com.example.risotto.presentation.favorites.presenter;

import com.example.risotto.core.utils.AppLogger;
import com.example.risotto.data.model.Meal;
import com.example.risotto.data.repository.favorite.FavoriteRepository;
import com.example.risotto.presentation.favorites.views.FavoritesView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritesPresenterImpl implements FavoritesPresenter {

    private final FavoriteRepository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private FavoritesView view;

    public FavoritesPresenterImpl(FavoriteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void attachView(FavoritesView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        disposables.clear();
    }

    @Override
    public void loadFavorites() {
        if (view == null) return;
        view.showLoading();

        Disposable disposable = repository.getFavorites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> {
                            if (view == null) return;
                            view.hideLoading();
                            if (meals == null || meals.isEmpty()) {
                                view.showEmptyState();
                            } else {
                                view.showFavorites(meals);
                            }
                        },
                        error -> {
                            if (view == null) return;
                            view.hideLoading();
                            view.showError("Failed to load favorites: " + error.getMessage());
                            AppLogger.e("FavoritesPresenterImpl: Error loading favorites", error);
                        }
                );
        disposables.add(disposable);
    }

    @Override
    public void removeFavorite(Meal meal) {
        Disposable disposable = repository.removeFavoriteById(meal.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> AppLogger.d("FavoritesPresenterImpl: Removed favorite meal " + meal.getId()),
                        error -> {
                            if (view != null) {
                                view.showError("Could not remove favorite: " + error.getMessage());
                            }
                        }
                );
        disposables.add(disposable);
    }
}
