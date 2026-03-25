package com.example.risotto.presentation.auth.presenter;

import com.example.risotto.R;
import com.example.risotto.core.utils.AuthValidator;
import com.example.risotto.core.utils.ErrorMapper;
import com.example.risotto.data.repository.auth.AuthRepository;
import com.example.risotto.presentation.auth.views.RegisterView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterPresenterImpl implements RegisterPresenter {

    private final android.content.Context context;
    private final AuthRepository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private RegisterView view;

    public RegisterPresenterImpl(android.content.Context context) {
        this.context = context;
        com.example.risotto.data.network.services.FirebaseService service = new com.example.risotto.data.network.services.FirebaseServiceImpl();
        com.example.risotto.data.datasource.remote.auth.AuthRemoteDataSource dataSource = new com.example.risotto.data.datasource.remote.auth.AuthRemoteDataSourceImpl(service);
        this.repository = new com.example.risotto.data.repository.auth.AuthRepositoryImpl(dataSource);
    }

    @Override
    public void attachView(RegisterView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        disposables.clear();
    }

    @Override
    public void registerWithEmail(String username, String email, String password) {
        if (view == null) return;
        
        view.clearErrors();

        boolean hasError = false;
        
        if (!AuthValidator.isNotEmpty(username)) {
            view.showUsernameError(context.getString(R.string.auth_error_username_required));
            hasError = true;
        }
        
        if (!AuthValidator.isNotEmpty(email)) {
            view.showEmailError(context.getString(R.string.auth_error_email_required));
            hasError = true;
        } else if (!AuthValidator.isValidEmail(email)) {
            view.showEmailError(context.getString(R.string.auth_error_invalid_email));
            hasError = true;
        }

        if (!AuthValidator.isNotEmpty(password)) {
            view.showPasswordError(context.getString(R.string.auth_error_password_required));
            hasError = true;
        } else if (!AuthValidator.isValidPassword(password)) {
            view.showPasswordError(context.getString(R.string.auth_error_short_password));
            hasError = true;
        }

        if (hasError) return;

        view.showLoading();

        Disposable disposable = repository.register(email, password, username)
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
                                view.showError(ErrorMapper.getErrorMessage(context, error));
                            }
                        }
                );

        disposables.add(disposable);
    }
}
