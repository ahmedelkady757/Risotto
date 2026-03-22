package com.example.risotto.presentation.mealdetail.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.risotto.presentation.mealdetail.presenter.MealDetailPresenter;
import com.example.risotto.presentation.mealdetail.presenter.MealDetailPresenterImpl;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import com.example.risotto.R;
import com.example.risotto.core.helper.YoutubeHelper;
import com.example.risotto.core.utils.AppLogger;
import com.example.risotto.data.datasource.remote.meal.MealRemoteDataSourceImpl;
import com.example.risotto.data.model.Meal;
import com.example.risotto.data.network.NetworkModule;
import com.example.risotto.data.network.api.MealDBApiService;
import com.example.risotto.data.repository.meal.MealRepositoryImpl;


public class MealDetailFragment extends Fragment implements MealDetailView {

    private static final String TAG_INGREDIENTS = "tag_ingredients";
    private static final String TAG_STEPS = "tag_steps";

    private MealDetailPresenter presenter;

    private Meal currentMeal;

    private View rootView;
    private ProgressBar loadingView;
    private ImageView ivMealDetail;
    private TextView tvMealName;
    private TextView tvArea;
    private TextView tvCategory;
    private TabLayout tabLayout;
    private View containerIngredients;
    private View containerSteps;
    private YouTubePlayerView youTubePlayerView;
    private FloatingActionButton fabFavorite;
    private Toolbar toolbar;

    // Child fragments
    private IngredientsFragment ingredientsFragment;
    private StepsFragment stepsFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppLogger.logFragment("MealDetailFragment", "onCreate");
        initPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_meal_detail, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppLogger.logFragment("MealDetailFragment", "onViewCreated");

        bindViews(view);
        setupToolbar();
        setupTabs();
        attachChildFragments();

        presenter.attachView(this);

        // Read mealId from nav args
        String mealId = null;
        if (getArguments() != null) {
            mealId = getArguments().getString("mealId");
        }

        if (mealId != null) {
            presenter.loadMealDetail(mealId);
        } else {
            showError(getString(R.string.detail_error_load));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
        if (youTubePlayerView != null)
            youTubePlayerView.release();
        rootView = null;
        loadingView = null;
        ivMealDetail = null;
        tvMealName = null;
        tvArea = null;
        tvCategory = null;
        tabLayout = null;
        containerIngredients = null;
        containerSteps = null;
        fabFavorite = null;
        toolbar = null;
        ingredientsFragment = null;
        stepsFragment = null;
        AppLogger.logFragment("MealDetailFragment", "onDestroyView");
    }

    // ── Init ──────────────────────────────────────────────────────────────────

    private void initPresenter() {
        MealDBApiService apiService = NetworkModule.getInstance()
                .getRetrofit().create(MealDBApiService.class);
        MealRemoteDataSourceImpl remoteDataSource = new MealRemoteDataSourceImpl(apiService);
        MealRepositoryImpl repository = new MealRepositoryImpl(remoteDataSource);

        com.example.risotto.data.db.AppDatabase db = com.example.risotto.data.db.AppDatabase.getInstance(requireContext());
        com.example.risotto.data.datasource.local.favorite.FavoriteLocalDataSourceImpl favLocal = 
                new com.example.risotto.data.datasource.local.favorite.FavoriteLocalDataSourceImpl(db.favoriteDao());
        com.example.risotto.data.repository.favorite.FavoriteRepositoryImpl favoriteRepository = 
                new com.example.risotto.data.repository.favorite.FavoriteRepositoryImpl(favLocal);

        presenter = new MealDetailPresenterImpl(repository, favoriteRepository);
    }

    private void bindViews(View view) {
        loadingView = view.findViewById(R.id.view_loading);
        ivMealDetail = view.findViewById(R.id.iv_meal_detail);
        tvMealName = view.findViewById(R.id.tv_detail_meal_name);
        tvArea = view.findViewById(R.id.tv_detail_area);
        tvCategory = view.findViewById(R.id.tv_detail_category);
        tabLayout = view.findViewById(R.id.tab_layout);
        containerIngredients = view.findViewById(R.id.container_ingredients);
        containerSteps = view.findViewById(R.id.container_steps);
        youTubePlayerView = view.findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        fabFavorite = view.findViewById(R.id.fab_favorite);
        toolbar = view.findViewById(R.id.toolbar);
    }

