package com.example.yourroom.announcement;

import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_ADDRESS;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_DAILY;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_DESCRIPTION;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_EMAIL;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_FAVORITES;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_IMAGE_URI;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_INTENT;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_NON_RESIDENTIAL;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_PHONE;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_PRICE;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_RESIDENTIAL;
import static com.example.yourroom.Constant.USER_KEY_ANNOUNCEMENT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.yourroom.ListRoomAdapter;
import com.example.yourroom.R;
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

public class DailyActivity extends AppCompatActivity implements ListRoomAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private ListRoomAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;

    private List<Announcement> mAnnouncement;

    private ActionBar actionBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        if (getSupportActionBar() != null){
            actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mAuth = FirebaseAuth.getInstance();
        mRecyclerView = findViewById(R.id.recycler_view_daily);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_circle_daily);

        mAnnouncement = new ArrayList<>();

        mAdapter = new ListRoomAdapter(getApplicationContext(), mAnnouncement);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(DailyActivity.this);
        mStorage = FirebaseStorage.getInstance();
        Intent i = getIntent();
        if(i != null){
            switch (i.getStringExtra(ANNOUNCEMENT_KEY_INTENT)){
                case ANNOUNCEMENT_KEY_RESIDENTIAL:
                    mDatabaseRef = FirebaseDatabase.getInstance().getReference(
                            USER_KEY_ANNOUNCEMENT + "/ " + ANNOUNCEMENT_KEY_RESIDENTIAL
                                    + "/ " + ANNOUNCEMENT_KEY_DAILY);
                    break;
                case ANNOUNCEMENT_KEY_NON_RESIDENTIAL:
                    mDatabaseRef = FirebaseDatabase.getInstance().getReference(
                            USER_KEY_ANNOUNCEMENT + "/ " + ANNOUNCEMENT_KEY_NON_RESIDENTIAL
                                    + "/ " + ANNOUNCEMENT_KEY_DAILY);
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
                Toast.makeText(DailyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        Announcement selectedItem = mAnnouncement.get(position);
        Intent intent = new Intent(DailyActivity.this, ItemActivity.class);
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
        FirebaseUser cUser = mAuth.getCurrentUser();
        String userId = cUser.getUid();
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
        Announcement newAnnouncement = new Announcement(email, phone, address, price, description, mImageUri.toString(), userId);
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
                Toast.makeText(DailyActivity.this, "Удалено", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        // Hides the status and navigation bar until the user clicks
        // on the screeen.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
