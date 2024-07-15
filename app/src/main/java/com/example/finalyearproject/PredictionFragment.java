package com.example.finalyearproject;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

import ViewModel.PredictionViewModel;
import adapter.PredictionAdapter;

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

        RecyclerView recyclerView = view.findViewById(R.id.listPredInfo);
        PredictionAdapter adapter = new PredictionAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        viewModel = new ViewModelProvider(requireActivity()).get(PredictionViewModel.class);

        viewModel.getPredictions().observe(getViewLifecycleOwner(), infoPredicts -> {
            adapter.setData(infoPredicts);
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage ->
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show());

        viewModel.fetchPredictions();
    }


}