    private void setupToolbar() {
        if (toolbar == null)
            return;
        toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(rootView).navigateUp());
    }

    private void setupTabs() {
        if (tabLayout == null)
            return;

        tabLayout.addTab(tabLayout.newTab().setText(R.string.detail_ingredients));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.detail_steps));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    showIngredientsTab();
                } else {
                    showStepsTab();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void attachChildFragments() {
        ingredientsFragment = (IngredientsFragment) getChildFragmentManager()
                .findFragmentByTag(TAG_INGREDIENTS);
        if (ingredientsFragment == null) {
            ingredientsFragment = new IngredientsFragment();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.container_ingredients, ingredientsFragment, TAG_INGREDIENTS)
                    .commit();
        }

        stepsFragment = (StepsFragment) getChildFragmentManager()
                .findFragmentByTag(TAG_STEPS);
        if (stepsFragment == null) {
            stepsFragment = new StepsFragment();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.container_steps, stepsFragment, TAG_STEPS)
                    .commit();
        }
    }

    // ── Tab switching ─────────────────────────────────────────────────────────

    private void showIngredientsTab() {
        if (containerIngredients != null)
            containerIngredients.setVisibility(View.VISIBLE);
        if (containerSteps != null)
            containerSteps.setVisibility(View.GONE);
    }

    private void showStepsTab() {
        if (containerIngredients != null)
            containerIngredients.setVisibility(View.GONE);
        if (containerSteps != null)
            containerSteps.setVisibility(View.VISIBLE);
    }

    // ── MealDetailView ────────────────────────────────────────────────────────

    @Override
    public void showMealDetail(Meal meal) {
        currentMeal = meal;

        tvMealName.setText(meal.getName());
        tvArea.setText(meal.getArea());
        tvCategory.setText(meal.getCategory());

        Glide.with(requireContext())
                .load(meal.getThumbnailUrl())
                .centerCrop()
                .into(ivMealDetail);

        // Ingredients
        if (ingredientsFragment != null && meal.getIngredients() != null) {
            ingredientsFragment.bindIngredients(meal.getIngredients());
        }

        // Steps
        if (stepsFragment != null && meal.getInstructions() != null) {
            stepsFragment.bindSteps(meal.getInstructions());
        }

        // FAB
        fabFavorite.setOnClickListener(v -> presenter.toggleFavorite(currentMeal));

        // YouTube
        String videoId = YoutubeHelper.extractVideoId(meal.getYoutubeUrl());
        AppLogger.d("MealDetailFragment: YouTube videoId = " + videoId);

        if (videoId != null && !videoId.isEmpty()) {
            youTubePlayerView.setVisibility(View.VISIBLE);
            
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    AppLogger.d("MealDetailFragment: YouTube player ready → " + videoId);
                    youTubePlayer.cueVideo(videoId, 0);
                }
            });
            
        } else {
            youTubePlayerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(String message) {
        if (rootView != null) {
            Snackbar.make(rootView, message != null
                    ? message
                    : getString(R.string.detail_error_load),
                    Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void showLoading() {
        if (loadingView != null)
            loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if (loadingView != null)
            loadingView.setVisibility(View.GONE);
    }

    @Override
    public void updateFavoriteState(boolean isFavorite) {
        if (fabFavorite == null)
            return;
        fabFavorite.setImageResource(isFavorite
                ? R.drawable.ic_favorite_filled
                : R.drawable.ic_favorite_outline);
    }

    @Override
    public void showFavoriteAdded() {
        if (rootView != null)
            Snackbar.make(rootView, R.string.detail_added_favorite, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showFavoriteRemoved() {
        if (rootView != null)
            Snackbar.make(rootView, R.string.detail_removed_favorite, Snackbar.LENGTH_SHORT).show();
    }
}