package com.example.risotto.core.ui.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.risotto.R;
import com.google.android.material.button.MaterialButton;

public class CustomConfirmDialog extends DialogFragment {

    public interface OnConfirmListener {
        void onConfirm();
    }

    private OnConfirmListener listener;
    private String title;
    private String message;
    private int iconRes = -1;

    public static CustomConfirmDialog newInstance(String title, String message, OnConfirmListener listener) {
        CustomConfirmDialog dialog = new CustomConfirmDialog();
        dialog.title = title;
        dialog.message = message;
        dialog.listener = listener;
        return dialog;
    }
    
    public CustomConfirmDialog setIcon(int iconRes) {
        this.iconRes = iconRes;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return inflater.inflate(R.layout.dialog_confirm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvTitle = view.findViewById(R.id.tv_dialog_title);
        TextView tvMessage = view.findViewById(R.id.tv_dialog_message);
        ImageView ivIcon = view.findViewById(R.id.iv_dialog_icon);
        MaterialButton btnCancel = view.findViewById(R.id.btn_dialog_cancel);
        MaterialButton btnConfirm = view.findViewById(R.id.btn_dialog_confirm);

        if (title != null) tvTitle.setText(title);
        if (message != null) tvMessage.setText(message);
        if (iconRes != -1) ivIcon.setImageResource(iconRes);

        btnCancel.setOnClickListener(v -> dismiss());
        btnConfirm.setOnClickListener(v -> {
            if (listener != null) listener.onConfirm();
            dismiss();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }
    }
}
