package com.ldd.e_noticeboarduma;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoticeActivity extends AppCompatActivity {
    public static TextView description, agame;
    public ImageView postImage;

    // creating variables for our recycler view,
    // progressbar, array list and adapter class.
    private RecyclerView noticeRV;
    private ProgressBar loadingPB;
    private ArrayList<Notice> noticeArrayList;
    private MainNoticeRVAdapter noticeRVAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
//    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        // initializing our views.
        loadingPB = findViewById(R.id.idProgressBar);
        noticeRV = findViewById(R.id.idRVLeagues);
        noticeArrayList = new ArrayList<>();

        Utility.appendLog("NoticeActivity: onCreate");

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
        swipeRefreshLayout = findViewById(R.id.refreshLayout);
        

        swipe();
    }

    private void getDataFromServer() {
        noticeArrayList.clear();
        Utility.appendLog("getting notice from sever");

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
                    Toast.makeText(NoticeActivity.this, "Fail to get data..", Toast.LENGTH_SHORT).show();
                    loadingPB.setVisibility(View.GONE);
                    agame.setText("Check your network and try again");
                    Utility.appendLog("failed in getting notice from sever");
                }
            }
        });
    }

    private void prepareCourseRV() {
        noticeRV.setHasFixedSize(true);
        noticeRV.setLayoutManager(new LinearLayoutManager(this));

        // adding our array list to our recycler view adapter class.
        noticeRVAdapter = new MainNoticeRVAdapter(this, noticeArrayList);

        // setting adapter to our recycler view.
        noticeRV.setAdapter(noticeRVAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


 @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.about:
                AlertDialog.Builder about = new AlertDialog.Builder(NoticeActivity.this);
                AlertDialog.Builder builder = about.setMessage(R.string.about)
                        .setCancelable(true)
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert1 = about.create();
                alert1.setTitle(R.string.us);
                alert1.show();
                break;

            case R.id.exit:
                AlertDialog.Builder a_builder = new AlertDialog.Builder(NoticeActivity.this);
                AlertDialog.Builder builder1 = a_builder.setMessage(R.string.are_you_sure)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = a_builder.create();
                alert.setTitle(R.string.exit);
                alert.show();
                break;

            case R.id.login:
                startActivity(new Intent(NoticeActivity.this, LoginActivity.class));
                finish();
                break;

            case R.id.student:
                startActivity(new Intent(NoticeActivity.this, SignInEmailActivity.class));
                finish();
                break;

            default:
                return super.onOptionsItemSelected(item);

        }

        return true;
    }

    private void swipe(){
        // Refresh  the layout
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        // Your code goes here

                        getDataFromServer();
                        prepareCourseRV();

                        // This line is important as it explicitly
                        // refreshes only once
                        // If "true" it implicitly refreshes forever
                        swipeRefreshLayout.setRefreshing(false);
                        loadingPB.setVisibility(View.GONE);
                        Utility.appendLog("activity refreshed");
                    }
                }
        );
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}