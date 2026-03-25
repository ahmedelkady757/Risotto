package com.example.risotto.core.utils;

import android.content.Context;
import com.example.risotto.R;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ErrorMapper {

    public static String getErrorMessage(Context context, Throwable throwable) {
        if (throwable instanceof UnknownHostException || 
            throwable instanceof ConnectException || 
            throwable instanceof FirebaseNetworkException) {
            return context.getString(R.string.error_network);
        } else if (throwable instanceof SocketTimeoutException) {
            return context.getString(R.string.error_timeout);
        } else if (throwable instanceof FirebaseAuthInvalidCredentialsException ||
                   throwable instanceof FirebaseAuthInvalidUserException) {
            return context.getString(R.string.error_auth_invalid_credentials);
        } else if (throwable instanceof FirebaseAuthUserCollisionException) {
            return context.getString(R.string.error_auth_user_exists);
        } else if (throwable instanceof FirebaseAuthWeakPasswordException) {
            return context.getString(R.string.error_auth_weak_password);
        }
        
        return context.getString(R.string.error_auth_generic);
    }
}
