package com.example.risotto.core.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;


public final class NetworkUtils {

    private NetworkUtils() { }


    public static boolean isConnected(Context context) {
        if (context == null) {
            AppLogger.w("NetworkUtils.isConnected: context is null — returning false");
            return false;
        }

        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) {
            AppLogger.w("NetworkUtils.isConnected: ConnectivityManager is null");
            return false;
        }

        Network activeNetwork = cm.getActiveNetwork();
        if (activeNetwork == null) {
            AppLogger.d("NetworkUtils: no active network");
            return false;
        }

        NetworkCapabilities capabilities = cm.getNetworkCapabilities(activeNetwork);
        if (capabilities == null) {
            AppLogger.d("NetworkUtils: no network capabilities");
            return false;
        }

        boolean connected =
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);

        AppLogger.d("NetworkUtils: isConnected = " + connected);
        return connected;
    }


    public static boolean isWifi(Context context) {
        if (context == null) return false;

        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) return false;

        Network activeNetwork = cm.getActiveNetwork();
        if (activeNetwork == null) return false;

        NetworkCapabilities capabilities = cm.getNetworkCapabilities(activeNetwork);
        if (capabilities == null) return false;

        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
    }
}