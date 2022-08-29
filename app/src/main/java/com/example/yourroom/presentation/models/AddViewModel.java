package com.example.yourroom.presentation.models;

import static com.example.yourroom.data.Constant.USER_KEY_ANNOUNCEMENT;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.example.yourroom.data.AnnouncementRepositoryImpl;
import com.example.yourroom.domain.Announcement;
import com.example.yourroom.domain.usecases.AddAnnouncementUseCase;
import com.example.yourroom.domain.usecases.GetAnnouncementItemUseCase;
import com.example.yourroom.domain.usecases.GetAnnouncementListUseCase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;


public class AddViewModel extends ViewModel {

    private AnnouncementRepositoryImpl repository = new AnnouncementRepositoryImpl();

    private AddAnnouncementUseCase addAnnouncementUseCase = new AddAnnouncementUseCase(repository);
    private GetAnnouncementListUseCase getAnnouncementListUseCase = new GetAnnouncementListUseCase(repository);
    private GetAnnouncementItemUseCase getAnnouncementItemUseCase = new GetAnnouncementItemUseCase(repository);
    final Uri[] mImageUri = new Uri[1];

    public void addAnnouncement(String email, String phone, String address, String price, String description, String path, String pathImage){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser cUser = mAuth.getCurrentUser();
        String userId = cUser.getUid();

        Announcement newAnnouncement = new Announcement(
                email,
                phone,
                price + " грн",
                address, description,
                mImageUri.toString(),
                userId
        );
        addAnnouncementUseCase.addAnnouncement(path, pathImage, newAnnouncement);
    }

    public void uploadImage(ImageButton mImageButtonAdd, String mStorageRef){
        Bitmap bitmap = ((BitmapDrawable) mImageButtonAdd.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] byteArray = baos.toByteArray();
        final StorageReference mRef = repository.setStorageRef(mStorageRef)
                .child(System.currentTimeMillis() + USER_KEY_ANNOUNCEMENT);
        UploadTask up = mRef.putBytes(byteArray);
        Task<Uri> task = up.continueWithTask(task1 -> mRef.getDownloadUrl())
                .addOnCompleteListener(task12 -> mImageUri[0] = task12.getResult());
    }
}
