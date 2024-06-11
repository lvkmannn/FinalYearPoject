package ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import model.InfoWeather;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<List<InfoWeather>> weatherData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final OkHttpClient okHttpClient;
    private boolean isDataLoaded = false;

    public HomeViewModel() {
        // Configure OkHttpClient with increased timeout settings
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public LiveData<List<InfoWeather>> getWeatherData() {
        return weatherData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchWeatherData() {
        if (isDataLoaded) {
            return;
        }

        Request request = new Request.Builder()
                .url("http://10.0.2.2:5000/get-items-by-current-hour")
                .header("Connection", "close")
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                String message = "Network Error";
                if (e != null) {
                    message = e.getMessage();
                }
                errorMessage.postValue(message);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    JSONArray jsonArray = new JSONArray(response.body().string());
                    List<InfoWeather> dataList = new ArrayList<>();

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

                        InfoWeather data = new InfoWeather(id, latitude, longitude, normal, alert, warning, danger, hourlyRainfall, todayRainfall, waterLevel, stationId, districtName, stationName, lastUpdate);
                        dataList.add(data);
                    }
                    weatherData.postValue(dataList);
                    isDataLoaded = true;
                } catch (JSONException e) {
                    errorMessage.postValue("Data parsing error");
                }
            }
        });
    }
}

