package com.example.yourroom.domain.usecases;

import com.example.yourroom.domain.Announcement;
import com.example.yourroom.domain.AnnouncementRepository;

public class AddAnnouncementUseCase {
    private final AnnouncementRepository announcementRepository;
    public AddAnnouncementUseCase(AnnouncementRepository AnnouncementRepository){
        this.announcementRepository = AnnouncementRepository;
    }
    public void addAnnouncement(String path, String pathImage, Announcement announcement){
        announcementRepository.addAnnouncement(path, pathImage, announcement);
    }
}
