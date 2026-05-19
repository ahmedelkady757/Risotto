package com.example.risotto.presentation.categories.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.risotto.R;
import com.example.risotto.data.datasource.local.meal.MealLocalDataSourceImpl;
import com.example.risotto.data.datasource.remote.meal.MealRemoteDataSourceImpl;
import com.example.risotto.data.db.AppDatabase;
import com.example.risotto.data.model.Category;
import com.example.risotto.data.network.NetworkModule;
import com.example.risotto.data.network.services.MealDBApiService;
import com.example.risotto.data.repository.meal.MealRepositoryImpl;
import com.example.risotto.presentation.categories.presenter.AllCategoriesPresenter;
import com.example.risotto.presentation.categories.presenter.AllCategoriesPresenterImpl;

import java.util.List;

public class AllCategoriesFragment extends Fragment implements AllCategoriesView {

    private RecyclerView rvCategories;
    private View viewLoading;
    private AllCategoriesPresenter presenter;
    private AllCategoryAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }

    private void initPresenter() {
        AppDatabase db = AppDatabase.getInstance(requireContext());
        MealDBApiService apiService = NetworkModule.getInstance()
                .getRetrofit().create(MealDBApiService.class);

        presenter = new AllCategoriesPresenterImpl(
                new MealRepositoryImpl(
                        new MealRemoteDataSourceImpl(apiService),
                        new MealLocalDataSourceImpl(db.cachedMealDao(), db.cachedCategoryDao())));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvCategories = view.findViewById(R.id.rv_all_categories);
        viewLoading  = view.findViewById(R.id.view_loading);

        com.google.android.material.appbar.MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(
                    v -> Navigation.findNavController(view).navigateUp());
        }

        setupRecyclerView();

        presenter.attachView(this);
        presenter.loadCategories();
    }

    private void setupRecyclerView() {
        adapter = new AllCategoryAdapter(category -> {
            Bundle bundle = new Bundle();
            bundle.putString("categoryName", category.getName());
            Navigation.findNavController(requireView()).navigate(R.id.action_allCategories_to_categoryMeals, bundle);
        });
        rvCategories.setAdapter(adapter);
    }

    @Override
    public void showCategories(List<Category> categories) {
        adapter.submitList(categories);
    }

    @Override
    public void showLoading() {
        viewLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        viewLoading.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}
