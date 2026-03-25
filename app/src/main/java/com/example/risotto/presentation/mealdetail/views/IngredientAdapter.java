package com.example.risotto.presentation.mealdetail.views;

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
import com.example.risotto.data.model.Ingredient;


public class IngredientAdapter extends ListAdapter<Ingredient, IngredientAdapter.IngredientViewHolder> {

    private static final String INGREDIENT_IMAGE_BASE_URL =
            "https://www.themealdb.com/images/ingredients/";


    public IngredientAdapter() {
        super(DIFF_CALLBACK);
    }


    private static final DiffUtil.ItemCallback<Ingredient> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Ingredient>() {

                @Override
                public boolean areItemsTheSame(@NonNull Ingredient oldItem,
                                               @NonNull Ingredient newItem) {
                    return oldItem.getName().equals(newItem.getName());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Ingredient oldItem,
                                                  @NonNull Ingredient newItem) {
                    return oldItem.getName().equals(newItem.getName())
                            && oldItem.getMeasure().equals(newItem.getMeasure());
                }
            };


    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        holder.bind(getItem(position));
    }


    static class IngredientViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivIngredient;
        private final TextView tvIngredientName;
        private final TextView tvIngredientMeasure;

        IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIngredient       = itemView.findViewById(R.id.iv_ingredient);
            tvIngredientName   = itemView.findViewById(R.id.tv_ingredient_name);
            tvIngredientMeasure = itemView.findViewById(R.id.tv_ingredient_measure);
        }

        void bind(Ingredient ingredient) {
            tvIngredientName.setText(ingredient.getName());
            tvIngredientMeasure.setText(ingredient.getMeasure());

            // Build thumbnail URL from ingredient name
            String imageUrl = INGREDIENT_IMAGE_BASE_URL
                    + ingredient.getName().replace(" ", "%20")
                    + "-Small.png";

            Glide.with(itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_search)
                    .error(R.drawable.ic_search)
                    .centerInside()
                    .into(ivIngredient);
        }
    }
}