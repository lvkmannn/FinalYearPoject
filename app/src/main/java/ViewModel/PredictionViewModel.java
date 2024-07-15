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
            return;
        }

        Request request = new Request.Builder()
                .url("https://lvkmannn.pythonanywhere.com/get-predictions-by-today-date")
                .header("Connection", "close")
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                errorMessage.postValue("Network Error: " + e.getMessage());
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
                        InfoPredict data = new InfoPredict(
                                jsonObject.getString("date"),
                                jsonObject.getString("district"),
                                jsonObject.getString("location"),
                                jsonObject.getDouble("id"),
                                jsonObject.getDouble("testLoss"),
                                jsonObject.getDouble("MAE"),
                                jsonObject.getDouble("MSE"),
                                jsonObject.getDouble("RMSE"),
                                jsonObject.getDouble("day1"),
                                jsonObject.getDouble("day2"),
                                jsonObject.getDouble("day3"),
                                jsonObject.getDouble("normal"),
                                jsonObject.getDouble("alert"),
                                jsonObject.getDouble("warning"),
                                jsonObject.getDouble("danger"),
                                jsonObject.getDouble("latitude"),
                                jsonObject.getDouble("longitude")
                        );
                        dataList.add(data);
                    }
                    predictions.postValue(dataList);
                    isDataLoaded = true;
                } catch (JSONException | IOException e) {
                    errorMessage.postValue("Data parsing error: " + e.getMessage());
                }
            }
        });
    }
}
