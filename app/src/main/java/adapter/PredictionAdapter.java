package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Layout;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.example.finalyearproject.R;
import model.InfoPredict;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PredictionAdapter extends RecyclerView.Adapter<PredictionAdapter.PredictionViewHolder> {
    private final Context mContext;
    private List<InfoPredict> mData;

    public PredictionAdapter(Context mContext, List<InfoPredict> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    public void setData(List<InfoPredict> newData) {
        this.mData = newData;
        notifyDataSetChanged();
    }

    @Override
    public PredictionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.prediction_cardview, parent, false);
        return new PredictionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PredictionViewHolder holder, int position) {
        InfoPredict infoPredict = mData.get(position);
        holder.textDistrict.setText(infoPredict.getDistrict());
        holder.textLocation.setText(infoPredict.getLocation());

        // Check the highest predicted water level among the three days
        double highestWaterLevel = Math.max(infoPredict.getDay1(), Math.max(infoPredict.getDay2(), infoPredict.getDay3()));
        double floodLevel = highestWaterLevel - infoPredict.getDanger();
        String floodLevelString = String.format("%.2f", floodLevel);
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
        String predictedDay = daysOfWeek[(dayOfWeek + highestWaterLevelIndex + 1) % 7];

        if (highestWaterLevel < infoPredict.getAlert()) {
            holder.textAlert.setText("Status: Normal");
            holder.textPrediction.setText("No flood is predicted within 3 days");
            holder.textWL.setText("Highest: " + formattedWaterLevel + " Meter");
            holder.alertLayout.setBackgroundResource(R.color.lightgreen);
        } else if (highestWaterLevel >= infoPredict.getAlert() && highestWaterLevel < infoPredict.getWarning()) {
            holder.textAlert.setText("Status: Alert");
            holder.textPrediction.setText("No flood is predicted within 3 days");
            holder.textWL.setText("Highest: " + formattedWaterLevel + " Meter");
            holder.alertLayout.setBackgroundResource(R.color.lemonyellow);
        } else if (highestWaterLevel >= infoPredict.getWarning() && highestWaterLevel <= infoPredict.getDanger()) {
            holder.textAlert.setText("Status: Warning");
            holder.textPrediction.setText("No flood is predicted within 3 days");
            holder.textWL.setText("Highest: " + formattedWaterLevel + " Meter");
            holder.alertLayout.setBackgroundResource(R.color.orange);
        } else if (highestWaterLevel > infoPredict.getDanger()) {
            holder.textAlert.setText("Status: Danger");
            holder.textPrediction.setText("Flood is predicted on " + predictedDay);
            holder.textWL.setText("Flood Level: " + floodLevelString + " Meter");
            holder.alertLayout.setBackgroundResource(R.color.redpastel);
        }

        setupBarChart(holder.anyChartView, infoPredict);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setupBarChart(AnyChartView anyChartView, InfoPredict infoPredict) {
        APIlib.getInstance().setActiveAnyChartView(anyChartView);

        Cartesian cartesian = AnyChart.column();
        cartesian.crosshair().yLabel(true).yStroke((Stroke) null, null, null, (String) null, (String) null);

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        String day1 = daysOfWeek[(dayOfWeek + 1) % 7];
        String day2 = daysOfWeek[(dayOfWeek + 2) % 7];
        String day3 = daysOfWeek[(dayOfWeek + 3) % 7];

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry(day1, infoPredict.getDay1()));
        data.add(new ValueDataEntry(day2, infoPredict.getDay2()));
        data.add(new ValueDataEntry(day3, infoPredict.getDay3()));

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

        cartesian.lineMarker(0)
                .value(infoPredict.getNormal())
                .stroke("2 #00FF00")
                .layout(Layout.HORIZONTAL);

        cartesian.lineMarker(1)
                .value(infoPredict.getAlert())
                .stroke("2 #FFFF00")
                .layout(Layout.HORIZONTAL);

        cartesian.lineMarker(2)
                .value(infoPredict.getWarning())
                .stroke("2 #FFA500")
                .layout(Layout.HORIZONTAL);

        cartesian.lineMarker(3)
                .value(infoPredict.getDanger())
                .stroke("2 #FF0000")
                .layout(Layout.HORIZONTAL);

        anyChartView.setChart(cartesian);
    }

    public static class PredictionViewHolder extends RecyclerView.ViewHolder {
        TextView textDistrict, textLocation, textAlert, textPrediction, textWL;
        RelativeLayout alertLayout;
        AnyChartView anyChartView;

        public PredictionViewHolder(View itemView) {
            super(itemView);
            textDistrict = itemView.findViewById(R.id.textDistrict);
            textLocation = itemView.findViewById(R.id.textLocation);
            textAlert = itemView.findViewById(R.id.textAlert);
            textPrediction = itemView.findViewById(R.id.textPrediction);
            textWL = itemView.findViewById(R.id.textWL);
            alertLayout = itemView.findViewById(R.id.alertLayout);
            anyChartView = itemView.findViewById(R.id.any_chart_view);
        }
    }
}
