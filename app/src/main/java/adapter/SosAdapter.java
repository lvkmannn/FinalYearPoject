package adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.finalyearproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import model.FireDepartment;

public class SosAdapter extends BaseAdapter implements Filterable {
    private final Context mContext;
    private List<FireDepartment> mData;
    private List<FireDepartment> mDataFiltered;
    private final LayoutInflater mInflater;


    public SosAdapter(Context mContext, List<FireDepartment> mData) {
        this.mContext = mContext;
        this.mData = mData;
        this.mDataFiltered = mData;
        mInflater = LayoutInflater.from(mContext);
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

        if (v == null){
            v = mInflater.inflate(R.layout.sos_cardview, parent, false);
        }

        TextView FireDepartmentText = v.findViewById(R.id.FireDepartmentText);
        TextView districtText = v.findViewById(R.id.locationText);
        TextView stateText = v.findViewById(R.id.districtText);
        TextView phoneNumText = v.findViewById(R.id.phoneNumText);
        FloatingActionButton callFab = v.findViewById(R.id.callFab);

        FireDepartment fireDepartment = mDataFiltered.get(position);
        FireDepartmentText.setText(fireDepartment.getName());
        districtText.setText(fireDepartment.getDistrict());
        stateText.setText(fireDepartment.getState());
        phoneNumText.setText(fireDepartment.getPhoneNum());


        callFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireDepartment calledFireDepartment = mData.get(position);
                String phoneNum = calledFireDepartment.getPhoneNum();

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNum));
                mContext.startActivity(intent);
            }
        });

        return v;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase().trim();

                if (charString.isEmpty()) {
                    mDataFiltered = mData;
                } else {
                    List<FireDepartment> filteredList = new ArrayList<>();
                    for (FireDepartment fireDepartment : mData) {
                        if (fireDepartment.getName().toLowerCase().contains(charString)) {
                            filteredList.add(fireDepartment);
                        }
                    }
                    mDataFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mDataFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mDataFiltered = (ArrayList<FireDepartment>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
