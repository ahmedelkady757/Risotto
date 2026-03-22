package com.example.risotto;

import android.app.Application;

import com.example.risotto.core.utils.AppLogger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RisottoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppLogger.i("RisottoApp: application started");
    }


    public static String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : "";
    }


    public static boolean isGuestUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user == null || user.isAnonymous();
    }


    public static boolean isRealUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null && !user.isAnonymous();
    }
}
