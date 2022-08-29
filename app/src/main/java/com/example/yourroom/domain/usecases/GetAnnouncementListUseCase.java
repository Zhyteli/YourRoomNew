package com.example.yourroom.domain.usecases;

import androidx.lifecycle.LiveData;

import com.example.yourroom.domain.Announcement;
import com.example.yourroom.domain.AnnouncementRepository;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class GetAnnouncementListUseCase {

    private final AnnouncementRepository announcementRepository;

    public GetAnnouncementListUseCase(AnnouncementRepository AnnouncementRepository){
        this.announcementRepository = AnnouncementRepository;
    }
    void getAnnouncementList(){
        announcementRepository.getAnnouncementList();
    }
}
