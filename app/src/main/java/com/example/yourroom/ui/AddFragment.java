package com.example.yourroom.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yourroom.Announcement.Announcement;
import com.example.yourroom.MainActivity;
import com.example.yourroom.R;
import com.example.yourroom.Upload;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;


public class AddFragment extends Fragment {
    private EditText mEditTextFileAddress, mEditTextFilePrice, mEditTextFileDescription,
            mEditTextFileEmail, mEditTextFilePhone;
    private RadioButton mRadioButtonRent, mRadioButtonDaily, mRadioButtonResidential, mRadioButtonNon_residential;
    private Button mButtonAdd;
    private ImageButton mImageButtonAdd;
    private String USER_KEY_ANNOUNCEMENT = "Announcement";

    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private Uri mImageUri;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        mImageButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage();
            }
        });
        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mImageButtonAdd != null) {
                    uploadImage();
                }
            }
        });

    }
    private void init(@NonNull View view) {
        mEditTextFileAddress = view.findViewById(R.id.edit_text_address);
        mEditTextFilePrice = view.findViewById(R.id.edit_text_price);
        // Описание добавить внутырь
        mEditTextFileDescription = view.findViewById(R.id.edit_text_description);
        //
        mEditTextFileEmail = view.findViewById(R.id.edit_text_email);
        mEditTextFilePhone = view.findViewById(R.id.edit_text_phone);

        mRadioButtonDaily = view.findViewById(R.id.dailyRb);
        mRadioButtonRent = view.findViewById(R.id.rentRb);
        mRadioButtonResidential = view.findViewById(R.id.residentialRb);
        mRadioButtonNon_residential = view.findViewById(R.id.non_residential_Rb);

        mButtonAdd = view.findViewById(R.id.button_publish);
        mImageButtonAdd = view.findViewById(R.id.image_button_add);

        mStorageRef = FirebaseStorage.getInstance().getReference(USER_KEY_ANNOUNCEMENT);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(USER_KEY_ANNOUNCEMENT);

    }
    private void saveUser(){
        String id = mDatabaseRef.push().getKey();
        String email = mEditTextFileEmail.getText().toString();
        String phone = mEditTextFilePhone.getText().toString();
        String address = mEditTextFileAddress.getText().toString();
        String price = mEditTextFilePrice.getText().toString();
        String description = mEditTextFileDescription.getText().toString();
        Announcement newAnnouncement = new Announcement(email, phone, address, price, description, mImageUri.toString());

        if(!TextUtils.isEmpty(email)) {
            if (id != null) mDatabaseRef.child(id).setValue(newAnnouncement);
            Toast.makeText(getActivity(), "Сохранено", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(), "Пустое поле", Toast.LENGTH_SHORT).show();
        }
    }
    private void uploadImage(){
        Bitmap bitmap = ((BitmapDrawable) mImageButtonAdd.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] byteArray = baos.toByteArray();
        final StorageReference mRef = mStorageRef.child(System.currentTimeMillis() + USER_KEY_ANNOUNCEMENT);
        UploadTask up = mRef.putBytes(byteArray);
        Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return mRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                mImageUri = task.getResult();
                saveUser();
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && data != null){
            Log.d("MyLog","Image Url: " + data.getData());
            mImageButtonAdd.setImageURI(data.getData());
        }
    }
    // Сам вызов из памяти фотки
    private void getImage(){
        Intent intentChoose = new Intent();
        intentChoose.setType("image/*");
        intentChoose.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentChoose,1);
    }
}