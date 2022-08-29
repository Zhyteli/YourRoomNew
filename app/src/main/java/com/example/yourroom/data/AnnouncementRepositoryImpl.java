package com.example.yourroom.data;

import static com.example.yourroom.data.Constant.USER_KEY_ANNOUNCEMENT_AD;
import static com.example.yourroom.data.Constant.USER_KEY_ANNOUNCEMENT_ALL;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.yourroom.domain.Announcement;
import com.example.yourroom.domain.AnnouncementRepository;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class AnnouncementRepositoryImpl implements AnnouncementRepository {

    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseAll;
    private StorageReference mStorageRef;
    private ValueEventListener mDBListener;

    private MutableLiveData<List<Announcement>> _announcementListVD;
    private LiveData<List<Announcement>> announcementListVD;
    private List<Announcement> announcementList;

    public LiveData<List<Announcement>> get_announcementList() {
        return announcementListVD;
    }

    @Override
    public void addAnnouncement(String path, String pathImage, Announcement announcement) {
        setDatabaseRef(path);
        setStorageRef(pathImage);
        String id = mDatabaseRef.push().getKey();
        String idAll = mDatabaseAll.push().getKey();
        if (id != null) mDatabaseRef.child(id).setValue(announcement);
        if (idAll != null) mDatabaseAll.child(idAll).setValue(announcement);
    }

    @Override
    public void deleteAnnouncement(Announcement announcement) {

    }

    @Override
    public void getAnnouncementList() {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(USER_KEY_ANNOUNCEMENT_AD);
    }

    @Override
    public Announcement getAnnouncementItem(int announcementId) {
        Announcement selectedItem = announcementList.get(announcementId);
        return selectedItem;
    }

    private void setDatabaseRef(String path) {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(path);
        mDatabaseAll = FirebaseDatabase.getInstance().getReference(USER_KEY_ANNOUNCEMENT_ALL);
    }

    public StorageReference setStorageRef(String pathImage){
        return mStorageRef = FirebaseStorage.getInstance().getReference(pathImage);
    }
}
