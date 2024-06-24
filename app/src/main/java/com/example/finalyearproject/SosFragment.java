package com.example.finalyearproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import model.FireDepartment;
import adapter.SosAdapter;

public class SosFragment extends Fragment implements Filterable {

    private ListView sosListView;
    private SearchView search_bar;
    private SosAdapter adapter;
    FloatingActionButton callFab;
    static int PERMISSION_CODE = 100;


    List<FireDepartment> fireDepartmentList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Finding the view
        sosListView = view.findViewById(R.id.sosListView);
        search_bar = view.findViewById(R.id.search_bar);
        callFab = view.findViewById(R.id.callFab);

        // Populate the fire department list
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Kuala Selangor", "Kuala Selangor", "Selangor", "03-3289 1444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Batu Arang", "Batu Arang", "Selangor", "03-6035 2444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Rawang", "Rawang", "Selangor", "03-6092 4444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Ampang", "Ampang", "Selangor", "03-4292 4444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Sabak Bernam", "Sabak Bernam", "Selangor", "03-3216 1444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Klang Utara", "Klang", "Selangor", "03-3371 4444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Pelabuhan Klang", "Pelabuhan Klang", "Selangor", "03-3168 4444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Kajang", "Kajang", "Selangor", "03-8736 4444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Bandar Tun Hussein Onn", "Cheras", "Selangor", "03-9081 8634"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Semenyih", "Semenyih", "Selangor", "03-8723 9589"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Banting", "Langat", "Selangor", "03-3187 1444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Sepang", "Sepang", "Selangor", "03-3142 1333"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Klang Selatan", "Klang", "Selangor", "03-3342 4444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Damansara", "Petaling Jaya", "Selangor", "03-7729 4444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Bandar Baru Bangi", "Bandar Baru Bangi", "Selangor", "03-8925 4444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Petaling Jaya", "Petaling Jaya", "Selangor", "03-7958 5857"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Selayang", "Batu Caves", "Selangor", "03-6136 5944"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Sungai Buloh", "Sungai Buloh", "Selangor", "03-6157 5055"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Sri Andalas", "Klang", "Selangor", "03-3373 4444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat KLIA", "Sepang", "Selangor", "03-8787 4970"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Seri Kembangan", "Seri Kembangan", "Selangor", "03-8941 6281"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Subang Jaya", "Subang Jaya", "Selangor", "03-5634 9444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Bukit Jelutong", "Shah Alam", "Selangor", "03-7846 4211"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Cyberjaya", "Cyberjaya", "Selangor", "03-8318 4944"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Sekinchan", "Sekinchan", "Selangor", "03-3241 8075"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Sungai Besar", "Sungai Besar", "Selangor", "03-3224 6434"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Jalan Penchala", "Petaling Jaya", "Selangor", "03-7772 8144"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Puchong", "Puchong", "Selangor", "03-8070 7879"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Telok Panglima Garang", "Jenjarom", "Selangor", "03-3181 4440"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat KaparPersiaran Hamzah Alang", "Klang", "Selangor", "03-3392 0984"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Rawang", "Rawang", "Selangor", "03-6021 4144"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Bukit Dengkil Jalan Banting", "Dengkil", "Selangor", "03-8768 7806"));

        // Create and set up the adapter
        adapter = new SosAdapter(getContext(), fireDepartmentList);
        sosListView.setAdapter(adapter);

        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Filter the list based on the query when the user submits
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the list based on the query as the user types
                adapter.getFilter().filter(newText);
                return false;
            }
        });



    }


    @Override
    public Filter getFilter() {

        return null;
    }
}
