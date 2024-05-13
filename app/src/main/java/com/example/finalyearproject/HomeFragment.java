package com.example.finalyearproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.HomeAdapter;
import model.InfoWeather;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment implements Filterable{

    private ListView listWeatherInfo;
    private HomeAdapter adapter;

    private List<InfoWeather> dataList;
    private SearchView search_bar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Finding the view
        listWeatherInfo = view.findViewById(R.id.listWeatherInfo);

        dataList = new ArrayList<>();

        // Create and set up the adapter
        adapter = new HomeAdapter(getContext(), dataList);
        listWeatherInfo.setAdapter(adapter);
        search_bar = view.findViewById(R.id.search_bar);

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("http://192.168.1.101:5000/get-items-by-current-hour").build();

        // Make asynchronous call
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                String message = "Network Error";

                if (e!=null){
                    message = e.getMessage();
                }
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    JSONArray jsonArray = new JSONArray(response.body().string());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String stationId = jsonObject.getString("stationId");
                        String lastUpdate = jsonObject.getString("lastUpdate");
                        String districtName = jsonObject.getString("districtName");
                        String stationName = jsonObject.getString("stationName");
                        double latitude = jsonObject.getDouble("latitude");
                        double longitude = jsonObject.getDouble("longitude");
                        double normal = jsonObject.getDouble("normal");
                        double alert = jsonObject.getDouble("alert");
                        double warning = jsonObject.getDouble("warning");
                        double danger = jsonObject.getDouble("danger");
                        double waterLevel = jsonObject.getDouble("waterLevel");
                        double hourlyRainfall = jsonObject.getDouble("hourlyRainfall");
                        double todayRainfall = jsonObject.getDouble("todayRainfall");

                        // Create InfoWeather object and add to dataList
                        InfoWeather data = new InfoWeather(id,latitude, longitude,normal, alert, warning,danger, hourlyRainfall, todayRainfall, waterLevel, stationId, districtName, stationName, lastUpdate);
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
        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Filter the list based on the query when the user submits
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the list based on the query as the user types
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}