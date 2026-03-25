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
import com.example.risotto.data.model.Meal;

public class TopMealAdapter extends ListAdapter<Meal, TopMealAdapter.TopMealViewHolder> {

    public interface OnTopMealClickListener {
        void onTopMealClick(Meal meal);
    }

    private final OnTopMealClickListener listener;

    public TopMealAdapter(OnTopMealClickListener listener) {
        super(new MealDiffCallback());
        this.listener = listener;
    }

    @NonNull
    @Override
    public TopMealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_top_meal, parent, false);
        return new TopMealViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TopMealViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class TopMealViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivMeal;
        private final TextView tvName;
        private final OnTopMealClickListener listener;

        public TopMealViewHolder(@NonNull View itemView, OnTopMealClickListener listener) {
            super(itemView);
            this.listener = listener;
            ivMeal = itemView.findViewById(R.id.iv_top_meal);
            tvName = itemView.findViewById(R.id.tv_top_meal_name);
        }

        public void bind(Meal meal) {
            tvName.setText(meal.getName());
            Glide.with(itemView.getContext())
                    .load(meal.getThumbnailUrl())
                    .centerCrop()
                    .into(ivMeal);

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onTopMealClick(meal);
            });
        }
    }

    static class MealDiffCallback extends DiffUtil.ItemCallback<Meal> {
        @Override
        public boolean areItemsTheSame(@NonNull Meal oldItem, @NonNull Meal newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Meal oldItem, @NonNull Meal newItem) {
            return oldItem.equals(newItem);
        }
    }
}
