package com.example.yourroom.domain.usecases;

import com.example.yourroom.domain.Announcement;
import com.example.yourroom.domain.AnnouncementRepository;

public class DeleteAnnouncementItemUseCase {

    private final AnnouncementRepository announcementRepository;

    public DeleteAnnouncementItemUseCase(AnnouncementRepository AnnouncementRepository){
        this.announcementRepository = AnnouncementRepository;
    }

    public void addAnnouncement(Announcement announcement){
        announcementRepository.deleteAnnouncement(announcement);
    }
}
