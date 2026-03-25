package com.example.risotto.presentation.auth.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.airbnb.lottie.LottieAnimationView;
import com.example.risotto.R;
import com.example.risotto.data.datasource.remote.auth.AuthRemoteDataSourceImpl;
import com.example.risotto.data.repository.auth.AuthRepositoryImpl;
import com.example.risotto.presentation.auth.presenter.SplashPresenter;
import com.example.risotto.presentation.auth.presenter.SplashPresenterImpl;
import com.google.firebase.auth.FirebaseAuth;


public class SplashFragment extends Fragment implements SplashView {

    private SplashPresenter presenter;
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }
    
    private void initPresenter() {
        presenter = new SplashPresenterImpl(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_splash, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);

        // Fade in app name + tagline
        view.findViewById(R.id.tv_app_name).animate().alpha(1f).setStartDelay(400).setDuration(600).start();
        view.findViewById(R.id.tv_app_tagline).animate().alpha(1f).setStartDelay(600).setDuration(600).start();

        LottieAnimationView lottie = view.findViewById(R.id.lottie_splash);
        lottie.addAnimatorListener(new android.animation.Animator.AnimatorListener() {
            @Override public void onAnimationStart(android.animation.Animator a) {}
            @Override public void onAnimationCancel(android.animation.Animator a) {}
            @Override public void onAnimationRepeat(android.animation.Animator a) {}

            @Override
            public void onAnimationEnd(android.animation.Animator a) {
                if (!isAdded() || getView() == null) return;
                presenter.decideNextScreen();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
        rootView = null;
    }

    // --- SplashView Implementation ---

    @Override
    public void navigateToLogin() {
        if (rootView != null) {
            Navigation.findNavController(rootView)
                    .navigate(R.id.action_splash_to_login);
        }
    }

    @Override
    public void navigateToHome() {
        if (rootView != null) {
            Navigation.findNavController(rootView)
                    .navigate(R.id.action_splash_to_home);
        }
    }
}
