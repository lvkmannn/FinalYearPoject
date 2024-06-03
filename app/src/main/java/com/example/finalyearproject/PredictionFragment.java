package com.example.finalyearproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.PredictionAdapter;
import model.InfoPredict;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class PredictionFragment extends Fragment {
    private ListView listPredInfo;
    private PredictionAdapter adapter;
    private List<InfoPredict> dataList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prediction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Finding the view
        listPredInfo = view.findViewById(R.id.listPredInfo);

        dataList = new ArrayList<>();

        // Create and set up the adapter
        adapter = new PredictionAdapter(getContext(), dataList);
        listPredInfo.setAdapter(adapter);

        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder().url("http://10.0.2.2:5000/get-predictions-by-today-date").header("Connection", "close").build();

        // Make asynchronous call
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                String message = "Network Error";
                Log.e("NetworkError", "10.0.2.2:5000/get-predictions-by-today-date", e);

                if (e!=null){
                    message = e.getMessage();
                }
                // Ensure Toast is shown on the main UI thread
                final String finalMessage = message;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), finalMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    JSONArray jsonArray = new JSONArray(response.body().string());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String district = jsonObject.getString("district");
                        String location = jsonObject.getString("location");
                        String date = jsonObject.getString("date");
                        double testLoss = jsonObject.getDouble("testLoss");
                        double MAE = jsonObject.getDouble("MAE");
                        double MSE = jsonObject.getDouble("MSE");
                        double RMSE = jsonObject.getDouble("RMSE");
                        double day1 = jsonObject.getDouble("day1");
                        double day2 = jsonObject.getDouble("day2");
                        double day3 = jsonObject.getDouble("day3");


                        // Create InfoPredict object and add to dataList
                        InfoPredict data = new InfoPredict(date, district, location, testLoss, MAE, MSE, RMSE, day1, day2, day3);
                        dataList.add(data);
                    }

                    // Notify the adapter of data changes
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}