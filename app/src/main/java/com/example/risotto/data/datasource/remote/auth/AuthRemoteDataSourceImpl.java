package com.example.risotto.data.datasource.remote.auth;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class AuthRemoteDataSourceImpl implements AuthRemoteDataSource {

    private final FirebaseAuth auth;

    public AuthRemoteDataSourceImpl(FirebaseAuth auth) {
        this.auth = auth;
    }

    @Override
    public Completable loginWithEmail(String email, String password) {
        return Completable.create(emitter -> {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(result -> {
                        if (!emitter.isDisposed()) emitter.onComplete();
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) emitter.onError(e);
                    });
        });
    }

    @Override
    public Completable registerWithEmail(String email, String password, String username) {
        return Completable.create(emitter -> {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(result -> {
                        if (result.getUser() != null) {
                            com.google.firebase.auth.UserProfileChangeRequest profileUpdate = new com.google.firebase.auth.UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();
                            result.getUser().updateProfile(profileUpdate)
                                    .addOnCompleteListener(task -> {
                                        if (!emitter.isDisposed()) emitter.onComplete();
                                    });
                        } else {
                            if (!emitter.isDisposed()) emitter.onComplete();
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) emitter.onError(e);
                    });
        });
    }

    @Override
    public Completable loginWithCredential(AuthCredential credential) {
        return Completable.create(emitter -> {
            auth.signInWithCredential(credential)
                    .addOnSuccessListener(result -> {
                        if (!emitter.isDisposed()) emitter.onComplete();
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) emitter.onError(e);
                    });
        });
    }

    @Override
    public Completable loginAsGuest() {
        return Completable.create(emitter -> {
            auth.signInAnonymously()
                    .addOnSuccessListener(result -> {
                        if (!emitter.isDisposed()) emitter.onComplete();
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) emitter.onError(e);
                    });
        });
    }

    @Override
    public void logout() {
        auth.signOut();
    }

    @Override
    public Single<FirebaseUser> getCurrentUser() {
        return Single.create(emitter -> {
            FirebaseUser user = auth.getCurrentUser();
            if (user != null) {
                if (!emitter.isDisposed()) emitter.onSuccess(user);
            } else {
                if (!emitter.isDisposed()) emitter.onError(new Exception("No user logged in"));
            }
        });
    }

    @Override
    public boolean isUserLoggedIn() {
        return auth.getCurrentUser() != null;
    }
}
