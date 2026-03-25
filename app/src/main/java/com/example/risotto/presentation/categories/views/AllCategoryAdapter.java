package com.example.risotto.presentation.categories.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.risotto.R;
import com.example.risotto.data.model.Category;

public class AllCategoryAdapter extends ListAdapter<Category, AllCategoryAdapter.AllCategoryViewHolder> {

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    private final OnCategoryClickListener listener;

    public AllCategoryAdapter(OnCategoryClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Category> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Category>() {
                @Override
                public boolean areItemsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
                    return oldItem.getName().equals(newItem.getName()) &&
                           oldItem.getThumbnailUrl().equals(newItem.getThumbnailUrl());
                }
            };

    @NonNull
    @Override
    public AllCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_grid, parent, false);
        return new AllCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllCategoryViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    static class AllCategoryViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivCategory;
        private final TextView tvCategoryName;

        AllCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCategory = itemView.findViewById(R.id.iv_category);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
        }

        void bind(Category category, OnCategoryClickListener listener) {
            tvCategoryName.setText(category.getName());
            Glide.with(itemView.getContext())
                    .load(category.getThumbnailUrl())
                    .centerCrop()
                    .placeholder(R.drawable.ic_home)
                    .into(ivCategory);

            itemView.setOnClickListener(v -> listener.onCategoryClick(category));
        }
    }
}
