package com.example.risotto.presentation.mealdetail.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.risotto.R;
import com.example.risotto.data.model.Ingredient;

import java.util.List;


public class IngredientsFragment extends Fragment {

    private static final int SPAN_COUNT = 3;

    private RecyclerView rvIngredients;
    private IngredientAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ingredients, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvIngredients = view.findViewById(R.id.rv_ingredients);
        rvIngredients.setLayoutManager(
                new GridLayoutManager(requireContext(), SPAN_COUNT)
        );
        adapter = new IngredientAdapter();
        rvIngredients.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rvIngredients = null;
        adapter = null;
    }


    public void bindIngredients(List<Ingredient> ingredients) {
        if (adapter == null) return;
        adapter.submitList(ingredients);
    }
}