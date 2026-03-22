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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * Splash screen — plays a Lottie animation, then checks Firebase auth state.
 *
 * Navigation outcomes:
 *   • No user at all           → loginFragment
 *   • Anonymous user (guest)   → homeFragment  (limited access — auth guard in individual fragments)
 *   • Real user (email/Google) → homeFragment  (full access)
 */
public class SplashFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                navigateBasedOnAuthState(getView());
            }
        });
    }

    private void navigateBasedOnAuthState(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            // Never logged in → show Login
            Navigation.findNavController(view)
                    .navigate(R.id.action_splash_to_login);
        } else {
            // Either anonymous or real user → go straight to Home
            // (individual fragments handle their own auth guard)
            Navigation.findNavController(view)
                    .navigate(R.id.action_splash_to_home);
        }
    }
}
