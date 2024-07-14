package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.enums.Layout;
import com.anychart.graphics.vector.Stroke;
import com.example.finalyearproject.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import model.InfoPredict;

public class PredictionAdapter extends BaseAdapter {

    private final Context mContext;
    private List<InfoPredict> mData;

    public PredictionAdapter(Context mContext, List<InfoPredict> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    public void setData(List<InfoPredict> newData) {
        this.mData = newData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            v = inflater.inflate(R.layout.prediction_cardview, parent, false);
        }

        if (convertView == null) {
            TextView textDistrict = v.findViewById(R.id.textDistrict);
            TextView textLocation = v.findViewById(R.id.textLocation);
            TextView textAlert = v.findViewById(R.id.textAlert);
            TextView textPrediction = v.findViewById(R.id.textPrediction);
            TextView textWL = v.findViewById(R.id.textWL);
            RelativeLayout alertLayout = v.findViewById(R.id.alertLayout);

            // Setting up the text
            InfoPredict infoPredict = mData.get(position);
            textDistrict.setText(infoPredict.getDistrict());
            textLocation.setText(infoPredict.getLocation());

            // Check the highest predicted water level among the three days
            double highestWaterLevel = Math.max(infoPredict.getDay1(), Math.max(infoPredict.getDay2(), infoPredict.getDay3()));
            double floodLevel = highestWaterLevel - infoPredict.getDanger();
            String floodLevelString = String.format("%.2f", floodLevel);
            // Format the water level values to display only two decimal places
            String formattedWaterLevel = String.format("%.2f", highestWaterLevel);

            // Determine the index of the day with the highest water level
            int highestWaterLevelIndex;
            if (highestWaterLevel == infoPredict.getDay1()) {
                highestWaterLevelIndex = 0;
            } else if (highestWaterLevel == infoPredict.getDay2()) {
                highestWaterLevelIndex = 1;
            } else {
                highestWaterLevelIndex = 2;
            }

            // Get the current day of the week
            Calendar calendar = Calendar.getInstance();
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            // Calculate the day when the flood is predicted
            String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
            String predictedDay = daysOfWeek[(dayOfWeek + highestWaterLevelIndex + 1) % 7]; // +1 to start from the next day

            if (highestWaterLevel < infoPredict.getAlert()) {
                textAlert.setText("Status: Normal");
                textPrediction.setText("No flood is predicted within 3 days");
                textWL.setText("Highest: " + formattedWaterLevel + " Meter");
                alertLayout.setBackgroundResource(R.color.lightgreen);
            } else if (highestWaterLevel >= infoPredict.getAlert() && highestWaterLevel < infoPredict.getWarning()) {
                textAlert.setText("Status: Alert");
                textPrediction.setText("No flood is predicted within 3 days");
                textWL.setText("Highest: " + formattedWaterLevel + " Meter");
                alertLayout.setBackgroundResource(R.color.lemonyellow);
            } else if (highestWaterLevel >= infoPredict.getWarning() && highestWaterLevel <= infoPredict.getDanger()) {
                textAlert.setText("Status: Warning");
                textPrediction.setText("No flood is predicted within 3 days");
                textWL.setText("Highest: " + formattedWaterLevel + " Meter");
                alertLayout.setBackgroundResource(R.color.orange);
            } else if (highestWaterLevel > infoPredict.getDanger()) {
                textAlert.setText("Status: Danger");
                textPrediction.setText("Flood is predicted on " + predictedDay);
                textWL.setText("Flood Level: " + floodLevelString + " Meter");
                alertLayout.setBackgroundResource(R.color.redpastel);
            }

            setupBarChart(v, infoPredict);
        }

        return v;
    }

    public void setupBarChart(View v, InfoPredict infoPredict){
        // For the bar chart
        AnyChartView anyChartView = v.findViewById(R.id.any_chart_view);
        ProgressBar progressBar = v.findViewById(R.id.progress_bar);
        anyChartView.setProgressBar(progressBar);
        APIlib.getInstance().setActiveAnyChartView(anyChartView);

        // Creating the chart
        Cartesian cartesian = AnyChart.column();
        cartesian.crosshair().yLabel(true).yStroke((Stroke) null, null, null, (String) null, (String) null);

        // Get the current day of the week
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Create an array of day names
        String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        // Calculate the next three days starting from the next day
        String day1 = daysOfWeek[(dayOfWeek + 1) % 7];
        String day2 = daysOfWeek[(dayOfWeek + 2) % 7];
        String day3 = daysOfWeek[(dayOfWeek + 3) % 7];

        // Format the data to display only two decimal places
        String formattedDay1 = String.format("%.2f", infoPredict.getDay1());
        String formattedDay2 = String.format("%.2f", infoPredict.getDay2());
        String formattedDay3 = String.format("%.2f", infoPredict.getDay3());

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry(day1, Double.parseDouble(formattedDay1)));
        data.add(new ValueDataEntry(day2, Double.parseDouble(formattedDay2)));
        data.add(new ValueDataEntry(day3, Double.parseDouble(formattedDay3)));

        Column column = cartesian.column(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: } Meter");

        cartesian.animation(false);

        cartesian.title("3 Days Water Level Prediction");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: } Meter");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Day");
        cartesian.yAxis(0).title("Water Level");

        // Add line markers with different colors
        cartesian.lineMarker(0)
                .value(infoPredict.getNormal())
                .stroke("2 #00FF00")  // Green color for normal
                .layout(Layout.HORIZONTAL);

        cartesian.lineMarker(1)
                .value(infoPredict.getAlert())
                .stroke("2 #FFFF00")  // Yellow color for alert
                .layout(Layout.HORIZONTAL);

        cartesian.lineMarker(2)
                .value(infoPredict.getWarning())
                .stroke("2 #FFA500")  // Orange color for warning
                .layout(Layout.HORIZONTAL);

        cartesian.lineMarker(3)
                .value(infoPredict.getDanger())
                .stroke("2 #FF0000")  // Red color for danger
                .layout(Layout.HORIZONTAL);

        anyChartView.setChart(cartesian);
    }

}
