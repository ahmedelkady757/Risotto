package com.example.risotto.presentation.search.views;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
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
import com.example.risotto.data.model.Meal;
import com.example.risotto.data.network.NetworkModule;
import com.example.risotto.data.network.services.MealDBApiService;
import com.example.risotto.data.repository.meal.MealRepositoryImpl;
import com.example.risotto.presentation.search.presenter.SearchPresenter;
import com.example.risotto.presentation.search.presenter.SearchPresenterImpl;

import java.util.List;

public class SearchFragment extends Fragment implements MealSearchView {

    private EditText etSearch;
    private RecyclerView rvResults;
    private View viewEmptyState;
    private View viewNoResults;
    private TextView tvEmptyMessage;
    private View viewLoading;

    private SearchPresenter presenter;
    private MealAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }

    private void initPresenter() {
        AppDatabase db = AppDatabase.getInstance(requireContext());
        MealDBApiService apiService = NetworkModule.getInstance()
                .getRetrofit().create(MealDBApiService.class);

        presenter = new SearchPresenterImpl(
                requireContext(),
                new MealRepositoryImpl(
                        new MealRemoteDataSourceImpl(apiService),
                        new MealLocalDataSourceImpl(db.cachedMealDao(), db.cachedCategoryDao())));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etSearch = view.findViewById(R.id.et_search);
        rvResults = view.findViewById(R.id.rv_search_results);
        viewEmptyState = view.findViewById(R.id.view_empty_state);
        viewNoResults = view.findViewById(R.id.view_no_results);
        tvEmptyMessage = view.findViewById(R.id.tv_empty_message);
        viewLoading = view.findViewById(R.id.view_loading);

        setupRecyclerView();
        setupSearchListener();
        presenter.attachView(this);
    }

    private void setupRecyclerView() {
        rvResults.setLayoutManager(new androidx.recyclerview.widget.GridLayoutManager(requireContext(), 2));
        adapter = new MealAdapter(meal -> {
            Bundle bundle = new Bundle();
            bundle.putString("mealId", meal.getId());
            androidx.navigation.Navigation.findNavController(requireView()).navigate(R.id.mealDetailFragment, bundle);
        });
        rvResults.setAdapter(adapter);
    }

    private void setupSearchListener() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.search(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard();
                return true;
            }
            return false;
        });
    }

    private void hideKeyboard() {
        android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) requireContext()
                .getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
        if (imm != null && etSearch != null) {
            imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
        }
    }

    @Override
    public void showResults(List<Meal> meals) {
        adapter.submitList(meals);
        if (meals.isEmpty()) {
            rvResults.setVisibility(View.GONE);
            viewNoResults.setVisibility(View.VISIBLE);
        } else {
            rvResults.setVisibility(View.VISIBLE);
            viewNoResults.setVisibility(View.GONE);
            hideEmptyState();
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
    public void showEmptyState(String message) {
        if (message.contains("typing")) {
            viewEmptyState.setVisibility(View.VISIBLE);
            viewNoResults.setVisibility(View.GONE);
        } else {
            viewEmptyState.setVisibility(View.GONE);
            viewNoResults.setVisibility(View.VISIBLE);
        }
        tvEmptyMessage.setText(message);
        rvResults.setVisibility(View.GONE);
    }

    @Override
    public void hideEmptyState() {
        viewEmptyState.setVisibility(View.GONE);
        viewNoResults.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}