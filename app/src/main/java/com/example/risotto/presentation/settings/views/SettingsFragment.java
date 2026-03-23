package com.example.risotto.presentation.settings.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.risotto.R;
import com.example.risotto.core.utils.AppLogger;
import com.example.risotto.core.utils.AuthGuardHelper;
import com.example.risotto.presentation.settings.presenter.SettingsPresenter;
import com.example.risotto.presentation.settings.presenter.SettingsPresenterImpl;


public class SettingsFragment extends Fragment implements SettingsView {

    private View rootView;
    private FrameLayout flContainer;
    private FrameLayout flContent;

    private SettingsPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppLogger.logFragment("SettingsFragment", "onCreate");
        presenter = new SettingsPresenterImpl();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppLogger.logFragment("SettingsFragment", "onViewCreated");

        flContainer = view.findViewById(R.id.fl_container);
        flContent = view.findViewById(R.id.fl_content);

        presenter.attachView(this);
        presenter.checkAuthAndUpdate();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.checkAuthAndUpdate();
    }

    @Override
    public void showLocked() {
        if (rootView == null || flContainer == null) return;

        AuthGuardHelper.guardIfGuest(rootView, flContainer);
        if (flContent != null) flContent.setVisibility(View.GONE);
    }

    @Override
    public void showContent() {
        if (flContainer == null) return;

        AuthGuardHelper.removeGuardIfPresent(flContainer);
        if (flContent != null) flContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
        rootView = null;
        flContainer = null;
        flContent = null;
    }
}