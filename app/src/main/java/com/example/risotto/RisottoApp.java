package com.example.risotto;

import android.app.Application;

import com.example.risotto.core.utils.AppLogger;

public class RisottoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppLogger.i("RisottoApp: application started");
    }
}