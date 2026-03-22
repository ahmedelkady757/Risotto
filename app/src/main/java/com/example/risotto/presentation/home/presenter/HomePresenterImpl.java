package com.example.risotto.presentation.home.presenter;

import com.example.risotto.core.utils.AppLogger;
import com.example.risotto.data.model.Category;
import com.example.risotto.data.model.Meal;
import com.example.risotto.data.repository.meal.MealRepository;
import com.example.risotto.presentation.home.views.HomeView;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;


public class HomePresenterImpl implements HomePresenter {

    private final MealRepository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private HomeView view;

    private static Meal cachedMealOfDay;
    private static java.util.List<Category> cachedCategories;
    private static java.util.List<Meal> cachedTopMeals;

    public HomePresenterImpl(MealRepository repository) {
        this.repository = repository;
    }


    @Override
    public void attachView(HomeView view) {
        this.view = view;
        AppLogger.d("HomePresenter: attachView");
    }

    @Override
    public void detachView() {
        this.view = null;
        disposables.clear();
        AppLogger.d("HomePresenter: detachView — disposables cleared");
    }


    @Override
    public void refreshData() {
        cachedMealOfDay = null;
        cachedCategories = null;
        cachedTopMeals = null;

        loadMealOfDay();
        loadCategories();
        loadTopMeals();
        
        if (view != null) {
            view.hideRefresh();
        }
    }

    @Override
    public void loadMealOfDay() {
        if (view == null) return;
        
        if (cachedMealOfDay != null) {
            view.showMealOfDay(cachedMealOfDay);
            return;
        }

        view.showLoading();
        AppLogger.d("HomePresenter: loadMealOfDay");

        Disposable disposable = repository.getRandomMeal()
                .subscribe(
                        meal -> {
                            cachedMealOfDay = meal;
                            if (view == null) return;
                            AppLogger.d("HomePresenter: meal of day loaded → " + meal.getName());
                            view.hideLoading();
                            view.showMealOfDay(meal);
                        },
                        error -> {
                            if (view == null) return;
                            AppLogger.e("HomePresenter: loadMealOfDay error", error);
                            view.hideLoading();
                            view.showMealOfDayError(error.getMessage());
                        }
                );

        disposables.add(disposable);
    }


    @Override
    public void loadCategories() {
        if (view == null) return;

        if (cachedCategories != null) {
            view.showCategories(cachedCategories);
            return;
        }

        AppLogger.d("HomePresenter: loadCategories");

        Disposable disposable = repository.getCategories()
                .subscribe(
                        categories -> {
                            cachedCategories = categories;
                            if (view == null) return;
                            AppLogger.d("HomePresenter: categories loaded → " + categories.size());
                            view.showCategories(categories);
                        },
                        error -> {
                            if (view == null) return;
                            AppLogger.e("HomePresenter: loadCategories error", error);
                            view.showCategoriesError(error.getMessage());
                        }
                );

        disposables.add(disposable);
    }

    @Override
    public void loadTopMeals() {
        if (view == null) return;

        if (cachedTopMeals != null) {
            view.showTopMeals(cachedTopMeals);
            return;
        }

        AppLogger.d("HomePresenter: loadTopMeals");

        Disposable disposable = repository.filterByCategory("Seafood")
                .map(meals -> meals.size() > 10 ? meals.subList(0, 10) : meals)
                .subscribe(
                        meals -> {
                            cachedTopMeals = meals;
                            if (view == null) return;
                            AppLogger.d("HomePresenter: top meals loaded → " + meals.size());
                            view.showTopMeals(meals);
                        },
                        error -> {
                            if (view == null) return;
                            AppLogger.e("HomePresenter: loadTopMeals error", error);
                            view.showTopMealsError(error.getMessage());
                        }
                );

        disposables.add(disposable);
    }

    @Override
    public void onCategorySelected(Category category) {
        AppLogger.d("HomePresenter: category selected → " + category.getName());
    }

    @Override
    public void onMealOfDaySelected(Meal meal) {
        AppLogger.d("HomePresenter: meal of day selected → " + meal.getName());
    }
}
