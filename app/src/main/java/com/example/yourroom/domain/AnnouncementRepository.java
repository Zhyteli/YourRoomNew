package com.example.yourroom.domain;

public interface AnnouncementRepository {
    void addAnnouncement(String path, String pathImage, Announcement announcement);

    void deleteAnnouncement(Announcement announcement);

    void getAnnouncementList();

    Announcement getAnnouncementItem(int announcementId);

}
