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

    // Kept across view recreation so data is never lost when navigating back
    private List<Country> pendingCountries;
    private CountriesAdapter.OnCountryClickListener pendingListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rvCountries = new RecyclerView(requireContext());
        rvCountries.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        rvCountries.setClipToPadding(false);
        rvCountries.setPadding(0, 8, 16, 8);
        return rvCountries;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new CountriesAdapter(pendingListener);
        rvCountries.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        rvCountries.setAdapter(adapter);

        // Re-apply any data that arrived before the view was ready
        if (pendingCountries != null) {
            adapter.setCountries(pendingCountries);
        }
    }

    /**
     * Called by HomeFragment whenever the presenter pushes new or cached data.
     * Safe to call at any time — if the view isn't ready yet, data is buffered and
     * applied inside onViewCreated when the RecyclerView and adapter are set up.
     */
    public void bindCountries(List<Country> countries,
                              CountriesAdapter.OnCountryClickListener listener) {
        pendingCountries = countries;
        pendingListener  = listener;

        if (adapter == null) return; // view not ready yet — onViewCreated will pick it up

        adapter = new CountriesAdapter(listener);
        rvCountries.setAdapter(adapter);
        adapter.setCountries(countries);
    }
}
