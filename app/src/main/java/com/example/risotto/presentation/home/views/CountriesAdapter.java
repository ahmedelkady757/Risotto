package com.example.risotto.presentation.home.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.risotto.R;
import com.example.risotto.data.model.Country;

import java.util.ArrayList;
import java.util.List;

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.CountryViewHolder> {

    private List<Country> countries = new ArrayList<>();
    private final OnCountryClickListener listener;

    public interface OnCountryClickListener {
        void onCountryClick(Country country);
    }

    public CountriesAdapter(OnCountryClickListener listener) {
        this.listener = listener;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        Country country = countries.get(position);
        holder.bind(country, listener);
    }

    @Override
    public int getItemCount() {
        return countries != null ? countries.size() : 0;
    }

    static class CountryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFlag;
        TextView tvName;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFlag = itemView.findViewById(R.id.iv_country_flag);
            tvName = itemView.findViewById(R.id.tv_country_name);
        }

        public void bind(Country country, OnCountryClickListener listener) {
            tvName.setText(country.getName());
            Glide.with(itemView.getContext())
                    .load(country.getImageUrl())
                    .placeholder(R.drawable.ic_flag_placeholder)
                    .error(R.drawable.ic_flag_placeholder)
                    .transition(com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade(200))
                    .into(ivFlag);
            
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCountryClick(country);
                }
            });
        }
    }
}
