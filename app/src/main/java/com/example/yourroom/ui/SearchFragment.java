package com.example.yourroom.ui;

import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_DAILY;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_INTENT;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_INTENT_RES;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_NON_RESIDENTIAL;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_RENT;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_RESIDENTIAL;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.yourroom.Announcement.DailyActivity;
import com.example.yourroom.Announcement.NewBuildingsActivity;
import com.example.yourroom.Announcement.RentActivity;
import com.example.yourroom.Announcement.WarehouseActivity;
import com.example.yourroom.Constant;
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
        mOnClick();
    }
    private void init(@NonNull View view){
        mButtonRent = view.findViewById(R.id.buttonRent);
        mButtonDaily = view.findViewById(R.id.buttonDaily);
        mButtonNewBuildings = view.findViewById(R.id.buttonNewBuildings);
        mButtonWarehouse = view.findViewById(R.id.buttonWarehouse);

    }
    private void mOnClick(){
        mButtonRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rentDialog(RentActivity.class);
            }
        });
        mButtonDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rentDialog(DailyActivity.class);
            }
        });
        mButtonNewBuildings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rentDialog(NewBuildingsActivity.class);
            }
        });
        mButtonWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), WarehouseActivity.class);
                startActivity(i);
            }
        });
    }
    private void rentDialog(Class activity) {
        String[] options = {ANNOUNCEMENT_KEY_RESIDENTIAL, ANNOUNCEMENT_KEY_NON_RESIDENTIAL};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Ad search");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        Intent i = new Intent(getActivity(), activity);
                        i.putExtra(ANNOUNCEMENT_KEY_INTENT, ANNOUNCEMENT_KEY_RESIDENTIAL);
                        startActivity(i);
                        break;
                    case 1:
                        Intent intent = new Intent(getActivity(), activity);
                        intent.putExtra(ANNOUNCEMENT_KEY_INTENT, ANNOUNCEMENT_KEY_NON_RESIDENTIAL);
                        startActivity(intent);
                        break;
                }
            }
        });
        builder.create().show();
    }
}