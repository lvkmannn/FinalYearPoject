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
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Kuala Selangor", "Kuala Selangor", "Selangor", "0332891444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Batu Arang", "Batu Arang", "Selangor", "0360352444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Rawang", "Rawang", "Selangor", "0360924444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Ampang", "Ampang", "Selangor", "0342924444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Sabak Bernam", "Sabak Bernam", "Selangor", "0332161444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Klang Utara", "Klang", "Selangor", "0333714444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Pelabuhan Klang", "Pelabuhan Klang", "Selangor", "0331684444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Kajang", "Kajang", "Selangor", "0387364444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Bandar Tun Hussein Onn", "Cheras", "Selangor", "0390818634"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Semenyih", "Semenyih", "Selangor", "0387239589"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Banting", "Langat", "Selangor", "0331871444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Sepang", "Sepang", "Selangor", "0331421333"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Klang Selatan", "Klang", "Selangor", "0333424444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Damansara", "Petaling Jaya", "Selangor", "0377294444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Bandar Baru Bangi", "Bandar Baru Bangi", "Selangor", "0389254444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Petaling Jaya", "Petaling Jaya", "Selangor", "0379585857"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Selayang", "Batu Caves", "Selangor", "0361365944"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Sungai Buloh", "Sungai Buloh", "Selangor", "0361575055"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Sri Andalas", "Klang", "Selangor", "0333734444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat KLIA", "Sepang", "Selangor", "0387874970"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Seri Kembangan", "Seri Kembangan", "Selangor", "0389416281"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Subang Jaya", "Subang Jaya", "Selangor", "0356349444"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Bukit Jelutong", "Shah Alam", "Selangor", "0378464211"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Cyberjaya", "Cyberjaya", "Selangor", "0383184944"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Sekinchan", "Sekinchan", "Selangor", "0332418075"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Sungai Besar", "Sungai Besar", "Selangor", "0332246434"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Jalan Penchala", "Petaling Jaya", "Selangor", "0377728144"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Puchong", "Puchong", "Selangor", "0380707879"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Telok Panglima Garang", "Jenjarom", "Selangor", "0331814440"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat KaparPersiaran Hamzah Alang", "Klang", "Selangor", "0333920984"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Rawang", "Rawang", "Selangor", "0360214144"));
        fireDepartmentList.add(new FireDepartment("Balai Bomba dan Penyelamat Bukit Dengkil Jalan Banting", "Dengkil", "Selangor", "0387687806"));

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
