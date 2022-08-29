package com.example.yourroom.presentation.fragments;

import static com.example.yourroom.data.Constant.ANNOUNCEMENT_KEY_FAVORITES;
import static com.example.yourroom.data.Constant.ANNOUNCEMENT_KEY_NON_RESIDENTIAL;
import static com.example.yourroom.data.Constant.ANNOUNCEMENT_KEY_RESIDENTIAL;
import static com.example.yourroom.data.Constant.USER_KEY_ANNOUNCEMENT;

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

import com.example.yourroom.domain.Announcement;
import com.example.yourroom.presentation.adapters.ListRoomAdapter;
import com.example.yourroom.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ListRoomAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;

    private List<Announcement> mAnnouncement;

    private Button button_residential, button_non_residential;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        mOnClick();
    }

    private void init(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_favorites);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        button_residential = view.findViewById(R.id.button_living_quarters);
        button_non_residential = view.findViewById(R.id.button_non_residential_premises);
        mProgressCircle = view.findViewById(R.id.progress_circle_favorites);

        mAnnouncement = new ArrayList<>();

        mAdapter = new ListRoomAdapter(getActivity().getApplicationContext(), mAnnouncement);
        mRecyclerView.setAdapter(mAdapter);
        Log.d("Scan", mAdapter.toString());
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    private void mOnClick() {
        button_residential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseRef = FirebaseDatabase.getInstance().getReference(
                        USER_KEY_ANNOUNCEMENT + "/ " + ANNOUNCEMENT_KEY_FAVORITES
                                + "/ " + ANNOUNCEMENT_KEY_RESIDENTIAL);
                data();
            }
        });
        button_non_residential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseRef = FirebaseDatabase.getInstance().getReference(
                        USER_KEY_ANNOUNCEMENT + "/ " + ANNOUNCEMENT_KEY_FAVORITES
                                + "/ " + ANNOUNCEMENT_KEY_NON_RESIDENTIAL);
                data();
            }
        });
    }
    private void data(){
       // FirebaseUser cUser = mAuth.getCurrentUser();
      //  String userEmail = cUser.getEmail();
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mAnnouncement.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Announcement announcement = postSnapshot.getValue(Announcement.class);
                    //User user = postSnapshot.getValue(User.class);
                    announcement.setKey(postSnapshot.getKey());
                    mAnnouncement.add(announcement);
                   // assert announcement != null;
                    //if (announcement.email.equals(userEmail)){

                   // }

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
}