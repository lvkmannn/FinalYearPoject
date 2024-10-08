package com.example.finalyearproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import ViewModel.HomeViewModel;
import ViewModel.PredictionViewModel;
import model.InfoHome;
import model.InfoPredict;

public class OwnAreaFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "OwnAreaFragment";
    private GoogleMap map;
    private final int FINE_PERMISSION_CODE = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private SearchView mapSearchView;
    private PredictionViewModel predictionViewModel;
    private HomeViewModel homeViewModel;
    private List<InfoHome> homeDataList;
    private List<InfoPredict> predictionDataList;
    private Bundle savedInstanceState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        setRetainInstance(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_own_area, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapSearchView = view.findViewById(R.id.mapSearchView);
        FloatingActionButton currentLocFab = view.findViewById(R.id.currentLocFab);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        mapSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocation(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        predictionViewModel = new ViewModelProvider(this).get(PredictionViewModel.class);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        currentLocFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });


        predictionViewModel.getPredictions().observe(getViewLifecycleOwner(), predictions -> {
            predictionDataList = predictions;
            if (map != null) {
                addMarkers(predictions);
            }
        });

        homeViewModel.getWeatherData().observe(getViewLifecycleOwner(), this::updateHomeData);

        predictionViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error ->
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show()
        );

        homeViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error ->
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show()
        );

        predictionViewModel.fetchPredictions();
        homeViewModel.fetchWeatherData();
    }

    //Function to search location
    private void searchLocation(String location) {
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocationName(location, 1);
            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                map.addMarker(new MarkerOptions().position(latLng).title(location));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Function to get current location
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                }
            });
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        getCurrentLocation();
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Re-add markers when the map is ready
        if (predictionDataList != null) {
            addMarkers(predictionDataList);
        }
    }


    // Function to add marker
    private void addMarkers(List<InfoPredict> predictions) {
        if (map != null) {
            map.clear();
            if (homeDataList == null) {
                Log.d(TAG, "addMarkers: homeDataList is null, skipping marker addition.");
                return;
            }
            for (InfoPredict info : predictions) {
                LatLng latLng = new LatLng(info.getLatitude(), info.getLongitude());
                //double waterLevel = getWaterLevelForLocation(info.getLatitude(), info.getLongitude());
                double waterLevel = getWaterLevelForLocation(info.getLocation());


                // Set the time zone to Malaysia (GMT+8)
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kuala_Lumpur"));
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

                // Calculate the upcoming 3 days
                String day1 = daysOfWeek[(dayOfWeek % 7)]; // today
                String day2 = daysOfWeek[(dayOfWeek + 1) % 7];
                String day3 = daysOfWeek[(dayOfWeek + 2) % 7];

                // Determine the highest water level and its corresponding day
                double highestWaterLevel = Math.max(info.getDay1(), Math.max(info.getDay2(), info.getDay3()));
                int highestWaterLevelIndex;
                if (highestWaterLevel == info.getDay1()) {
                    highestWaterLevelIndex = 0;
                } else if (highestWaterLevel == info.getDay2()) {
                    highestWaterLevelIndex = 1;
                } else {
                    highestWaterLevelIndex = 2;
                }

                String predictedDay = daysOfWeek[(dayOfWeek + highestWaterLevelIndex) % 7];

                // Create the snippet string based on highest water level
                String snippet;
                if (highestWaterLevel > info.getDanger()) {
                    snippet = String.format("Current Water Level: %.2f Meter\n" +
                                    "\n3-Day Water Level Prediction: \n" +
                                    "%s: %.2f Meter\n" +
                                    "%s: %.2f Meter\n" +
                                    "%s: %.2f Meter\n" +
                                    "\nFlood is predicted on %s",
                            waterLevel, day1, info.getDay1(), day2, info.getDay2(), day3, info.getDay3(), predictedDay);
                } else {
                    snippet = String.format("Current Water Level: %.2f Meter\n" +
                                    "\n3-Day Water Level Prediction: \n" +
                                    "%s: %.2f Meter\n" +
                                    "%s: %.2f Meter\n" +
                                    "%s: %.2f Meter\n" +
                                    "\nNo flood is predicted within 3 days",
                            waterLevel, day1, info.getDay1(), day2, info.getDay2(), day3, info.getDay3());
                }

                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(info.getLocation())
                        .snippet(snippet));

                map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker arg0) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(@NonNull Marker marker) {
                        LinearLayout info = new LinearLayout(getContext());
                        info.setOrientation(LinearLayout.VERTICAL);

                        TextView title = new TextView(getContext());
                        title.setTextColor(Color.BLACK);
                        title.setGravity(Gravity.CENTER);
                        title.setTypeface(null, Typeface.BOLD);
                        title.setText(marker.getTitle());

                        // Get current date and time in Malaysian time zone
                        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kuala_Lumpur"));
                        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
                        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kuala_Lumpur")); // Ensure the formatter uses the correct time zone
                        String currentTime = dateFormat.format(calendar.getTime());

                        TextView subtitleView = new TextView(getContext());
                        subtitleView.setTextColor(Color.GRAY);
                        subtitleView.setGravity(Gravity.CENTER);
                        subtitleView.setTypeface(null, Typeface.ITALIC);
                        subtitleView.setText("As of " + currentTime);

                        TextView snippet = new TextView(getContext());
                        snippet.setTextColor(Color.DKGRAY);
                        snippet.setText(marker.getSnippet());

                        info.addView(title);
                        info.addView(subtitleView);
                        info.addView(snippet);

                        return info;
                    }
                });
            }
        }
    }


    // Function to get InfoHome object
    private void updateHomeData(List<InfoHome> homeData) {
        this.homeDataList = homeData;
        Log.d(TAG, "updateHomeData: Received home data with size " + homeData.size());
        if (map != null && predictionDataList != null) {
            addMarkers(predictionDataList);
        }
    }


    // Function to get the water level for location that is available for prediction
    private double getWaterLevelForLocation(String location) {
        if (homeDataList != null) { // Add null check
            for (InfoHome info : homeDataList) {
                if (info.getStationName().equals(location)) {
                    Log.d(TAG, "getWaterLevelForLocation: Found matching water level for location " + info.getStationName() + ": " + info.getWaterLevel());
                    return info.getWaterLevel();
                }
            }
        } else {
            Log.d(TAG, "getWaterLevelForLocation: homeDataList is null");
        }
        Log.d(TAG, "getWaterLevelForLocation: No matching water level found for latitude " + location);
        return 0.0;
    }

    public void setMapType(int mapType) {
        if (map != null) {
            map.setMapType(mapType);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }
}