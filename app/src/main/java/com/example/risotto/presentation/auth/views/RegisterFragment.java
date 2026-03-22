package com.example.risotto.presentation.auth.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.risotto.R;
import com.example.risotto.data.datasource.remote.auth.AuthRemoteDataSourceImpl;
import com.example.risotto.data.repository.auth.AuthRepositoryImpl;
import com.example.risotto.presentation.auth.presenter.RegisterPresenter;
import com.example.risotto.presentation.auth.presenter.RegisterPresenterImpl;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;


public class RegisterFragment extends Fragment implements RegisterView {

    private RegisterPresenter presenter;

    private TextInputLayout tilUsername;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private TextInputEditText etUsername;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private CircularProgressIndicator progress;
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }

    private void initPresenter() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        AuthRemoteDataSourceImpl remoteDataSource = new AuthRemoteDataSourceImpl(auth);
        AuthRepositoryImpl repository = new AuthRepositoryImpl(remoteDataSource);
        presenter = new RegisterPresenterImpl(repository);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tilUsername = view.findViewById(R.id.til_username);
        tilEmail = view.findViewById(R.id.til_email);
        tilPassword = view.findViewById(R.id.til_password);
        etUsername = view.findViewById(R.id.et_username);
        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        progress = view.findViewById(R.id.progress_register);

        view.findViewById(R.id.btn_register).setOnClickListener(v -> {
            String username = text(etUsername);
            String email = text(etEmail);
            String password = text(etPassword);
            presenter.registerWithEmail(username, email, password);
        });

        view.findViewById(R.id.btn_go_login).setOnClickListener(v ->
                Navigation.findNavController(view).popBackStack());

        presenter.attachView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
        rootView = null;
        tilUsername = null;
        tilEmail = null;
        tilPassword = null;
        etUsername = null;
        etEmail = null;
        etPassword = null;
        progress = null;
    }

    // --- RegisterView Implementation ---

    @Override
    public void showLoading() {
        if (progress != null) progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if (progress != null) progress.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void navigateToHome() {
        if (rootView != null) {
            Navigation.findNavController(rootView).navigate(R.id.action_register_to_home);
        }
    }

    @Override
    public void clearErrors() {
        if (tilUsername != null) tilUsername.setError(null);
        if (tilEmail != null) tilEmail.setError(null);
        if (tilPassword != null) tilPassword.setError(null);
    }

    @Override
    public void showUsernameError(String message) {
        if (tilUsername != null) tilUsername.setError(message);
    }

    @Override
    public void showEmailError(String message) {
        if (tilEmail != null) tilEmail.setError(message);
    }

    @Override
    public void showPasswordError(String message) {
        if (tilPassword != null) tilPassword.setError(message);
    }

    private String text(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }
}
