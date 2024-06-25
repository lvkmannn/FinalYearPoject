package adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.core.cartesian.series.Line;
import com.anychart.charts.Cartesian;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.Layout;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.example.finalyearproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import model.GraphHome;
import model.InfoHome;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeAdapter extends BaseAdapter implements Filterable {

    private final Context mContext;
    private List<InfoHome> mData;
    private List<InfoHome> mDataFiltered;
    private List<GraphHome> dataGraph;

    public HomeAdapter(Context mContext, List<InfoHome> mData) {
        this.mContext = mContext;
        this.mData = mData;
        this.mDataFiltered = mData;
        this.dataGraph = new ArrayList<>();
    }

    public void setData(List<InfoHome> data) {
        this.mData = data;
        this.mDataFiltered = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataFiltered.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            v = inflater.inflate(R.layout.home_cardview, parent, false);
        }

        TextView textDistrict = v.findViewById(R.id.textDistrict);
        TextView textLocation = v.findViewById(R.id.textLocation);
        TextView textWL = v.findViewById(R.id.textWL);
        TextView textHourlyRainfall = v.findViewById(R.id.textHourlyRainfall);
        TextView textTodayRainfall = v.findViewById(R.id.textTodayRainfall);
        TextView textStatus = v.findViewById(R.id.textStatus);
        ImageView imageView = v.findViewById(R.id.imageView);

        LinearLayout linearLayoutWL = v.findViewById(R.id.linearLayoutWL);

        InfoHome infoHome = mDataFiltered.get(position);
        textDistrict.setText(infoHome.getDistrictName());
        textLocation.setText(infoHome.getStationName());
        textWL.setText("Water Level: " + infoHome.getWaterLevel() + " m");
        textHourlyRainfall.setText("Total Rainfall (hourly): " + infoHome.getHourlyRainfall() + " mm");
        textTodayRainfall.setText("Total Rainfall (day): " + infoHome.getTodayRainfall() + " mm");

        if (infoHome.getWaterLevel() < infoHome.getAlert()) {
            textStatus.setText("Status: Normal");
            linearLayoutWL.setBackgroundResource(R.color.lightgreen);
            imageView.setImageResource(R.drawable.safe_24);
        } else if (infoHome.getWaterLevel() >= infoHome.getAlert() && infoHome.getWaterLevel() < infoHome.getWarning()) {
            textStatus.setText("Status: Alert");
            linearLayoutWL.setBackgroundResource(R.color.lemonyellow);
            imageView.setImageResource(R.drawable.baseline_crisis_alert_24);
        } else if (infoHome.getWaterLevel() >= infoHome.getWarning() && infoHome.getWaterLevel() < infoHome.getDanger()) {
            textStatus.setText("Status: Warning");
            linearLayoutWL.setBackgroundResource(R.color.orange);
            imageView.setImageResource(R.drawable.baseline_warning_24);
        } else if (infoHome.getWaterLevel() >= infoHome.getDanger()) {
            textStatus.setText("Status: Danger");
            linearLayoutWL.setBackgroundResource(R.color.redpastel);
            imageView.setImageResource(R.drawable.danger_24);
        }

        Button btnGraph = v.findViewById(R.id.btnGraph);

        btnGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.graph_popup);

                dataGraph.clear(); // Clear previous data

                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .build();

                Request request = new Request.Builder()
                        .url("https://lvkmannn.pythonanywhere.com/get-items-by-id-for-last-12-hours/" + infoHome.getId())
                        .header("Connection", "close")
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(() -> {
                            String message = "Cannot fetch the data from the API: " + e.getMessage();
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(() -> {
                                String message = "Unexpected response code: " + response.code();
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                            });
                            return;
                        }

                        try {
                            JSONArray jsonArray = new JSONArray(response.body().string());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String lastUpdate = jsonObject.getString("lastUpdate");
                                double waterLevel = jsonObject.getDouble("waterLevel");

                                GraphHome data = new GraphHome(lastUpdate, waterLevel);
                                dataGraph.add(data);
                            }
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(() -> {
                                setupGraph(dialog, infoHome);
                            });
                        } catch (JSONException e) {
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(() -> {
                                String message = "JSON parsing error: " + e.getMessage();
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                });

                Button btnClose = dialog.findViewById(R.id.btnClose);
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        return v;
    }

    private void setupGraph(Dialog dialog, InfoHome infoHome) {
        AnyChartView anyChartView = dialog.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(dialog.findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.line();
        cartesian.animation(true);
        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair().yLabel(true).yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.title("12 Hours Water Level In Station " + infoHome.getStationName());
        cartesian.yAxis(0).title("Water Level (m)");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<>();
        for (GraphHome data : dataGraph) {
            seriesData.add(new CustomDataEntry(data.getLastUpdate(), data.getWaterLevel()));
        }

        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping seriesMapping = set.mapAs("{ x: 'x', value: 'value'}");

        Line series1 = cartesian.line(seriesMapping);
        series1.name(infoHome.getStationName());
        series1.hovered().markers().enabled(true);
        series1.hovered().markers().type(MarkerType.CIRCLE).size(4d);
        series1.tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(5).offsetY(5);

        // Enable markers for each data point
        series1.markers().enabled(true);
        series1.markers().type(MarkerType.CIRCLE).size(5d);  // Customize marker type and size as needed

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        // Add line markers with different colors
        cartesian.lineMarker(0)
                .value(infoHome.getNormal())
                .stroke("2 #00FF00")  // Green color for normal
                .layout(Layout.HORIZONTAL);

        cartesian.lineMarker(1)
                .value(infoHome.getAlert())
                .stroke("2 #FFFF00")  // Yellow color for alert
                .layout(Layout.HORIZONTAL);

        cartesian.lineMarker(2)
                .value(infoHome.getWarning())
                .stroke("2 #FFA500")  // Orange color for warning
                .layout(Layout.HORIZONTAL);

        cartesian.lineMarker(3)
                .value(infoHome.getDanger())
                .stroke("2 #FF0000")  // Red color for danger
                .layout(Layout.HORIZONTAL);

        anyChartView.setChart(cartesian);
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString().toLowerCase().trim();
                if (charString.isEmpty()) {
                    mDataFiltered = mData;
                } else {
                    List<InfoHome> filteredList = new ArrayList<>();
                    for (InfoHome infoHome : mData) {
                        if (infoHome.getStationName().toLowerCase().contains(charString) || infoHome.getDistrictName().toLowerCase().contains(charString)) {
                            filteredList.add(infoHome);
                        }
                    }
                    mDataFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mDataFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mDataFiltered = (ArrayList<InfoHome>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value) {
            super(x, value);
        }
    }
}

