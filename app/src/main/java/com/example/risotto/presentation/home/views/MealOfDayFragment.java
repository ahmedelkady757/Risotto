package com.example.risotto.presentation.home.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import com.example.risotto.R;
import com.example.risotto.data.model.Meal;

public class MealOfDayFragment extends Fragment {


    public interface OnViewRecipeClickListener {
        void onViewRecipeClick(Meal meal);
    }


    private ImageView ivMealOfDay;
    private TextView tvMealName;
    private TextView tvMealArea;
    private MaterialButton btnViewRecipe;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_of_day, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivMealOfDay   = view.findViewById(R.id.iv_meal_of_day);
        tvMealName    = view.findViewById(R.id.tv_meal_name);
        tvMealArea    = view.findViewById(R.id.tv_meal_area);
        btnViewRecipe = view.findViewById(R.id.btn_view_recipe);

        if (pendingMeal != null && pendingListener != null) {
            bindMeal(pendingMeal, pendingListener);
            pendingMeal = null;
            pendingListener = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ivMealOfDay   = null;
        tvMealName    = null;
        tvMealArea    = null;
        btnViewRecipe = null;
    }


    private Meal pendingMeal;
    private OnViewRecipeClickListener pendingListener;

    public void bindMeal(Meal meal, OnViewRecipeClickListener listener) {
        if (tvMealName == null) {
            pendingMeal = meal;
            pendingListener = listener;
            return;
        }

        tvMealName.setText(meal.getName());
        tvMealArea.setText(meal.getArea());

        Glide.with(requireContext())
                .load(meal.getThumbnailUrl())
                .centerCrop()
                .into(ivMealOfDay);

        btnViewRecipe.setOnClickListener(v -> listener.onViewRecipeClick(meal));
        requireView().setOnClickListener(v -> listener.onViewRecipeClick(meal));
    }
}
