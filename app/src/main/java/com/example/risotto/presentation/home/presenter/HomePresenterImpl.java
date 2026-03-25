package com.example.risotto.presentation.home.presenter;

import com.example.risotto.core.utils.ErrorMapper;
import com.example.risotto.data.model.Category;
import com.example.risotto.data.model.Meal;
import com.example.risotto.data.repository.meal.MealRepository;
import com.example.risotto.presentation.home.views.HomeView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class HomePresenterImpl implements HomePresenter {

    private final android.content.Context context;
    private final MealRepository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private HomeView view;

    private static Meal cachedMealOfDay;
    private static java.util.List<Category> cachedCategories;
    private static java.util.List<Meal> cachedTopMeals;

    public HomePresenterImpl(android.content.Context context, MealRepository repository) {
        this.context = context;
        this.repository = repository;
    }


    @Override
    public void attachView(HomeView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        disposables.clear();
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
        Disposable disposable = repository.getRandomMeal()
                .flatMap(meal -> repository.cacheMeal(meal).toSingleDefault(meal))
                .onErrorResumeNext(error -> {
                    return repository.getCachedRandomMeal();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meal -> {
                            if (view == null) return;
                            cachedMealOfDay = meal;
                            view.hideLoading();
                            view.showMealOfDay(meal);
                        },
                        error -> {
                            if (view != null) {
                                view.hideLoading();
                                view.showMealOfDayError(ErrorMapper.getErrorMessage(context, error));
                            }
                        }
                );
        disposables.add(disposable);
    }

    @Override
    public void loadCategories() {
        if (view == null) return;

        if (cachedCategories != null && !cachedCategories.isEmpty()) {
            view.showCategories(cachedCategories);
            return;
        }

        Disposable disposable = repository.getCategories()
                .flatMap(categories -> repository.cacheCategories(categories).toSingleDefault(categories))
                .onErrorResumeNext(error -> {
                    return repository.getCachedCategories().firstOrError();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categories -> {
                            if (view == null) return;
                            java.util.List<Category> limited = categories.size() > 6 
                                    ? categories.subList(0, 6) 
                                    : categories;
                            cachedCategories = limited;
                            view.showCategories(limited);
                        },
                        error -> {
                            if (view != null) {
                                view.showCategoriesError(com.example.risotto.core.utils.ErrorMapper.getErrorMessage(context, error));
                            }
                        }
                );
        disposables.add(disposable);
    }

    @Override
    public void loadTopMeals() {
        if (view == null) return;

        if (cachedTopMeals != null && !cachedTopMeals.isEmpty()) {
            view.showTopMeals(cachedTopMeals);
            return;
        }

        Disposable disposable = repository.filterByCategory("Seafood")
                .flatMap(meals -> {
                    return io.reactivex.rxjava3.core.Observable.fromIterable(meals)
                            .flatMapCompletable(meal -> repository.cacheMeal(meal))
                            .toSingleDefault(meals);
                })
                .onErrorResumeNext(error -> {
                    return repository.getCachedTopMeals().firstOrError();
                })
                .map(meals -> meals.size() > 10 ? meals.subList(0, 10) : meals)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> {
                            if (view == null) return;
                            cachedTopMeals = meals;
                            view.showTopMeals(meals);
                        },
                        error -> {
                            if (view != null) {
                                view.showTopMealsError(com.example.risotto.core.utils.ErrorMapper.getErrorMessage(context, error));
                            }
                        }
                );
        disposables.add(disposable);
    }

    @Override
    public void onCategorySelected(Category category) {
    }

    @Override
    public void onMealOfDaySelected(Meal meal) {
    }
}
