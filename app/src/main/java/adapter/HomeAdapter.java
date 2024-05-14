package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.finalyearproject.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import model.InfoWeather;

public class HomeAdapter extends BaseAdapter implements Filterable{

    private final Context mContext;
    private List<InfoWeather> mData;

    private List<InfoWeather> mDataFiltered;


    public HomeAdapter(Context mContext, List<InfoWeather> mData){
        this.mContext = mContext;
        this.mData = mData;
        this.mDataFiltered = mData;
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

        if (v==null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            v = inflater.inflate(R.layout.home_cardview, parent, false);
        }

        //TextView textDatetime = v.findViewById(R.id.textDatetime);

        TextView textDistrict = v.findViewById(R.id.textDistrict);
        TextView textLocation = v.findViewById(R.id.textLocation);
        TextView textWL = v.findViewById(R.id.textWL);
        TextView textHourlyRainfall = v.findViewById(R.id.textHourlyRainfall);
        TextView textTodayRainfall = v.findViewById(R.id.textTodayRainfall);
        TextView textStatus = v.findViewById(R.id.textStatus);
        ImageView imageView = v.findViewById(R.id.imageView);

        // Find linear layout
        LinearLayout linearLayoutWL = v.findViewById(R.id.linearLayoutWL);

        // Set the datetime
        /*Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a", Locale.getDefault());
        String dt = sdf.format(calendar.getTime());
        textDatetime.setText("Last Updated: " + dt);*/

        // Set the information
        InfoWeather infoWeather = mDataFiltered.get(position);
        textDistrict.setText(infoWeather.getDistrictName());
        textLocation.setText(infoWeather.getStationName());
        textWL.setText("Water Level: " + infoWeather.getWaterLevel() + " m");
        textHourlyRainfall.setText("Total Rainfall (hourly): " + infoWeather.getHourlyRainfall() + " mm");
        textTodayRainfall.setText("Total Rainfall (day): " + infoWeather.getTodayRainfall() + " mm");
        
        if (infoWeather.getWaterLevel() < infoWeather.getAlert()){
            textStatus.setText("Status: Normal");
            linearLayoutWL.setBackgroundResource(R.color.lightgreen);
            imageView.setImageResource(R.drawable.safe_24);
        }
        else if (infoWeather.getWaterLevel() >= infoWeather.getAlert() && infoWeather.getWaterLevel() < infoWeather.getWarning()) {
            textStatus.setText("Status: Alert");
            linearLayoutWL.setBackgroundResource(R.color.lemonyellow);
            imageView.setImageResource(R.drawable.baseline_crisis_alert_24);
        } else if (infoWeather.getWaterLevel() >= infoWeather.getWarning() && infoWeather.getWaterLevel() < infoWeather.getDanger()) {
            textStatus.setText("Status: Warning");
            linearLayoutWL.setBackgroundResource(R.color.orange);
            imageView.setImageResource(R.drawable.baseline_warning_24);
        } else if (infoWeather.getWaterLevel() >= infoWeather.getDanger() ) {
            textStatus.setText("Status: Danger");
            linearLayoutWL.setBackgroundResource(R.color.redpastel);
            imageView.setImageResource(R.drawable.danger_24);
        }


        return v;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString().toLowerCase().trim();

                if (charString.isEmpty()){
                    mDataFiltered = mData;
                }else {
                    List<InfoWeather> filteredList = new ArrayList<>();
                    for (InfoWeather infoWeather:mData){
                        if (infoWeather.getStationName().toLowerCase().contains(charString) || infoWeather.getDistrictName().toLowerCase().contains(charString)){
                            filteredList.add(infoWeather);
                        }
                    }
                    mDataFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mDataFiltered;
                return  filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mDataFiltered = (ArrayList<InfoWeather>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
