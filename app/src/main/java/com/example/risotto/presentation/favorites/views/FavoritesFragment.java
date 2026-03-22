package com.example.risotto.presentation.favorites.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.risotto.R;
import com.example.risotto.core.utils.AppLogger;
import com.example.risotto.core.utils.AuthGuardHelper;


public class FavoritesFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppLogger.logFragment("FavoritesFragment", "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppLogger.logFragment("FavoritesFragment", "onViewCreated");

        if (AuthGuardHelper.guardIfGuest(view, (ViewGroup) view)) return;

        loadFavorites();
    }

    private void loadFavorites() {
        AppLogger.d("FavoritesFragment: loadFavorites (real user)");
    }
}
