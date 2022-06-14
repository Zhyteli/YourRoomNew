package com.example.yourroom.Announcement;

import com.google.firebase.database.Exclude;

public class Announcement {
    public String key, email, phone, price, address, description, imageUrl;

    public Announcement() {
        //empty constructor needed
    }

    public Announcement(String email, String phone, String price, String address, String description, String imageUrl) {
        this.email = email;
        this.phone = phone;
        this.price = price;
        this.address = address;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
