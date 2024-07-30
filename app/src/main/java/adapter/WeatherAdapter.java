package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalyearproject.R;

import java.util.List;

import model.Weather;

public class WeatherAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<Weather> mData;

    public WeatherAdapter(Context mContext, List<Weather> mData) {
        this.mContext = mContext;
        this.mData = mData;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            v = inflater.inflate(R.layout.weather_cardview, parent, false);
        }

        TextView locationText = v.findViewById(R.id.locationText);
        TextView dateText = v.findViewById(R.id.dateText);
        TextView morningText = v.findViewById(R.id.morningText);
        TextView afternoonText = v.findViewById(R.id.afternoonText);
        TextView nightText = v.findViewById(R.id.nightText);
        TextView summaryText = v.findViewById(R.id.summaryText);
        TextView whenText = v.findViewById(R.id.whenText);
        TextView minTempText = v.findViewById(R.id.minTempText);
        TextView maxTempText = v.findViewById(R.id.maxTempText);

        // For image
        ImageView imageView = v.findViewById(R.id.imageView);

        Weather weather = mData.get(position);
        locationText.setText(weather.getLocation_name());
        dateText.setText(weather.getDate());
        morningText.setText(weather.getMorning_forecast());
        afternoonText.setText(weather.getAfternoon_forecast());
        nightText.setText(weather.getNight_forecast());
        summaryText.setText(weather.getSummary_forecast());
        whenText.setText(weather.getSummary_when());
        minTempText.setText(weather.getMin_temp());
        maxTempText.setText(weather.getMax_temp());

        if (weather.getSummary_forecast().toLowerCase().contains("tiada hujan".toLowerCase())) {
            imageView.setImageResource(R.drawable.sunny);
        } else if (weather.getSummary_forecast().toLowerCase().contains("ribut petir".toLowerCase())) {
            imageView.setImageResource(R.drawable.thunderstorm);
        } else if (weather.getSummary_forecast().equals("Hujan") || weather.getSummary_forecast().contains("beberapa tempat".toLowerCase()) ) {
            imageView.setImageResource(R.drawable.baseline_grain_24);
        }


        return v;
    }
}
