package com.example.yourroom.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.yourroom.R;

public class SearchFragment extends Fragment {

    private Button mButtonRent, mButtonDaily, mButtonNewBuildings, mButtonWarehouse;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }
    private void init(@NonNull View view){
        mButtonRent = view.findViewById(R.id.buttonRent);
        mButtonDaily = view.findViewById(R.id.buttonDaily);
        mButtonNewBuildings = view.findViewById(R.id.buttonNewBuildings);
        mButtonWarehouse = view.findViewById(R.id.buttonWarehouse);
    }

}