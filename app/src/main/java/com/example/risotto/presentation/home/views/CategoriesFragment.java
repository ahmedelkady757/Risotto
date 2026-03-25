package com.example.risotto.presentation.home.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.risotto.R;
import com.example.risotto.data.model.Category;

import java.util.List;


public class CategoriesFragment extends Fragment {

    private RecyclerView rvCategories;
    private CategoryAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvCategories = view.findViewById(R.id.rv_categories);
        rvCategories.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        if (pendingCategories != null && pendingListener != null) {
            bindCategories(pendingCategories, pendingListener);
            pendingCategories = null;
            pendingListener = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rvCategories = null;
        adapter = null;
    }

    private List<Category> pendingCategories;
    private CategoryAdapter.OnCategoryClickListener pendingListener;

    public void bindCategories(List<Category> categories,
                               CategoryAdapter.OnCategoryClickListener listener) {
        if (rvCategories == null) {
            pendingCategories = categories;
            pendingListener = listener;
            return;
        }

        if (adapter == null) {
            adapter = new CategoryAdapter(listener);
            rvCategories.setAdapter(adapter);
        }

        adapter.submitList(categories);
    }
}
