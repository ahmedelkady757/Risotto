package com.example.risotto.presentation.auth.presenter;

import com.example.risotto.data.datasource.remote.auth.AuthRemoteDataSource;
import com.example.risotto.data.datasource.remote.auth.AuthRemoteDataSourceImpl;
import com.example.risotto.data.network.services.FirebaseService;
import com.example.risotto.data.network.services.FirebaseServiceImpl;
import com.example.risotto.data.repository.auth.AuthRepository;
import com.example.risotto.data.repository.auth.AuthRepositoryImpl;
import com.example.risotto.presentation.auth.views.SplashView;

public class SplashPresenterImpl implements SplashPresenter {

    private final AuthRepository repository;
    private SplashView view;

    public SplashPresenterImpl(android.content.Context context) {
        FirebaseService service = new FirebaseServiceImpl();
        AuthRemoteDataSource dataSource = new AuthRemoteDataSourceImpl(service);
        this.repository = new AuthRepositoryImpl(dataSource);
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
