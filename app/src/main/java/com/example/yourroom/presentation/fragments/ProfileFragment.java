package com.example.yourroom.presentation.fragments;

import static com.example.yourroom.data.Constant.ANNOUNCEMENT_KEY_ADDRESS;
import static com.example.yourroom.data.Constant.ANNOUNCEMENT_KEY_DESCRIPTION;
import static com.example.yourroom.data.Constant.ANNOUNCEMENT_KEY_EMAIL;
import static com.example.yourroom.data.Constant.ANNOUNCEMENT_KEY_IMAGE_URI;
import static com.example.yourroom.data.Constant.ANNOUNCEMENT_KEY_PHONE;
import static com.example.yourroom.data.Constant.ANNOUNCEMENT_KEY_PRICE;
import static com.example.yourroom.data.Constant.USER_KEY;
import static com.example.yourroom.data.Constant.USER_KEY_ANNOUNCEMENT_ALL;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yourroom.presentation.adapters.ListRoomAdapter;
import com.example.yourroom.presentation.activity.LoginActivity;
import com.example.yourroom.R;
import com.example.yourroom.domain.User;
import com.example.yourroom.domain.Announcement;
import com.example.yourroom.presentation.announcement.ItemActivity;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment implements ListRoomAdapter.OnItemClickListener{

    private Button btExit;
    private TextView id_person, email, id_phone;
    private ImageView imageAvatar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;

    private RecyclerView mRecyclerView;
    private ListRoomAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;

    private List<Announcement> mAnnouncement;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        mOnClick();
        getDataFromDB();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(USER_KEY_ANNOUNCEMENT_ALL);
        FirebaseUser cUser = mAuth.getCurrentUser();
        String userId = cUser.getUid();
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mAnnouncement.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Announcement announcement = postSnapshot.getValue(Announcement.class);
                    assert announcement != null;
                    User user = postSnapshot.getValue(User.class);
                    assert user != null;
                    if (announcement.userId.equals(userId)){
                        announcement.setKey(postSnapshot.getKey());
                        mAnnouncement.add(announcement);
                    }
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

    private void init(View view){
        btExit = view.findViewById(R.id.exit);
        Drawable exitDrawable = getResources().getDrawable(R.drawable.ic_baseline_exit_to_app_24);
        btExit.setCompoundDrawablesWithIntrinsicBounds(null, null, exitDrawable, null);
        id_person = view.findViewById(R.id.id_person_tv);
        email = view.findViewById(R.id.email_tv);
        id_phone = view.findViewById(R.id.id_phone);
        imageAvatar = view.findViewById(R.id.avatar_iv);
        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
        mRecyclerView = view.findViewById(R.id.recyclerView_profile);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mProgressCircle = view.findViewById(R.id.progress_circle_profile);

        mAnnouncement = new ArrayList<>();

        mAdapter = new ListRoomAdapter(getActivity().getApplicationContext(), mAnnouncement);
        mRecyclerView.setAdapter(mAdapter);
        Log.d("Scan", mAdapter.toString());

        mStorage = FirebaseStorage.getInstance();

    }

    private void mOnClick(){
        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);
            }
        });
    }
    private void getDataFromDB() {
        FirebaseUser cUser = mAuth.getCurrentUser();
        if(cUser != null){
            String userId = cUser.getUid();
            String userEmail = cUser.getEmail();
            final String[] userPhone = new String[1];
            final String[] userImage = new String[1];
            ValueEventListener vListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        User user = ds.getValue(User.class);
                        assert user != null;
                        if (user.id.equals(userId)){
                            id_person.setText(userId);
                            email.setText(userEmail);
                            userPhone[0] = user.phone;
                            id_phone.setText(userPhone[0]);
                            userImage[0] = user.imageUrl;
                           Picasso.get().load(userImage[0]).into(imageAvatar);

                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };
            mDataBase.addValueEventListener(vListener);
        }
        else {
            Toast.makeText(getActivity(), "Вы не зарегистрировались" ,Toast.LENGTH_SHORT).show();
        }

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
    public void onDeleteClick(int position) {
        Announcement selectedItem = mAnnouncement.get(position);
        String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(getActivity(), "Удалено", Toast.LENGTH_SHORT).show();
            }
        });
    }
}