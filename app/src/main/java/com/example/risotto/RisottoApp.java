package com.example.risotto;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RisottoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static boolean isRealUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null && !user.isAnonymous();
    }
}
