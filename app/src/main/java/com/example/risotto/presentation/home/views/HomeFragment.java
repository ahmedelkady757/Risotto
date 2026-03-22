package com.example.risotto.presentation.home.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.risotto.presentation.home.presenter.HomePresenter;
import com.example.risotto.presentation.home.presenter.HomePresenterImpl;
import com.google.android.material.snackbar.Snackbar;

import com.example.risotto.R;
import com.example.risotto.core.utils.AppLogger;
import com.example.risotto.data.datasource.remote.meal.MealRemoteDataSourceImpl;
import com.example.risotto.data.model.Category;
import com.example.risotto.data.model.Meal;
import com.example.risotto.data.network.api.MealDBApiService;
import com.example.risotto.data.network.NetworkModule;
import com.example.risotto.data.repository.meal.MealRepositoryImpl;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;


public class HomeFragment extends Fragment implements HomeView {

    private static final String TAG_MEAL_OF_DAY = "tag_meal_of_day";
    private static final String TAG_CATEGORIES  = "tag_categories";
    private static final String TAG_TOP_MEALS   = "tag_top_meals";

    private HomePresenter presenter;
    private MealOfDayFragment mealOfDayFragment;
    private CategoriesFragment categoriesFragment;
    private TopMealsFragment topMealsFragment;
    private View loadingView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppLogger.logFragment("HomeFragment", "onCreate");
        initPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppLogger.logFragment("HomeFragment", "onViewCreated");

        loadingView = view.findViewById(R.id.view_loading);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.refreshData();
        });

        view.findViewById(R.id.search_bar_container).setOnClickListener(v -> {
            AppLogger.logNav("HomeFragment -> SearchFragment via search bar");
            androidx.navigation.NavController navController =
                    Navigation.findNavController(view);
            navController.navigate(
                    R.id.searchFragment,
                    null,
                    new androidx.navigation.NavOptions.Builder()
                            .setPopUpTo(navController.getGraph().getStartDestinationId(), false)
                            .setLaunchSingleTop(true)
                            .build()
            );
        });

        view.findViewById(R.id.btn_see_all_categories).setOnClickListener(v -> {
            AppLogger.logNav("HomeFragment -> CategoriesFragment (coming sprint)");
        });

        attachChildFragments();

        presenter.attachView(this);
        presenter.loadMealOfDay();
        presenter.loadCategories();
        presenter.loadTopMeals();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
        loadingView        = null;
        rootView           = null;
        mealOfDayFragment  = null;
        categoriesFragment = null;
        topMealsFragment   = null;
        AppLogger.logFragment("HomeFragment", "onDestroyView");
    }


    private void initPresenter() {
        MealDBApiService apiService = NetworkModule.getInstance()
                .getRetrofit()
                .create(MealDBApiService.class);
        MealRemoteDataSourceImpl remoteDataSource = new MealRemoteDataSourceImpl(apiService);

        com.example.risotto.data.db.AppDatabase db = com.example.risotto.data.db.AppDatabase.getInstance(requireContext());
        com.example.risotto.data.datasource.local.meal.MealLocalDataSourceImpl mealLocal =
                new com.example.risotto.data.datasource.local.meal.MealLocalDataSourceImpl(db.cachedMealDao(), db.cachedCategoryDao());

        MealRepositoryImpl repository = new MealRepositoryImpl(remoteDataSource, mealLocal);
        presenter = new HomePresenterImpl(repository);
    }


    private void attachChildFragments() {
        mealOfDayFragment = (MealOfDayFragment) getChildFragmentManager()
                .findFragmentByTag(TAG_MEAL_OF_DAY);
        if (mealOfDayFragment == null) {
            mealOfDayFragment = new MealOfDayFragment();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.container_meal_of_day, mealOfDayFragment, TAG_MEAL_OF_DAY)
                    .commit();
        }

        categoriesFragment = (CategoriesFragment) getChildFragmentManager()
                .findFragmentByTag(TAG_CATEGORIES);
        if (categoriesFragment == null) {
            categoriesFragment = new CategoriesFragment();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.container_categories, categoriesFragment, TAG_CATEGORIES)
                    .commit();
        }

        topMealsFragment = (TopMealsFragment) getChildFragmentManager()
                .findFragmentByTag(TAG_TOP_MEALS);
        if (topMealsFragment == null) {
            topMealsFragment = new TopMealsFragment();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.container_top_meals, topMealsFragment, TAG_TOP_MEALS)
                    .commit();
        }
    }


    // ── HomeView ──────────────────────────────────────────────────────────────

    @Override
    public void showMealOfDay(Meal meal) {
        if (mealOfDayFragment == null) return;
        mealOfDayFragment.bindMeal(meal, this::navigateToDetail);
    }

    @Override
    public void showMealOfDayError(String message) {
        AppLogger.e("HomeFragment: meal of day error -> " + message);
        showSnackbar(getString(R.string.home_error_meal_of_day));
    }

    @Override
    public void showCategories(List<Category> categories) {
        if (categoriesFragment == null) return;
        categoriesFragment.bindCategories(categories, category -> {
            presenter.onCategorySelected(category);
        });
    }

    @Override
    public void showCategoriesError(String message) {
        AppLogger.e("HomeFragment: categories error -> " + message);
        showSnackbar(getString(R.string.home_error_categories));
    }

    @Override
    public void showTopMeals(List<Meal> topMeals) {
        if (topMealsFragment == null) return;
        topMealsFragment.bindTopMeals(topMeals, this::navigateToDetail);
    }

    @Override
    public void showTopMealsError(String message) {
        AppLogger.e("HomeFragment: top meals error -> " + message);
    }

    @Override
    public void showLoading() {
        if (loadingView != null) loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if (loadingView != null) loadingView.setVisibility(View.GONE);
    }
    
    @Override
    public void hideRefresh() {
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    private void navigateToDetail(Meal meal) {
        if (meal == null || rootView == null) return;
        AppLogger.logNav("HomeFragment -> MealDetail: " + meal.getId());
        Bundle args = new Bundle();
        args.putString("mealId", meal.getId());
        args.putString("mealName", meal.getName());
        Navigation.findNavController(rootView)
                .navigate(R.id.action_home_to_mealDetail, args);
    }


    private void showSnackbar(String message) {
        if (rootView != null) {
            Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
        }
    }
}
