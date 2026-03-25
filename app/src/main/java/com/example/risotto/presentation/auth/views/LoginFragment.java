package com.example.risotto.presentation.auth.views;

import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.risotto.R;
import com.example.risotto.data.datasource.remote.auth.AuthRemoteDataSourceImpl;
import com.example.risotto.data.repository.auth.AuthRepositoryImpl;
import com.example.risotto.presentation.auth.presenter.LoginPresenter;
import com.example.risotto.presentation.auth.presenter.LoginPresenterImpl;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;


public class LoginFragment extends Fragment implements LoginView {

    private LoginPresenter presenter;
    private CredentialManager credentialManager;

    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
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
        presenter = new LoginPresenterImpl(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        credentialManager = CredentialManager.create(requireContext());

        tilEmail = view.findViewById(R.id.til_email);
        tilPassword = view.findViewById(R.id.til_password);
        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        progress = view.findViewById(R.id.progress_login);

        view.findViewById(R.id.btn_login).setOnClickListener(v -> {
            String email = text(etEmail);
            String password = text(etPassword);
            presenter.loginWithEmail(email, password);
        });

        view.findViewById(R.id.btn_google).setOnClickListener(v -> presenter.onGoogleSignInClicked());
        view.findViewById(R.id.btn_guest).setOnClickListener(v -> presenter.loginAsGuest());
        view.findViewById(R.id.btn_go_register).setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_login_to_register));

        presenter.attachView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
        rootView = null;
        tilEmail = null;
        tilPassword = null;
        etEmail = null;
        etPassword = null;
        progress = null;
    }

    private void handleGoogleSignIn() {
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(getString(R.string.default_web_client_id))
                .setAutoSelectEnabled(true)
                .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        credentialManager.getCredentialAsync(
                requireActivity(),
                request,
                new CancellationSignal(),
                ContextCompat.getMainExecutor(requireContext()),
                new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse result) {
                        try {
                            Credential credential = result.getCredential();
                            if (credential instanceof CustomCredential &&
                                    credential.getType().equals(GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {

                                GoogleIdTokenCredential googleId = GoogleIdTokenCredential.createFrom(credential.getData());
                                AuthCredential authCredential = GoogleAuthProvider.getCredential(googleId.getIdToken(), null);
                                presenter.loginWithGoogle(authCredential);
                            } else {
                                showError(getString(R.string.auth_error_unexpected));
                            }
                        } catch (Exception e) {
                            showError(e.getMessage());
                        }
                    }

                    @Override
                    public void onError(GetCredentialException e) {
                        showError(getString(R.string.auth_error_google_failed));
                    }
                }
        );
    }

    // --- LoginView Implementation ---

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
            Navigation.findNavController(rootView).navigate(R.id.action_login_to_home);
        }
    }

    @Override
    public void clearErrors() {
        if (tilEmail != null) tilEmail.setError(null);
        if (tilPassword != null) tilPassword.setError(null);
    }

    @Override
    public void showEmailError(String message) {
        if (tilEmail != null) tilEmail.setError(message);
    }

    @Override
    public void showPasswordError(String message) {
        if (tilPassword != null) tilPassword.setError(message);
    }

    @Override
    public void showGoogleSignInPicker() {
        handleGoogleSignIn();
    }

    private String text(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }
}
