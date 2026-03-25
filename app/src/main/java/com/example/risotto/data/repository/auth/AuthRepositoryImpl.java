package com.example.risotto.data.repository.auth;

import com.example.risotto.data.datasource.remote.auth.AuthRemoteDataSource;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class AuthRepositoryImpl implements AuthRepository {

    private final AuthRemoteDataSource remoteDataSource;

    public AuthRepositoryImpl(AuthRemoteDataSource remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    @Override
    public Completable login(String email, String password) {
        return remoteDataSource.loginWithEmail(email, password);
    }

    @Override
    public Completable register(String email, String password, String username) {
        return remoteDataSource.registerWithEmail(email, password, username);
    }

    @Override
    public Completable loginWithGoogle(AuthCredential credential) {
        return remoteDataSource.loginWithCredential(credential);
    }

    @Override
    public Completable loginAsGuest() {
        return remoteDataSource.loginAsGuest();
    }

    @Override
    public void logout() {
        remoteDataSource.logout();
    }

    @Override
    public Single<FirebaseUser> getCurrentUser() {
        return remoteDataSource.getCurrentUser();
    }

    @Override
    public boolean isUserLoggedIn() {
        return remoteDataSource.isUserLoggedIn();
    }
}
