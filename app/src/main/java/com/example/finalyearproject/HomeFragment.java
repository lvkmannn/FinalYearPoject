package com.example.finalyearproject;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ViewModel.HomeViewModel;
import adapter.HomeAdapter;
import model.InfoHome;

public class HomeFragment extends Fragment implements Filterable {

    private ListView listWeatherInfo;
    private HomeAdapter adapter;
    private SearchView search_bar;
    private HomeViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listWeatherInfo = view.findViewById(R.id.listWeatherInfo);
        search_bar = view.findViewById(R.id.search_bar);

        adapter = new HomeAdapter(getContext(), new ArrayList<>());
        listWeatherInfo.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        viewModel.getWeatherData().observe(getViewLifecycleOwner(), new Observer<List<InfoHome>>() {
            @Override
            public void onChanged(List<InfoHome> infoHomes) {
                adapter.setData(infoHomes);
                adapter.notifyDataSetChanged();
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        if (viewModel.getWeatherData().getValue() == null) {
            viewModel.fetchWeatherData();
        }

        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public Filter getFilter() {
        return adapter.getFilter();
    }
}
