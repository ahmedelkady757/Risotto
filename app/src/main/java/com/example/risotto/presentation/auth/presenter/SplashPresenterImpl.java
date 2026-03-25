package com.example.risotto.presentation.auth.presenter;

import com.example.risotto.data.repository.auth.AuthRepository;
import com.example.risotto.presentation.auth.views.SplashView;

public class SplashPresenterImpl implements SplashPresenter {

    private final AuthRepository repository;
    private SplashView view;

    public SplashPresenterImpl(android.content.Context context) {
        com.example.risotto.data.network.services.FirebaseService service = new com.example.risotto.data.network.services.FirebaseServiceImpl();
        com.example.risotto.data.datasource.remote.auth.AuthRemoteDataSource dataSource = new com.example.risotto.data.datasource.remote.auth.AuthRemoteDataSourceImpl(service);
        this.repository = new com.example.risotto.data.repository.auth.AuthRepositoryImpl(dataSource);
    }

    @Override
    public void attachView(SplashView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void decideNextScreen() {
        if (view == null) return;
        
        if (repository.isUserLoggedIn()) {
            view.navigateToHome();
        } else {
            view.navigateToLogin();
        }
    }
}
