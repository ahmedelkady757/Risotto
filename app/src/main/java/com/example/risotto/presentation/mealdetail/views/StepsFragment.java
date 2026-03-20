package com.example.risotto.presentation.mealdetail.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.risotto.R;
import com.example.risotto.core.utils.AppLogger;

public class StepsFragment extends Fragment {

    private TextView tvSteps;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppLogger.logFragment("StepsFragment", "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_steps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppLogger.logFragment("StepsFragment", "onViewCreated");
        tvSteps = view.findViewById(R.id.tv_steps);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        tvSteps = null;
    }


    public void bindSteps(String instructions) {
        if (tvSteps == null) return;
        tvSteps.setText(instructions);
        AppLogger.d("StepsFragment: steps bound");
    }
}