package com.note11.easy_calling.data;

import android.net.Uri;

import java.util.Calendar;

public class CallLogModel {
    private String name, phone, duration;
    private Calendar date;
    private int type;
    private Uri photo;

    public CallLogModel() { }

    public CallLogModel(String name, String phone, String duration, Calendar date, int type, Uri photo) {
        this.name = name;
        this.phone = phone;
        this.duration = duration;
        this.date = date;
        this.type = type;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Uri getPhoto() {
        return photo;
    }

    public void setPhoto(Uri photo) {
        this.photo = photo;
    }
}
