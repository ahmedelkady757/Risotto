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
import com.google.firebase.auth.UserProfileChangeRequest;


public class RegisterFragment extends Fragment {

    private FirebaseAuth auth;

    private TextInputLayout           tilUsername;
    private TextInputLayout           tilEmail;
    private TextInputLayout           tilPassword;
    private TextInputEditText         etUsername;
    private TextInputEditText         etEmail;
    private TextInputEditText         etPassword;
    private CircularProgressIndicator progress;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();

        tilUsername = view.findViewById(R.id.til_username);
        tilEmail    = view.findViewById(R.id.til_email);
        tilPassword = view.findViewById(R.id.til_password);
        etUsername  = view.findViewById(R.id.et_username);
        etEmail     = view.findViewById(R.id.et_email);
        etPassword  = view.findViewById(R.id.et_password);
        progress    = view.findViewById(R.id.progress_register);

        view.findViewById(R.id.btn_register).setOnClickListener(v -> handleRegister(view));
        view.findViewById(R.id.btn_go_login).setOnClickListener(v ->
                Navigation.findNavController(view).popBackStack());
    }



    private void handleRegister(View root) {
        tilUsername.setError(null);
        tilEmail.setError(null);
        tilPassword.setError(null);

        String username = text(etUsername);
        String email    = text(etEmail);
        String password = text(etPassword);

        if (TextUtils.isEmpty(username)) {
            tilUsername.setError(getString(R.string.auth_error_empty_fields));
            return;
        }
        if (TextUtils.isEmpty(email)) {
            tilEmail.setError(getString(R.string.auth_error_empty_fields));
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(R.string.auth_error_invalid_email));
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            tilPassword.setError(getString(R.string.auth_error_short_password));
            return;
        }

        setLoading(true);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    // Save display name
                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build();
                    result.getUser().updateProfile(profileUpdate)
                            .addOnCompleteListener(task -> navigateToHome(root));
                })
                .addOnFailureListener(e -> {
                    setLoading(false);
                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }



    private void navigateToHome(View root) {
        setLoading(false);
        Navigation.findNavController(root).navigate(R.id.action_register_to_home);
    }

    private void setLoading(boolean loading) {
        progress.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    private String text(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }
}
