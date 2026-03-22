package com.example.risotto.presentation.auth.views;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.risotto.R;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import android.os.CancellationSignal;
import androidx.core.content.ContextCompat;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;


public class LoginFragment extends Fragment {

    private FirebaseAuth auth;
    private CredentialManager credentialManager;

    private TextInputLayout           tilEmail;
    private TextInputLayout           tilPassword;
    private TextInputEditText         etEmail;
    private TextInputEditText         etPassword;
    private CircularProgressIndicator progress;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        credentialManager = CredentialManager.create(requireContext());

        tilEmail    = view.findViewById(R.id.til_email);
        tilPassword = view.findViewById(R.id.til_password);
        etEmail     = view.findViewById(R.id.et_email);
        etPassword  = view.findViewById(R.id.et_password);
        progress    = view.findViewById(R.id.progress_login);

        view.findViewById(R.id.btn_login).setOnClickListener(v -> handleEmailLogin(view));
        view.findViewById(R.id.btn_google).setOnClickListener(v -> handleGoogleSignIn(view));
        view.findViewById(R.id.btn_guest).setOnClickListener(v -> handleGuestLogin(view));
        view.findViewById(R.id.btn_go_register).setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_login_to_register));
    }



    private void handleEmailLogin(View root) {
        tilEmail.setError(null);
        tilPassword.setError(null);

        String email    = text(etEmail);
        String password = text(etPassword);

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            if (TextUtils.isEmpty(email))    tilEmail.setError(getString(R.string.auth_error_empty_fields));
            if (TextUtils.isEmpty(password)) tilPassword.setError(getString(R.string.auth_error_empty_fields));
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(R.string.auth_error_invalid_email));
            return;
        }
        if (password.length() < 6) {
            tilPassword.setError(getString(R.string.auth_error_short_password));
            return;
        }

        setLoading(true);
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> navigateToHome(root))
                .addOnFailureListener(e -> {
                    setLoading(false);
                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }



    private void handleGoogleSignIn(View root) {
        setLoading(true);

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

                                auth.signInWithCredential(authCredential)
                                        .addOnSuccessListener(authResult -> navigateToHome(root))
                                        .addOnFailureListener(e -> {
                                            setLoading(false);
                                            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        });
                            } else {
                                setLoading(false);
                                Toast.makeText(requireContext(), "Unexpected credential type", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            setLoading(false);
                            Toast.makeText(requireContext(), "Auth error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(GetCredentialException e) {
                        setLoading(false);
                        Toast.makeText(requireContext(), "Google sign-in canceled or failed", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }



    private void handleGuestLogin(View root) {
        setLoading(true);
        auth.signInAnonymously()
                .addOnSuccessListener(result -> navigateToHome(root))
                .addOnFailureListener(e -> {
                    setLoading(false);
                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }



    private void navigateToHome(View root) {
        setLoading(false);
        // Pop everything back to the start and go to Home
        Navigation.findNavController(root).navigate(R.id.action_login_to_home);
    }

    private void setLoading(boolean loading) {
        progress.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    private String text(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }
}
