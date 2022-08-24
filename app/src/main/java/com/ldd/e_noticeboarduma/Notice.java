package com.ldd.e_noticeboarduma;

import android.graphics.Bitmap;

import java.util.Date;

public class Notice {
    private Bitmap PostImage;
    private String PostDescription;
    private java.util.Date Date;


    public Notice() {
    }

    public Notice(Bitmap postImage, String postDescription, Date date) {
        PostImage = postImage;
        PostDescription = postDescription;
        Date = date;
    }

    public Bitmap getPostImage() {
        return PostImage;
    }

    public void setPostImage(Bitmap postImage) {
        PostImage = postImage;
    }

    public String getPostDescription() {
        return PostDescription;
    }

    public void setPostDescription(String postDescription) {
        PostDescription = postDescription;
    }

    public Date getDate() {
        return Date;
    }

    public void setDate(Date date) {
        Date = date;
    }
}
