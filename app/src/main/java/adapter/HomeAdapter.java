package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.finalyearproject.R;

import java.util.ArrayList;
import java.util.List;

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

        TextView textDistrict = v.findViewById(R.id.textDistrict);
        TextView textLocation = v.findViewById(R.id.textLocation);
        TextView textWL = v.findViewById(R.id.textWL);
        TextView textHourlyRainfall = v.findViewById(R.id.textHourlyRainfall);
        TextView textTodayRainfall = v.findViewById(R.id.textTodayRainfall);
        TextView textStatus = v.findViewById(R.id.textStatus);

        InfoWeather infoWeather = mDataFiltered.get(position);
        textDistrict.setText(infoWeather.getDistrictName());
        textLocation.setText(infoWeather.getStationName());
        textWL.setText("Water Level: " + Double.toString(infoWeather.getWaterLevel()) + " m");
        textHourlyRainfall.setText("Total Rainfall (hourly): " + Double.toString(infoWeather.getHourlyRainfall()) + " mm");
        textTodayRainfall.setText("Total Rainfall (day): " + Double.toString(infoWeather.getTodayRainfall()) + " mm");
        textStatus.setText("Nothing to show yet");


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
