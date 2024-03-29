package com.example.yourroom.ui;

import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_DAILY;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_NEW_BUILDINGS;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_NON_RESIDENTIAL;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_RENT;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_RESIDENTIAL;
import static com.example.yourroom.Constant.ANNOUNCEMENT_KEY_WAREHOUSE;
import static com.example.yourroom.Constant.USER_KEY;
import static com.example.yourroom.Constant.USER_KEY_ANNOUNCEMENT;
import static com.example.yourroom.Constant.USER_KEY_ANNOUNCEMENT_ALL;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.yourroom.announcement.Announcement;
import com.example.yourroom.R;
import com.example.yourroom.announcement.DailyActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;


public class AddFragment extends Fragment {
    private EditText mEditTextFileAddress, mEditTextFilePrice, mEditTextFileDescription,
            mEditTextFileEmail, mEditTextFilePhone;
    private RadioGroup mRadioGroupRent, mRadioGroupResidential;
    private RadioButton residential_Rb, warehouseRb, rentRb, dailyRb, residentialRb, non_residential_Rb;
    private Button mButtonAdd;
    private ImageButton mImageButtonAdd;

    String keyHierarchy, keyHierarchyImage, keyHierarchyMy;

    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseAll;
    private FirebaseAuth mAuth;
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
        mOnClick();
        FirebaseUser cUser = mAuth.getCurrentUser();
        assert cUser != null;
    }

    private void init(@NonNull View view) {
        mRadioGroupRent = view.findViewById(R.id.rent_daily_RG);
        mRadioGroupResidential = view.findViewById(R.id.residential_or_non_RG);
        residential_Rb = view.findViewById(R.id.residentialRb);
        warehouseRb = view.findViewById(R.id.warehouseRb);

        rentRb = mRadioGroupRent.findViewById(mRadioGroupRent.getCheckedRadioButtonId());
        dailyRb= mRadioGroupRent.findViewById(mRadioGroupRent.getCheckedRadioButtonId());
        residentialRb = mRadioGroupResidential.findViewById(mRadioGroupResidential.getCheckedRadioButtonId());
        non_residential_Rb = mRadioGroupResidential.findViewById(mRadioGroupResidential.getCheckedRadioButtonId());

        mEditTextFileAddress = view.findViewById(R.id.edit_text_address);
        mEditTextFilePrice = view.findViewById(R.id.edit_text_price);
        // Описание добавить внутырь
        mEditTextFileDescription = view.findViewById(R.id.edit_text_description);
        //
        mEditTextFileEmail = view.findViewById(R.id.edit_text_email);
        mEditTextFilePhone = view.findViewById(R.id.edit_text_phone);

        mButtonAdd = view.findViewById(R.id.button_publish);
        Drawable myDrawable = getResources().getDrawable(R.drawable.ic_baseline_public_24);
        mButtonAdd.setCompoundDrawablesWithIntrinsicBounds(myDrawable, null, null, null);
        mImageButtonAdd = view.findViewById(R.id.image_button_add);

        mAuth = FirebaseAuth.getInstance();
    }
    private void mOnClick(){
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
        //////////////////////////////
        //////////RadioButton/////////
        mRadioGroupRent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rentRb:
                        keyHierarchy = ANNOUNCEMENT_KEY_RENT;
                        residential_Rb.setVisibility(View.VISIBLE);
                        break;
                    case R.id.dailyRb:
                        keyHierarchy = ANNOUNCEMENT_KEY_DAILY;
                        residential_Rb.setVisibility(View.VISIBLE);
                        break;
                    case R.id.newBuildingsRb:
                        keyHierarchy = ANNOUNCEMENT_KEY_NEW_BUILDINGS;
                        residential_Rb.setVisibility(View.VISIBLE);
                        break;
                    case R.id.warehouseRb:
                        keyHierarchy = ANNOUNCEMENT_KEY_WAREHOUSE;
                        residential_Rb.setVisibility(View.GONE);
                }
            }
        });
        mRadioGroupResidential.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.residentialRb:
                        mStorageRef = FirebaseStorage.getInstance().getReference(
                                USER_KEY_ANNOUNCEMENT + "/ " + ANNOUNCEMENT_KEY_RESIDENTIAL + "/ " + keyHierarchy);
                        mDatabaseRef = FirebaseDatabase.getInstance().getReference(
                                USER_KEY_ANNOUNCEMENT + "/ " + ANNOUNCEMENT_KEY_RESIDENTIAL + "/ " + keyHierarchy);

                        keyHierarchyMy = USER_KEY_ANNOUNCEMENT + "/ " + ANNOUNCEMENT_KEY_RESIDENTIAL + "/ " + keyHierarchy;
                        warehouseRb.setVisibility(View.GONE);

                        keyHierarchyImage = USER_KEY_ANNOUNCEMENT + "/ " + ANNOUNCEMENT_KEY_RESIDENTIAL + "/ " + keyHierarchy;
                        break;
                    case R.id.non_residential_Rb:
                        mStorageRef = FirebaseStorage.getInstance().getReference(
                                USER_KEY_ANNOUNCEMENT + "/ " + ANNOUNCEMENT_KEY_NON_RESIDENTIAL + "/ " + keyHierarchy);
                        mDatabaseRef = FirebaseDatabase.getInstance().getReference(
                                USER_KEY_ANNOUNCEMENT + "/ " + ANNOUNCEMENT_KEY_NON_RESIDENTIAL + "/ " + keyHierarchy);

                        keyHierarchyMy = USER_KEY_ANNOUNCEMENT + "/ " + ANNOUNCEMENT_KEY_RESIDENTIAL + "/ " + keyHierarchy;
                        warehouseRb.setVisibility(View.VISIBLE);
                        keyHierarchyImage = USER_KEY_ANNOUNCEMENT + "/ " + ANNOUNCEMENT_KEY_NON_RESIDENTIAL + "/ " + keyHierarchy;
                        break;
                }
            }
        });
    }
    private void saveUser(){
        String id = mDatabaseRef.push().getKey();
        mDatabaseAll = FirebaseDatabase.getInstance().getReference(USER_KEY_ANNOUNCEMENT_ALL);
        String idAll = mDatabaseAll.push().getKey();
        FirebaseUser cUser = mAuth.getCurrentUser();
        String userId = cUser.getUid();
        String email = mEditTextFileEmail.getText().toString();
        String phone = mEditTextFilePhone.getText().toString();
        String address = mEditTextFileAddress.getText().toString();
        String price = mEditTextFilePrice.getText().toString();
        String description = mEditTextFileDescription.getText().toString();

        warehouseRb = mRadioGroupRent.findViewById(mRadioGroupRent.getCheckedRadioButtonId());

        Announcement newAnnouncement = new Announcement(email, phone, price + " грн", address, description, mImageUri.toString(), userId);

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(address)
                && !TextUtils.isEmpty(price) && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(mImageUri.toString())
                && warehouseRb != null || rentRb != null || dailyRb != null || residentialRb != null || non_residential_Rb != null) {

            if (id != null) mDatabaseRef.child(id).setValue(newAnnouncement);
            if (idAll != null) mDatabaseAll.child(idAll).setValue(newAnnouncement);
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