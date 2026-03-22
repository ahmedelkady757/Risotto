package com.example.risotto.core.utils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ErrorMapper {
    public static String getErrorMessage(Throwable throwable) {
        if (throwable instanceof UnknownHostException || throwable instanceof ConnectException) {
            return "No internet connection limit please check your network.";
        } else if (throwable instanceof SocketTimeoutException) {
            return "Connection timed out. Please try again.";
        }
        return "An unexpected error occurred: " + throwable.getMessage();
    }
}
