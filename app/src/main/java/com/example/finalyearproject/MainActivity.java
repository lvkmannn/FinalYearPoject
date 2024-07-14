package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavView);
        frameLayout = findViewById(R.id.frameLayout);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.navHome){
                    loadFragment(new HomeFragment(), false);
                } else if (itemId == R.id.navOwnArea) {
                    loadFragment(new OwnAreaFragment(), false);
                } else if (itemId == R.id.navFloodPrediction) {
                    loadFragment(new PredictionFragment(), false);
                } else if (itemId == R.id.navWeatherInfo) {
                    loadFragment(new WeatherFragment(), false);
                } else {
                    // nav sos
                    loadFragment(new SosFragment(), false);
                }

                return true;
            }
        });
        loadFragment(new HomeFragment(), true);

        // Retrieve and log the FCM token
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("MainActivity", "Fetching FCM registration token failed", task.getException());
                    return;
                }

                // Get new FCM registration token
                String token = task.getResult();
                Log.d("FCM", "FCM Registration Token: " + token);

                // Send token to your server or handle as needed
                sendTokenToServer(token);
            }
        });
    }

    private void loadFragment(Fragment fragment, boolean isAppInitialized){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isAppInitialized){
            fragmentTransaction.add(R.id.frameLayout, fragment);
        }else {
            fragmentTransaction.replace(R.id.frameLayout, fragment);
        }
        fragmentTransaction.commit();
    }

    private void sendTokenToServer(String token) {
        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        // Update JSON structure to match expected payload
        String json = "{\"FCMToken\":\"" + token + "\"}";
        Log.d("MainActivity", "JSON Payload: " + json); // Add this line to log the JSON payload

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url("https://lvkmannn.pythonanywhere.com/add-device-token")
                .post(body)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                Log.e("MainActivity", "Failed to send token to server", e);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("MainActivity", "Unexpected code " + response);
                    Log.e("MainActivity", "Response body: " + response.body().string());
                } else {
                    Log.d("MainActivity", "Token successfully sent to server");
                }
            }
        });
    }

}
