package com.example.risotto.presentation.mealdetail.presenter;

import com.example.risotto.RisottoApp;
import com.example.risotto.data.model.Meal;
import com.example.risotto.data.repository.favorite.FavoriteRepository;
import com.example.risotto.data.repository.meal.MealRepository;
import com.example.risotto.presentation.mealdetail.views.MealDetailView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailPresenterImpl implements MealDetailPresenter {

    private final MealRepository repository;
    private final FavoriteRepository favoriteRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private MealDetailView view;

    public MealDetailPresenterImpl(MealRepository repository, FavoriteRepository favoriteRepository) {
        this.repository = repository;
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public void attachView(MealDetailView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        disposables.clear();
    }

    @Override
    public void loadMealDetail(String mealId) {
        if (view == null) return;
        view.showLoading();

        Disposable disposable = io.reactivex.rxjava3.core.Single.zip(
                        repository.getMealById(mealId)
                                .flatMap(meal -> repository.cacheMeal(meal).toSingleDefault(meal))
                                .onErrorResumeNext(error -> {
                                    return repository.getCachedMealById(mealId);
                                }),
                        favoriteRepository.isFavorite(mealId).onErrorReturnItem(false),
                        (meal, isFav) -> {
                            meal.setFavorite(isFav);
                            return meal;
                        }
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meal -> {
                            if (view == null) return;
                            view.hideLoading();
                            view.showMealDetail(meal);
                            view.updateFavoriteState(meal.isFavorite());
                        },
                        error -> {
                            if (view == null) return;
                            view.hideLoading();
                            view.showError(com.example.risotto.core.utils.ErrorMapper.getErrorMessage(error));
                        }
                );

        disposables.add(disposable);
    }

    @Override
    public void toggleFavorite(Meal meal) {
        if (view == null) return;

        if (!RisottoApp.isRealUser()) {
            view.showError("You must be logged in to save favorites.");
            return;
        }

        boolean newState = !meal.isFavorite();
        meal.setFavorite(newState);
        view.updateFavoriteState(newState);

        if (newState) {
            Disposable d = favoriteRepository.addFavorite(meal)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> view.showFavoriteAdded(),
                            error -> view.showError("Could not add favorite: " + error.getMessage())
                    );
            disposables.add(d);
        } else {
            Disposable d = favoriteRepository.removeFavoriteById(meal.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> view.showFavoriteRemoved(),
                            error -> view.showError("Could not remove favorite: " + error.getMessage())
                    );
            disposables.add(d);
        }
    }
}