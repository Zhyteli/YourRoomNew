package com.example.yourroom.Announcement;


import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_FAVORITES;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_INTENT;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_NON_RESIDENTIAL;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_RENT;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_RESIDENTIAL;
import static com.example.yourroom.Constant.USER_KEY_ANNOUNCEMENT;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourroom.ListRoomAdapter;
import com.example.yourroom.R;
import com.example.yourroom.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class RentActivity extends AppCompatActivity implements ListRoomAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private ListRoomAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private FirebaseAuth mAuth;

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
        mAuth = FirebaseAuth.getInstance();
        mRecyclerView = findViewById(R.id.recycler_view_rent);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_circle_rent);

        mAnnouncement = new ArrayList<>();

        mAdapter = new ListRoomAdapter(getApplicationContext(), mAnnouncement);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(RentActivity.this);
        Log.d("Scan", mAdapter.toString());

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

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onWhatEverClick(int position) {
        FirebaseUser cUser = mAuth.getCurrentUser();

        Announcement selectedItem = mAnnouncement.get(position);
        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        String selectedKey = selectedItem.getKey();
        Intent i = getIntent();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        if(i != null){
            switch (i.getStringExtra(ANNOUNCEMENT_KEY_INTENT)){
                case ANNOUNCEMENT_KEY_RESIDENTIAL:
                    mDatabase = FirebaseDatabase.getInstance().getReference(
                            USER_KEY_ANNOUNCEMENT + "/ " + ANNOUNCEMENT_KEY_FAVORITES
                                    + "/ " + ANNOUNCEMENT_KEY_RESIDENTIAL);
                    break;
                case ANNOUNCEMENT_KEY_NON_RESIDENTIAL:
                    mDatabase = FirebaseDatabase.getInstance().getReference(
                            USER_KEY_ANNOUNCEMENT + "/ " + ANNOUNCEMENT_KEY_FAVORITES
                                    + "/ " + ANNOUNCEMENT_KEY_NON_RESIDENTIAL);
                    break;
            }
        }
        String id = mDatabase.push().getKey();

        String email = selectedItem.getEmail();
        String phone = selectedItem.getPhone();
        String address = selectedItem.getAddress();
        String price = selectedItem.getPrice();
        String description = selectedItem.getDescription();
        Uri mImageUri = Uri.parse(selectedItem.getImageUrl());
        Announcement newAnnouncement = new Announcement(email, phone, address, price, description, mImageUri.toString());
        if (id != null) mDatabase.child(id).setValue(newAnnouncement);

    }
    @Override
    public void onDeleteClick(int position) {
        Announcement selectedItem = mAnnouncement.get(position);
        String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(RentActivity.this, "Удалено", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }
}
