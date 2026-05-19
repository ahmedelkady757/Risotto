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

import com.example.risotto.data.model.Country;
import java.util.List;

public class CountriesFragment extends Fragment {

    private RecyclerView rvCountries;
    private CountriesAdapter adapter;
    private CountriesAdapter.OnCountryClickListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rvCountries = new RecyclerView(requireContext());
        rvCountries.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        rvCountries.setClipToPadding(false);
        rvCountries.setPadding(0, 8, 0, 8);
        return rvCountries;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        adapter = new CountriesAdapter(listener);
        rvCountries.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        rvCountries.setAdapter(adapter);
    }

    public void bindCountries(List<Country> countries, CountriesAdapter.OnCountryClickListener listener) {
        this.listener = listener;
        if (adapter != null) {
            adapter.setCountries(countries);
            // Re-assign listener if adapter was already created
            adapter = new CountriesAdapter(listener);
            rvCountries.setAdapter(adapter);
            adapter.setCountries(countries);
        }
    }
}
