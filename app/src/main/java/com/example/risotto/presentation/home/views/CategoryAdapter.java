package com.example.risotto.presentation.home.views;

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


public class CategoryAdapter extends ListAdapter<Category, CategoryAdapter.CategoryViewHolder> {


    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    private final OnCategoryClickListener listener;


    public CategoryAdapter(OnCategoryClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }


    private static final DiffUtil.ItemCallback<Category> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Category>() {

                @Override
                public boolean areItemsTheSame(@NonNull Category oldItem,
                                               @NonNull Category newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Category oldItem,
                                                  @NonNull Category newItem) {
                    return oldItem.getName().equals(newItem.getName())
                            && oldItem.getThumbnailUrl().equals(newItem.getThumbnailUrl());
                }
            };


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }


    static class CategoryViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivCategory;
        private final TextView tvCategoryName;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCategory     = itemView.findViewById(R.id.iv_category);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
        }

        void bind(Category category, OnCategoryClickListener listener) {
            tvCategoryName.setText(category.getName());

            Glide.with(itemView.getContext())
                    .load(category.getThumbnailUrl())
                    .placeholder(R.drawable.ic_home)
                    .error(R.drawable.ic_home)
                    .centerCrop()
                    .into(ivCategory);

            itemView.setOnClickListener(v -> listener.onCategoryClick(category));
        }
    }
}
