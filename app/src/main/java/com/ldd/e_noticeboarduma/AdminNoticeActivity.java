package com.ldd.e_noticeboarduma;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdminNoticeActivity extends AppCompatActivity {
    public static TextView description, agame;
    public ImageView postImage;

    // creating variables for our recycler view,
    // progressbar, array list and adapter class.
    private RecyclerView noticeRV;
    private ProgressBar loadingPB;
    private ArrayList<Notice> noticeArrayList;
    private NoticeRVAdapter noticeRVAdapter;
//    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notice);

        // initializing our views.
        loadingPB = findViewById(R.id.idProgressBar);
        noticeRV = findViewById(R.id.idRVLeagues);
        noticeArrayList = new ArrayList<>();

        // calling a method to
        // load recycler view.
        prepareCourseRV();

        // calling a method to get
        // the data from database.
        getDataFromServer();

//        listView.findViewById(R.id.listview);
        description = findViewById(R.id.league_name);
        postImage = findViewById(R.id.league_logo);
        agame = findViewById(R.id.agame);

        fab();
    }

    private void getDataFromServer() {
        noticeArrayList.clear();

        // Configure Query with our query.
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Notice");
        query.addAscendingOrder("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                // in done method checking if the error is null or not.
                if (e == null) {
                    // Adding objects into the Array
                    // if error is not null we are getting data from
                    // our object and passing it to our array list.
                    for (int i = 0; i < objects.size(); i++) {

                        // on below line we are extracting our
                        // data and adding it ot our array list
                        byte[] logo = objects.get(i).getBytes("PostImage");
                        String name = objects.get(i).getString("PostDescription");
                        Date date = objects.get(i).getCreatedAt();


                        Bitmap bmp = BitmapFactory.decodeByteArray(logo, 0, logo.length);
                        // on below line we are adding data to our array list.
                        noticeArrayList.add(new Notice(bmp, name, date));
                    }
                    // notifying adapter class on adding new data.
                    noticeRVAdapter.notifyDataSetChanged();
                    loadingPB.setVisibility(View.GONE);
                } else {
                    // handling error if we get any error.
                    Toast.makeText(AdminNoticeActivity.this, "Fail to get data..", Toast.LENGTH_SHORT).show();
                    loadingPB.setVisibility(View.GONE);
                    agame.setText("Check your network and try again");
                }
            }
        });
    }

    private void prepareCourseRV() {
        noticeRV.setHasFixedSize(true);
        noticeRV.setLayoutManager(new LinearLayoutManager(this));

        // adding our array list to our recycler view adapter class.
        noticeRVAdapter = new NoticeRVAdapter(this, noticeArrayList);

        // setting adapter to our recycler view.
        noticeRV.setAdapter(noticeRVAdapter);
    }

    public  void fab(){
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminNoticeActivity.this, AddNotice.class));
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AdminNoticeActivity.this, NoticeActivity.class));
        finish();
    }

}