package com.example.yourroom.Announcement;


import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_INTENT;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_NON_RESIDENTIAL;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_RENT;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_RESIDENTIAL;
import static com.example.yourroom.Constant.USER_KEY_ANNOUNCEMENT;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourroom.Constant;
import com.example.yourroom.ListRoomAdapter;
import com.example.yourroom.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class RentActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ListRoomAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;

    private List<Announcement> mAnnouncement;

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);

        if (getSupportActionBar() != null){
            actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mRecyclerView = findViewById(R.id.recycler_view_rent);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_circle_rent);

        mAnnouncement = new ArrayList<>();

        mAdapter = new ListRoomAdapter(getApplicationContext(), mAnnouncement);
        mRecyclerView.setAdapter(mAdapter);

        mStorage = FirebaseStorage.getInstance();

        Intent i = getIntent();
        if(i != null){
            switch (i.getStringExtra(ANNOUNCEMENT_KEY_INTENT)){
                case ANNOUNCEMENT_KEY_RESIDENTIAL:
                    mDatabaseRef = FirebaseDatabase.getInstance().getReference(
                            USER_KEY_ANNOUNCEMENT + "/ " + ANNOUNCEMENT_KEY_RESIDENTIAL
                                    + "/ " + ANNOUNCEMENT_KEY_RENT);
                    break;
                case ANNOUNCEMENT_KEY_NON_RESIDENTIAL:
                    mDatabaseRef = FirebaseDatabase.getInstance().getReference(
                            USER_KEY_ANNOUNCEMENT + "/ " + ANNOUNCEMENT_KEY_NON_RESIDENTIAL
                                    + "/ " + ANNOUNCEMENT_KEY_RENT);
                    break;
            }
        }
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mAnnouncement.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Announcement announcement = postSnapshot.getValue(Announcement.class);
                    announcement.setKey(postSnapshot.getKey());
                    mAnnouncement.add(announcement);
                }
                mAdapter.notifyDataSetChanged();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }
}
