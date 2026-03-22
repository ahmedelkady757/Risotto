package com.example.risotto.core.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.navigation.Navigation;

import com.example.risotto.R;
import com.example.risotto.RisottoApp;



public class AuthGuardHelper {


    public static boolean guardIfGuest(View view, ViewGroup container) {
        if (RisottoApp.isRealUser()) return false;

        View guard = android.view.LayoutInflater.from(view.getContext())
                .inflate(R.layout.view_auth_guard, container, false);

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
