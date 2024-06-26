package ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import model.InfoPredict;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PredictionViewModel extends ViewModel {
    private final MutableLiveData<List<InfoPredict>> predictions = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final OkHttpClient okHttpClient;
    private volatile boolean isDataLoaded = false;  // Flag to check if data is loaded

    public PredictionViewModel() {
        // Configure OkHttpClient with increased timeout settings
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build();
    }

    public LiveData<List<InfoPredict>> getPredictions() {
        return predictions;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchPredictions() {
        if (isDataLoaded) {
            // Data is already loaded, no need to fetch again
            return;
        }

        Request request = new Request.Builder()
                .url("https://lvkmannn.pythonanywhere.com/get-predictions-by-today-date")
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
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful() || responseBody == null) {
                        throw new IOException("Unexpected code " + response);
                    }

                    String responseData = responseBody.string();
                    JSONArray jsonArray = new JSONArray(responseData);
                    List<InfoPredict> dataList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        double id = jsonObject.getDouble("id");
                        String district = jsonObject.getString("district");
                        String location = jsonObject.getString("location");
                        double latitude = jsonObject.getDouble("latitude");
                        double longitude = jsonObject.getDouble("longitude");
                        String date = jsonObject.getString("date");
                        double normal = jsonObject.getDouble("normal");
                        double alert = jsonObject.getDouble("alert");
                        double warning = jsonObject.getDouble("warning");
                        double danger = jsonObject.getDouble("danger");
                        double testLoss = jsonObject.getDouble("testLoss");
                        double MAE = jsonObject.getDouble("MAE");
                        double MSE = jsonObject.getDouble("MSE");
                        double RMSE = jsonObject.getDouble("RMSE");
                        double day1 = jsonObject.getDouble("day1");
                        double day2 = jsonObject.getDouble("day2");
                        double day3 = jsonObject.getDouble("day3");

                        InfoPredict data = new InfoPredict(date, district, location, id, testLoss, MAE, MSE, RMSE, day1, day2, day3, normal, alert, warning, danger, latitude, longitude);
                        dataList.add(data);
                    }
                    predictions.postValue(dataList);
                    isDataLoaded = true;  // Set flag to true after data is loaded
                } catch (JSONException e) {
                    errorMessage.postValue("Data parsing error: " + e.getMessage());
                } catch (IOException e) {
                    errorMessage.postValue("Network error: " + e.getMessage());
                }
            }
        });
    }
}
