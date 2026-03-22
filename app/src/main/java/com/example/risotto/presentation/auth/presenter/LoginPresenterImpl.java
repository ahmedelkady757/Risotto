package com.example.risotto.presentation.auth.presenter;

import android.text.TextUtils;
import android.util.Patterns;

import com.example.risotto.core.utils.AppLogger;
import com.example.risotto.data.repository.auth.AuthRepository;
import com.example.risotto.presentation.auth.views.LoginView;
import com.google.firebase.auth.AuthCredential;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginPresenterImpl implements LoginPresenter {

    private final AuthRepository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private LoginView view;

    public LoginPresenterImpl(AuthRepository repository) {
        this.repository = repository;
    }

    @Override
    public void attachView(LoginView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        disposables.clear();
    }

    @Override
    public void loginWithEmail(String email, String password) {
        if (view == null) return;
        
        view.clearErrors();

        boolean hasError = false;
        if (TextUtils.isEmpty(email)) {
            view.showEmailError("Email is required");
            hasError = true;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.showEmailError("Invalid email format");
            hasError = true;
        }

        if (TextUtils.isEmpty(password)) {
            view.showPasswordError("Password is required");
            hasError = true;
        } else if (password.length() < 6) {
            view.showPasswordError("Password must be at least 6 characters");
            hasError = true;
        }

        if (hasError) return;

        view.showLoading();

        Disposable disposable = repository.login(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            if (view != null) {
                                view.hideLoading();
                                view.navigateToHome();
                            }
                        },
                        error -> {
                            if (view != null) {
                                view.hideLoading();
                                view.showError(error.getMessage());
                            }
                        }
                );

        disposables.add(disposable);
    }

    @Override
    public void loginWithGoogle(AuthCredential credential) {
        if (view == null) return;
        view.showLoading();

        Disposable disposable = repository.loginWithGoogle(credential)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            if (view != null) {
                                view.hideLoading();
                                view.navigateToHome();
                            }
                        },
                        error -> {
                            if (view != null) {
                                view.hideLoading();
                                view.showError(error.getMessage());
                            }
                        }
                );

        disposables.add(disposable);
    }

    @Override
    public void loginAsGuest() {
        if (view == null) return;
        view.showLoading();

        Disposable disposable = repository.loginAsGuest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            if (view != null) {
                                view.hideLoading();
                                view.navigateToHome();
                            }
                        },
                        error -> {
                            if (view != null) {
                                view.hideLoading();
                                view.showError(error.getMessage());
                            }
                        }
                );

        disposables.add(disposable);
    }
}
