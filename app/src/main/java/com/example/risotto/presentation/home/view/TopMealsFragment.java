package com.example.risotto.presentation.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.risotto.R;
import com.example.risotto.core.utils.AppLogger;
import com.example.risotto.data.model.Meal;

import java.util.List;

public class TopMealsFragment extends Fragment {

    private RecyclerView rvTopMeals;
    private TopMealAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppLogger.logFragment("TopMealsFragment", "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_meals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppLogger.logFragment("TopMealsFragment", "onViewCreated");

        rvTopMeals = view.findViewById(R.id.rv_top_meals);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rvTopMeals.setAdapter(null);
        rvTopMeals = null;
        adapter = null;
    }

    public void bindTopMeals(List<Meal> topMeals, TopMealAdapter.OnTopMealClickListener listener) {
        if (rvTopMeals == null) return;
        if (adapter == null) {
            adapter = new TopMealAdapter(listener);
            rvTopMeals.setAdapter(adapter);
        }
        adapter.submitList(topMeals);
    }
}
