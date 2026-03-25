package com.example.risotto.presentation.settings.presenter;

import com.example.risotto.RisottoApp;
import com.example.risotto.presentation.settings.views.SettingsView;

public class SettingsPresenterImpl implements SettingsPresenter {
    private final com.example.risotto.data.repository.auth.AuthRepository authRepository;
    private final io.reactivex.rxjava3.disposables.CompositeDisposable disposables = new io.reactivex.rxjava3.disposables.CompositeDisposable();
    private SettingsView attachedView;

    public SettingsPresenterImpl(com.example.risotto.data.repository.auth.AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public void attachView(SettingsView view) {
        this.attachedView = view;
    }

    @Override
    public void detachView() {
        this.attachedView = null;
        disposables.clear();
    }

    @Override
    public void checkAuthAndUpdate() {
        if (attachedView == null) return;
        if (authRepository.isUserLoggedIn()) {
            attachedView.showContent();
            disposables.add(authRepository.getCurrentUser()
                    .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                    .observeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread())
                    .subscribe(
                            user -> {
                                if (attachedView != null) {
                                    String name = user.getDisplayName();
                                    String email = user.getEmail();
                                    String photoUrl = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : null;
                                    attachedView.showUserProfile(name, email, photoUrl);
                                }
                            },
                            error -> {
                                // Silent failure for profile details if needed, or show locked if really not logged in
                            }
                    ));
        } else {
            attachedView.showLocked();
        }
    }

    @Override
    public void logout() {
        authRepository.logout();
        if (attachedView != null) {
            attachedView.showLogoutSuccess();
            attachedView.navigateToSplash();
        }
    }
}

