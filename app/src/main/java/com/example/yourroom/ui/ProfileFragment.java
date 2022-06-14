package com.example.yourroom.ui;

import static com.example.yourroom.Constant.USER_KEY;
import static com.example.yourroom.Constant.USER_KEY_ANNOUNCEMENT;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yourroom.Announcement.RentActivity;
import com.example.yourroom.LoginActivity;
import com.example.yourroom.R;
import com.example.yourroom.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;


public class ProfileFragment extends Fragment {

    private Button btExit;
    private TextView id_person, email, id_phone;
    private ImageView imageAvatar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;
    private StorageReference mStorageRef;

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
    }

    private void init(View view){
        btExit = view.findViewById(R.id.exit);
        id_person = view.findViewById(R.id.id_person_tv);
        email = view.findViewById(R.id.email_tv);
        id_phone = view.findViewById(R.id.id_phone);
        imageAvatar = view.findViewById(R.id.avatar_iv);
        mStorageRef = FirebaseStorage.getInstance().getReference(USER_KEY);
        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
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
}