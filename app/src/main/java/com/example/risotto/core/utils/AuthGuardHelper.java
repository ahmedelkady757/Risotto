package com.example.risotto.core.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.navigation.Navigation;

import com.example.risotto.R;
import com.example.risotto.RisottoApp;


/**
 * AuthGuardHelper — drop into any fragment that requires a real signed-in user.
 *
 * Usage in onViewCreated():
 *
 *   if (AuthGuardHelper.guardIfGuest(view, root)) return;
 *   // otherwise proceed to load and display content normally
 */
public class AuthGuardHelper {

    /**
     * If the current user is a guest or not logged in, inflates the auth guard
     * overlay into the given container and returns true (caller should return early).
     *
     * @param view       any view to resolve NavController from
     * @param container  the root ViewGroup of the fragment to overlay
     * @return true if the guard was shown (caller must return), false if user is real
     */
    public static boolean guardIfGuest(View view, ViewGroup container) {
        if (RisottoApp.isRealUser()) return false;

        // Inflate the guard view and add it on top of the fragment
        View guard = android.view.LayoutInflater.from(view.getContext())
                .inflate(R.layout.view_auth_guard, container, false);

        // Fill parent using FrameLayout params so it covers everything
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        container.addView(guard, params);

        guard.findViewById(R.id.btn_locked_login).setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.loginFragment));

        guard.findViewById(R.id.btn_locked_signup).setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.registerFragment));

        return true;
    }
}
