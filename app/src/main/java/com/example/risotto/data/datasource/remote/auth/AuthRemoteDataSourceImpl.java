package com.example.risotto.data.datasource.remote.auth;

import com.example.risotto.data.network.services.FirebaseService;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class AuthRemoteDataSourceImpl implements AuthRemoteDataSource {

    private final FirebaseService firebaseService;

    public AuthRemoteDataSourceImpl(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @Override
    public Completable loginWithEmail(String email, String password) {
        return firebaseService.signInWithEmail(email, password);
    }

    @Override
    public Completable registerWithEmail(String email, String password, String username) {
        return firebaseService.signUpWithEmail(email, password, username);
    }

    @Override
    public Completable loginWithCredential(AuthCredential credential) {
        return firebaseService.signInWithCredential(credential);
    }

    @Override
    public Completable loginAsGuest() {
        return firebaseService.signInAnonymously();
    }

    @Override
    public void logout() {
        firebaseService.signOut();
    }

    @Override
    public Single<FirebaseUser> getCurrentUser() {
        return firebaseService.getCurrentUser();
    }

    @Override
    public boolean isUserLoggedIn() {
        return firebaseService.isUserLoggedIn();
    }
}
