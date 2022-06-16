package com.example.yourroom.ui;

import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_ADDRESS;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_DESCRIPTION;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_EMAIL;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_IMAGE_URI;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_INTENT;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_NON_RESIDENTIAL;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_PHONE;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_PRICE;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_RENT;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_RESIDENTIAL;
import static com.example.yourroom.Constant.USER_KEY_ANNOUNCEMENT;
import static com.example.yourroom.Constant.USER_KEY_ANNOUNCEMENT_AD;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.yourroom.ListRoomAdapter;
import com.example.yourroom.MainActivity;
import com.example.yourroom.announcement.Announcement;
import com.example.yourroom.announcement.DailyActivity;
import com.example.yourroom.announcement.ItemActivity;
import com.example.yourroom.announcement.NewBuildingsActivity;
import com.example.yourroom.announcement.RentActivity;
import com.example.yourroom.announcement.WarehouseActivity;
import com.example.yourroom.R;
import com.example.yourroom.announcement.advertising.AdvertisingListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements AdvertisingListAdapter.OnItemClickListener {

    private Button mButtonRent, mButtonDaily, mButtonNewBuildings, mButtonWarehouse;
    private RecyclerView mRecyclerView;
    private AdvertisingListAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private FirebaseAuth mAuth;

    private List<Announcement> mAnnouncement;

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
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mAnnouncement.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Announcement announcement = postSnapshot.getValue(Announcement.class);
                    assert announcement != null;
                    announcement.setKey(postSnapshot.getKey());
                    mAnnouncement.add(announcement);
                }

                mAdapter.notifyDataSetChanged();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }
    private void init(@NonNull View view){
        mButtonRent = view.findViewById(R.id.buttonRent);
        mButtonDaily = view.findViewById(R.id.buttonDaily);
        mButtonNewBuildings = view.findViewById(R.id.buttonNewBuildings);
        mButtonWarehouse = view.findViewById(R.id.buttonWarehouse);

        mAuth = FirebaseAuth.getInstance();
        mRecyclerView = view.findViewById(R.id.recyclerView_advertising);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mProgressCircle = view.findViewById(R.id.progress_circle_advertising);

        mAnnouncement = new ArrayList<>();

        mAdapter = new AdvertisingListAdapter(getActivity().getApplicationContext(), mAnnouncement);
        mRecyclerView.setAdapter(mAdapter);
       //mAdapter.setOnItemClickListener();
        Log.d("Scan", mAdapter.toString());

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(USER_KEY_ANNOUNCEMENT_AD);
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

    @Override
    public void onItemClick(int position) {
        Announcement selectedItem = mAnnouncement.get(position);
        Intent intent = new Intent(getActivity(), ItemActivity.class);
        intent.putExtra(ANNOUNCEMENT_KEY_EMAIL, selectedItem.email);
        intent.putExtra(ANNOUNCEMENT_KEY_PHONE, selectedItem.phone);
        intent.putExtra(ANNOUNCEMENT_KEY_ADDRESS, selectedItem.address);
        intent.putExtra(ANNOUNCEMENT_KEY_PRICE, selectedItem.price);
        intent.putExtra(ANNOUNCEMENT_KEY_DESCRIPTION, selectedItem.description);
        intent.putExtra(ANNOUNCEMENT_KEY_IMAGE_URI, selectedItem.imageUrl);
        startActivity(intent);
    }

    @Override
    public void onWhatEverClick(int position) {

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }
}