package com.example.yourroom.domain.usecases;

import com.example.yourroom.domain.Announcement;
import com.example.yourroom.domain.AnnouncementRepository;

public class GetAnnouncementItemUseCase {

    private final AnnouncementRepository announcementRepository;

    public GetAnnouncementItemUseCase(AnnouncementRepository AnnouncementRepository){
        this.announcementRepository = AnnouncementRepository;
    }
    Announcement getAnnouncementItem(int announcementId){
        return announcementRepository.getAnnouncementItem(announcementId);
    }
}
