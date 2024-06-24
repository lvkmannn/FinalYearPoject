package com.example.finalyearproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


import adapter.WeatherAdapter;
import model.Weather;

public class WeatherFragment extends Fragment {

    private ListView weatherListView;
    private Spinner spinner;
    private static final String BASE_URL = "https://api.data.gov.my/weather/forecast/?limit=7&contains=";
    private String JSON_URL = BASE_URL + "Shah%20Alam@location__location_name"; // Default URL

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

        // For spinner
        spinner = view.findViewById(R.id.spinnerDistrict);

        // Create an ArrayAdapter with the custom hint
        ArrayAdapter<CharSequence> arrayAdapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.districtArray)) {
            @Override
            public boolean isEnabled(int position) {
                // Disable the first item from Spinner (Select District)
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray));
                } else {
                    tv.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
                }
                return view;
            }

            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray));
                } else {
                    tv.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
                }
                return view;
            }
        };

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        // Set listener for spinner selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) { // Check if the selected item is not the hint
                    String selectedDistrict = parent.getItemAtPosition(position).toString();
                    JSON_URL = BASE_URL + selectedDistrict + "@location__location_name";
                    new GetData().execute();
                } else {
                    // Clear the weather list if hint is selected
                    weatherList.clear();
                    PutDataIntoListView(weatherList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Initially load data if needed
        new GetData().execute();
    }

    public class GetData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder current = new StringBuilder();

            try {
                URL url = new URL(JSON_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();
                while (data != -1) {
                    current.append((char) data);
                    data = inputStreamReader.read();
                }

                urlConnection.disconnect();
                return current.toString();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            weatherList.clear(); // Clear the previous data
            try {
                JSONArray jsonArray = new JSONArray(s);

                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONObject locationObject = jsonObject.getJSONObject("location");

                    String locationName = locationObject.getString("location_name");
                    String date = jsonObject.getString("date");
                    String morningForecast = jsonObject.getString("morning_forecast");
                    String afternoonForecast = jsonObject.getString("afternoon_forecast");
                    String nightForecast = jsonObject.getString("night_forecast");
                    String summaryForecast = jsonObject.getString("summary_forecast");
                    String summaryWhen = jsonObject.getString("summary_when");
                    String minTemp = jsonObject.getString("min_temp");
                    String maxTemp = jsonObject.getString("max_temp");

                    // Format the date
                    Date originalDate = originalFormat.parse(date);
                    String formattedDate = targetFormat.format(originalDate);

                    // Create a Weather instance
                    Weather model = new Weather();
                    model.setLocation_name(locationName);
                    model.setDate("Date: " + formattedDate);
                    model.setMorning_forecast("Morning Forecast: " + morningForecast);
                    model.setAfternoon_forecast("Afternoon Forecast: " + afternoonForecast);
                    model.setNight_forecast("Night Forecast: " + nightForecast);
                    model.setSummary_forecast("Summary: " + summaryForecast);
                    model.setSummary_when("When: " + summaryWhen);
                    model.setMin_temp("Min. Temperature: " + minTemp + "°C");
                    model.setMax_temp("Max. Temperature: " + maxTemp + "°C");

                    weatherList.add(model);
                }

                // Sort the weather list by date
                Collections.sort(weatherList, new Comparator<Weather>() {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                    @Override
                    public int compare(Weather o1, Weather o2) {
                        try {
                            return dateFormat.parse(o1.getDate().substring(6)).compareTo(dateFormat.parse(o2.getDate().substring(6)));
                        } catch (ParseException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });

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
