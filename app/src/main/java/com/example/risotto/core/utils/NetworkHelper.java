package com.example.risotto.core.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

public class NetworkHelper {

    public interface NetworkStatusListener {
        void onNetworkStatusChanged(boolean isConnected);
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        Network activeNetwork = cm.getActiveNetwork();
        if (activeNetwork == null) return false;

        NetworkCapabilities caps = cm.getNetworkCapabilities(activeNetwork);
        return caps != null && (caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) || 
                               caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED));
    }

    public static ConnectivityManager.NetworkCallback registerNetworkCallback(Context context, NetworkStatusListener listener) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return null;

        ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(android.net.Network network) {
                listener.onNetworkStatusChanged(true);
            }

            @Override
            public void onLost(android.net.Network network) {
                // Check if there is still ANY active network
                listener.onNetworkStatusChanged(isConnected(context));
            }
        };

        NetworkRequest request = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();
        
        cm.registerNetworkCallback(request, callback);
        return callback;
    }

    public static void unregisterNetworkCallback(Context context, ConnectivityManager.NetworkCallback callback) {
        if (callback == null) return;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            cm.unregisterNetworkCallback(callback);
        }
    }
}
