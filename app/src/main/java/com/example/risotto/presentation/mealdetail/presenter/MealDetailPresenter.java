package com.example.risotto.presentation.mealdetail.presenter;

import com.example.risotto.core.utils.AppLogger;
import com.example.risotto.data.model.Meal;
import com.example.risotto.data.repository.MealRepository;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;


public class MealDetailPresenter implements MealDetailContract.Presenter {

    private final MealRepository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private MealDetailContract.View view;

    public MealDetailPresenter(MealRepository repository) {
        this.repository = repository;
    }


    @Override
    public void attachView(MealDetailContract.View view) {
        this.view = view;
        AppLogger.d("MealDetailPresenter: attachView");
    }

    @Override
    public void detachView() {
        this.view = null;
        disposables.clear();
        AppLogger.d("MealDetailPresenter: detachView — disposables cleared");
    }


    @Override
    public void loadMealDetail(String mealId) {
        if (view == null) return;
        view.showLoading();
        AppLogger.d("MealDetailPresenter: loadMealDetail → " + mealId);

        Disposable disposable = repository.getMealById(mealId)
                .subscribe(
                        meal -> {
                            if (view == null) return;
                            AppLogger.d("MealDetailPresenter: meal loaded → " + meal.getName());
                            view.hideLoading();
                            view.showMealDetail(meal);
                            view.updateFavoriteState(meal.isFavorite());
                        },
                        error -> {
                            if (view == null) return;
                            AppLogger.e("MealDetailPresenter: loadMealDetail error", error);
                            view.hideLoading();
                            view.showError(error.getMessage());
                        }
                );

        disposables.add(disposable);
    }



    @Override
    public void toggleFavorite(Meal meal) {
        if (view == null) return;

        boolean newState = !meal.isFavorite();
        meal.setFavorite(newState);

        AppLogger.d("MealDetailPresenter: toggleFavorite → " + meal.getName()
                + " isFavorite=" + newState);

        view.updateFavoriteState(newState);

        if (newState) {
            view.showFavoriteAdded();
        } else {
            view.showFavoriteRemoved();
        }
    }
}