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
import com.example.risotto.RisottoApp;
import com.example.risotto.core.ui.dialogs.CustomConfirmDialog;
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
    private android.widget.FrameLayout flContainer;
    private View toolbarView;
    private android.widget.ImageButton btnClearAll;
    private boolean viewsInitialized = false;

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
        flContainer = view.findViewById(R.id.fl_container);
        toolbarView = view.findViewById(R.id.toolbar);

        if (!RisottoApp.isRealUser()) {
            // Guest: show inline auth guard prompt and skip loading favorites.
            com.example.risotto.core.utils.AuthGuardHelper.guardIfGuest(view, flContainer);
            if (toolbarView != null) toolbarView.setVisibility(View.GONE);
            return;
        }

        // Real user: show favorites immediately.
        if (!viewsInitialized) {
            initViews(view);
            viewsInitialized = true;
        }
        presenter.attachView(this);
        presenter.loadFavorites();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getView() == null) return;

        if (flContainer == null) flContainer = getView().findViewById(R.id.fl_container);
        if (toolbarView == null) toolbarView = getView().findViewById(R.id.toolbar);

        if (!RisottoApp.isRealUser()) {
            // Still guest: ensure guard is visible.
            com.example.risotto.core.utils.AuthGuardHelper.guardIfGuest(getView(), flContainer);
            if (toolbarView != null) toolbarView.setVisibility(View.GONE);
            return;
        }

        // Auth is now real: remove inline guard overlay and load favorites.
        com.example.risotto.core.utils.AuthGuardHelper.removeGuardIfPresent(flContainer);
        if (toolbarView != null) toolbarView.setVisibility(View.VISIBLE);

        if (!viewsInitialized) {
            initViews(getView());
            viewsInitialized = true;
        }

        presenter.attachView(this);
        presenter.loadFavorites();
    }

    private void initViews(View view) {
        rvFavorites = view.findViewById(R.id.rv_favorites);
        progressBar = view.findViewById(R.id.progress_bar);
        layoutEmptyState = view.findViewById(R.id.layout_empty_state);
        btnClearAll = view.findViewById(R.id.btn_clear_all);

        if (btnClearAll != null) {
            btnClearAll.setOnClickListener(v -> showClearAllConfirmation());
        }

        adapter = new FavoritesAdapter(this);
        rvFavorites.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        rvFavorites.setAdapter(adapter);
    }

    private void showClearAllConfirmation() {
        CustomConfirmDialog.newInstance(
                getString(R.string.dialog_confirm_clear_all_title),
                getString(R.string.dialog_confirm_clear_all_message),
                () -> presenter.clearAllFavorites()
        ).show(getChildFragmentManager(), "clear_all_dialog");
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        rvFavorites.setVisibility(View.GONE);
        layoutEmptyState.setVisibility(View.GONE);
        if (btnClearAll != null) btnClearAll.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showFavorites(List<Meal> meals) {
        rvFavorites.setVisibility(View.VISIBLE);
        layoutEmptyState.setVisibility(View.GONE);
        if (btnClearAll != null) btnClearAll.setVisibility(View.VISIBLE);
        adapter.submitList(meals);
    }

    @Override
    public void showEmptyState() {
        rvFavorites.setVisibility(View.GONE);
        layoutEmptyState.setVisibility(View.VISIBLE);
        if (btnClearAll != null) btnClearAll.setVisibility(View.GONE);
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
        bundle.putString("mealId", meal.getId());
        Navigation.findNavController(requireView()).navigate(R.id.mealDetailFragment, bundle);
    }

    @Override
    public void onFavoriteToggleClick(Meal meal) {
        CustomConfirmDialog.newInstance(
                getString(R.string.dialog_confirm_remove_favorite_title),
                getString(R.string.dialog_confirm_remove_favorite_message, meal.getName()),
                () -> presenter.removeFavorite(meal)
        ).show(getChildFragmentManager(), "remove_fav_dialog");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();

        // Reset view references; fragment may be recreated later.
        adapter = null;
        rvFavorites = null;
        progressBar = null;
        layoutEmptyState = null;
        flContainer = null;
        toolbarView = null;
        btnClearAll = null;
        viewsInitialized = false;
    }
}
