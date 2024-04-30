package com.example.finalyearproject;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import adapter.WeatherAdapter;
import model.Weather;

public class WeatherFragment extends Fragment {

    private ListView weatherListView;
    private static String JSON_URL = "https://api.data.gov.my/weather/forecast/?limit=7&contains=Shah%20Alam@location__location_name";

    // List of the weather information from api
    List<Weather> weatherList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        weatherList = new ArrayList<>();
        weatherListView = view.findViewById(R.id.weatherListView);

        GetData getData = new GetData();
        getData.execute();
    }

    public class GetData extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            String current = "";

            try {
                URL url;
                HttpURLConnection urlConnection = null;

                try {
                    url = new URL(JSON_URL);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream inputStream = urlConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                    int data = inputStreamReader.read();

                    while(data!=-1){
                        current += (char) data;
                        data = inputStreamReader.read();
                    }
                    return current;

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (urlConnection != null){
                        urlConnection.disconnect();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return current;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray jsonArray = new JSONArray(s);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    JSONObject locationObject = jsonObject.getJSONObject("location");

                    //String locationId = locationObject.getString("location_id");
                    String locationName = locationObject.getString("location_name");

                    String date = jsonObject.getString("date");
                    String morningForecast = jsonObject.getString("morning_forecast");
                    String afternoonForecast = jsonObject.getString("afternoon_forecast");
                    String nightForecast = jsonObject.getString("night_forecast");
                    String summaryForecast = jsonObject.getString("summary_forecast");
                    String summaryWhen = jsonObject.getString("summary_when");
                    String minTemp = jsonObject.getString("min_temp");
                    String maxTemp = jsonObject.getString("max_temp");

                    // Create a Weather instance
                    Weather model = new Weather();
                    model.setLocation_name(locationName);
                    model.setDate(date);
                    model.setMorning_forecast("Morning Forecast: " + morningForecast);
                    model.setAfternoon_forecast("Afternoon Forecast: " + afternoonForecast);
                    model.setNight_forecast("Night Forecast: " + nightForecast);
                    model.setSummary_forecast("Summary: "+ summaryForecast);
                    model.setSummary_when("When: " + summaryWhen);
                    model.setMin_temp("Min. Temperature: " + minTemp + "°C");
                    model.setMax_temp("Max. Temperature: " + maxTemp + "°C");

                    weatherList.add(model);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            // Call method to put data into ListView
            PutDataIntoListView(weatherList);
        }
    }

    private void PutDataIntoListView(List<Weather> weatherList) {
        WeatherAdapter weatherAdapter = new WeatherAdapter(getActivity(), weatherList);
        weatherListView.setAdapter(weatherAdapter);
    }
}
