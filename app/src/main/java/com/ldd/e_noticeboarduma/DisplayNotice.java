package com.ldd.e_noticeboarduma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class DisplayNotice extends AppCompatActivity {
    public Bitmap bitmap = null;
    public ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notice);
        logo = findViewById(R.id.image);

        edit();
    }

    public void edit(){
       /* Bundle extras = getIntent().getExtras();
        if (extras != null) {
            *//*Uri*//*
            Uri path = (Uri) extras.get("image"); //image from gallery
//            bitmap = (Bitmap) extras.get("image");  //image from camera
            bitmap = Utility.bitImage;

            logo.setImageBitmap(bitmap);
        }*/

        bitmap = Utility.bitImage;

        logo.setImageBitmap(bitmap);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DisplayNotice.this, NoticeActivity.class));
        finish();
    }
}