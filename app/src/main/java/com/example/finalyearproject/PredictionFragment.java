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
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import ViewModel.PredictionViewModel;
import adapter.PredictionAdapter;
import model.InfoPredict;

public class PredictionFragment extends Fragment {
    private ListView listPredInfo;
    private PredictionAdapter adapter;
    private PredictionViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_prediction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listPredInfo = view.findViewById(R.id.listPredInfo);
        adapter = new PredictionAdapter(getContext(), new ArrayList<>());
        listPredInfo.setAdapter(adapter);

        // Scope the ViewModel to the Activity
        viewModel = new ViewModelProvider(requireActivity()).get(PredictionViewModel.class);

        viewModel.getPredictions().observe(getViewLifecycleOwner(), new Observer<List<InfoPredict>>() {
            @Override
            public void onChanged(List<InfoPredict> infoPredicts) {
                adapter.setData(infoPredicts);
                adapter.notifyDataSetChanged();
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Fetch predictions if not already loaded
        viewModel.fetchPredictions();
    }
}

