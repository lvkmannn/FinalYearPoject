package ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.InfoPredict;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PredictionViewModel extends ViewModel {
    private final MutableLiveData<List<InfoPredict>> predictions = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private boolean isDataLoaded = false;  // Flag to check if data is loaded

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
                .url("http://10.0.2.2:5000/get-predictions-by-today-date")
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
                    List<InfoPredict> dataList = new ArrayList<>();

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

                        InfoPredict data = new InfoPredict(date, district, location, testLoss, MAE, MSE, RMSE, day1, day2, day3);
                        dataList.add(data);
                    }
                    predictions.postValue(dataList);
                    isDataLoaded = true;  // Set flag to true after data is loaded
                } catch (JSONException e) {
                    errorMessage.postValue("Data parsing error");
                }
            }
        });
    }
}

