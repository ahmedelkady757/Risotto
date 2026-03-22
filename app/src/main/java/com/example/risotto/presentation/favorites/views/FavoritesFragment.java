package com.example.risotto.presentation.favorites.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.risotto.R;
import com.example.risotto.data.datasource.local.favorite.FavoriteLocalDataSourceImpl;
import com.example.risotto.data.db.AppDatabase;
import com.example.risotto.data.db.dao.FavoriteDao;
import com.example.risotto.data.model.Meal;
import com.example.risotto.data.repository.favorite.FavoriteRepositoryImpl;
import com.example.risotto.presentation.favorites.adapters.FavoritesAdapter;
import com.example.risotto.presentation.favorites.presenter.FavoritesPresenter;
import com.example.risotto.presentation.favorites.presenter.FavoritesPresenterImpl;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class FavoritesFragment extends Fragment implements FavoritesView, FavoritesAdapter.OnFavoriteMealClickListener {

    private FavoritesPresenter presenter;
    private FavoritesAdapter adapter;

    private RecyclerView rvFavorites;
    private ProgressBar progressBar;
    private LinearLayout layoutEmptyState;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }

    private void initPresenter() {
        // Setting up our single-source-of-truth manual Dependency Injection
        AppDatabase db = AppDatabase.getInstance(requireContext());
        FavoriteDao dao = db.favoriteDao();
        FavoriteLocalDataSourceImpl localDataSource = new FavoriteLocalDataSourceImpl(dao);
        FavoriteRepositoryImpl repository = new FavoriteRepositoryImpl(localDataSource);
        presenter = new FavoritesPresenterImpl(repository);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initViews(view);
        presenter.attachView(this);
        presenter.loadFavorites();
    }

    private void initViews(View view) {
        rvFavorites = view.findViewById(R.id.rv_favorites);
        progressBar = view.findViewById(R.id.progress_bar);
        layoutEmptyState = view.findViewById(R.id.layout_empty_state);

        adapter = new FavoritesAdapter(this);
        rvFavorites.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        rvFavorites.setAdapter(adapter);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        rvFavorites.setVisibility(View.GONE);
        layoutEmptyState.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showFavorites(List<Meal> meals) {
        rvFavorites.setVisibility(View.VISIBLE);
        layoutEmptyState.setVisibility(View.GONE);
        adapter.submitList(meals);
    }

    @Override
    public void showEmptyState() {
        rvFavorites.setVisibility(View.GONE);
        layoutEmptyState.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String error) {
        if (getView() != null) {
            Snackbar.make(getView(), error, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMealClick(Meal meal) {
        // Navigate to Meal Detail
        Bundle bundle = new Bundle();
        bundle.putString("MEAL_ID", meal.getId());
        Navigation.findNavController(requireView()).navigate(R.id.mealDetailFragment, bundle);
    }

    @Override
    public void onFavoriteToggleClick(Meal meal) {
        // We are on favorites screen, so clicking the toggle implies removing it.
        presenter.removeFavorite(meal);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}
