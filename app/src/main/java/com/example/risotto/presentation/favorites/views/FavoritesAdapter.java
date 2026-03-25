package com.example.risotto.presentation.favorites.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.risotto.R;
import com.example.risotto.data.model.Meal;

import java.util.ArrayList;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {

    private final List<Meal> favoriteMeals = new ArrayList<>();
    private final OnFavoriteMealClickListener clickListener;

    public interface OnFavoriteMealClickListener {
        void onMealClick(Meal meal);
        void onFavoriteToggleClick(Meal meal);
    }

    public FavoritesAdapter(OnFavoriteMealClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void submitList(List<Meal> meals) {
        this.favoriteMeals.clear();
        this.favoriteMeals.addAll(meals);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite_meal, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        holder.bind(favoriteMeals.get(position));
    }

    @Override
    public int getItemCount() {
        return favoriteMeals.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivMealImage;
        private final ImageView ivFavoriteToggle;
        private final TextView tvMealName;
        private final TextView tvMealArea;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMealImage = itemView.findViewById(R.id.iv_meal_image);
            ivFavoriteToggle = itemView.findViewById(R.id.iv_favorite_toggle);
            tvMealName = itemView.findViewById(R.id.tv_meal_name);
            tvMealArea = itemView.findViewById(R.id.tv_meal_area);
        }

        public void bind(Meal meal) {
            tvMealName.setText(meal.getName());
            tvMealArea.setText(meal.getArea() != null ? meal.getArea() : "");

            Glide.with(itemView.getContext())
                    .load(meal.getThumbnailUrl())
                    .placeholder(R.color.surface_card)
                    .into(ivMealImage);

            itemView.setOnClickListener(v -> {
                if (clickListener != null) clickListener.onMealClick(meal);
            });

            ivFavoriteToggle.setOnClickListener(v -> {
                if (clickListener != null) clickListener.onFavoriteToggleClick(meal);
            });
        }
    }
}
