package com.example.risotto.presentation.categories.view;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.risotto.R;
import com.example.risotto.core.utils.AppLogger;
import com.example.risotto.data.datasource.local.meal.MealLocalDataSourceImpl;
import com.example.risotto.data.datasource.remote.meal.MealRemoteDataSourceImpl;
import com.example.risotto.data.db.AppDatabase;
import com.example.risotto.data.model.Meal;
import com.example.risotto.data.network.NetworkModule;
import com.example.risotto.data.network.api.MealDBApiService;
import com.example.risotto.data.repository.meal.MealRepositoryImpl;
import com.example.risotto.presentation.categories.presenter.CategoryMealsPresenter;
import com.example.risotto.presentation.categories.presenter.CategoryMealsPresenterImpl;
import com.example.risotto.presentation.search.view.MealAdapter;

import java.util.List;

public class CategoryMealsFragment extends Fragment implements CategoryMealsView {

    private String categoryName;
    private TextView tvCategoryTitle;
    private EditText etSearch;
    private RecyclerView rvMeals;
    private View viewLoading;
    
    private CategoryMealsPresenter presenter;
    private com.example.risotto.presentation.search.view.MealAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryName = getArguments().getString("categoryName");
        }
        initPresenter();
    }

    private void initPresenter() {
        AppDatabase db = AppDatabase.getInstance(requireContext());
        MealDBApiService apiService = NetworkModule.getInstance()
                .getRetrofit().create(MealDBApiService.class);

        presenter = new CategoryMealsPresenterImpl(
                new MealRepositoryImpl(
                        new MealRemoteDataSourceImpl(apiService),
                        new MealLocalDataSourceImpl(db.cachedMealDao(), db.cachedCategoryDao())));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_meals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppLogger.logFragment("CategoryMealsFragment", "onViewCreated: " + categoryName);

        tvCategoryTitle = view.findViewById(R.id.tv_category_title);
        etSearch = view.findViewById(R.id.et_category_search);
        rvMeals = view.findViewById(R.id.rv_category_meals);
        viewLoading = view.findViewById(R.id.view_loading);

        if (categoryName != null) {
            tvCategoryTitle.setText(categoryName);
        }

        setupRecyclerView();
        setupSearchListener();

        presenter.attachView(this);
        if (categoryName != null) {
            presenter.loadMealsByCategory(categoryName);
        }
    }

    private void setupRecyclerView() {
        adapter = new com.example.risotto.presentation.search.view.MealAdapter(meal -> {
            Bundle bundle = new Bundle();
            bundle.putString("mealId", meal.getId());
            Navigation.findNavController(requireView()).navigate(R.id.mealDetailFragment, bundle);
        });
        rvMeals.setAdapter(adapter);
    }

    private void setupSearchListener() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.filterMeals(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            hideKeyboard();
            return true;
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && etSearch != null) {
            imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
        }
    }

    @Override
    public void showMeals(List<Meal> meals) {
        if (adapter != null) {
            adapter.submitList(meals);
        }
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
