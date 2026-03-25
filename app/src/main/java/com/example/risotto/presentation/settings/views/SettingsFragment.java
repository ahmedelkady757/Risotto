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

    private android.widget.TextView tvUserName;
    private android.widget.TextView tvUserEmail;
    private android.widget.ImageView ivUserPhoto;
    private android.widget.Button btnLogout;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        flContainer = view.findViewById(R.id.fl_container);
        flContent = view.findViewById(R.id.fl_content);
        tvUserName = view.findViewById(R.id.tv_user_name);
        tvUserEmail = view.findViewById(R.id.tv_user_email);
        ivUserPhoto = view.findViewById(R.id.iv_user_photo);
        btnLogout = view.findViewById(R.id.btn_logout);

        btnLogout.setOnClickListener(v -> presenter.logout());

        presenter.attachView(this);
        presenter.checkAuthAndUpdate();
    }

    @Override
    public void showUserProfile(String name, String email, String photoUrl) {
        if (tvUserName != null) tvUserName.setText(name != null ? name : "Real User");
        if (tvUserEmail != null) tvUserEmail.setText(email);
        if (ivUserPhoto != null && photoUrl != null) {
            com.bumptech.glide.Glide.with(this)
                    .load(photoUrl)
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .into(ivUserPhoto);
        }
    }

    @Override
    public void navigateToSplash() {
        androidx.navigation.Navigation.findNavController(requireView())
                .navigate(R.id.loginFragment, null, new androidx.navigation.NavOptions.Builder()
                        .setPopUpTo(R.id.nav_graph, true)
                        .build());
    }

    @Override
    public void showLogoutSuccess() {
        android.widget.Toast.makeText(getContext(), getString(R.string.settings_logout_success), android.widget.Toast.LENGTH_SHORT).show();
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