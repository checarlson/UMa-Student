package com.ldd.e_noticeboarduma;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ViewresultsActivity extends AppCompatActivity {

    private ArrayList<Results> resultsArrayList;
    private ResultsRVAdapter resultsRVAdapter;
    private RecyclerView resultsRV;
    private ProgressBar loadingPB;
    private TextView fname, program, matricule, department, year, seme;
    private String name, prog, mat, dept, yr, sem;
    private ImageButton download;
    private LinearLayout layout;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewresults);

        this.setTitle(R.string.result);

        fname = findViewById(R.id.fname);
        program = findViewById(R.id.progr);
        matricule = findViewById(R.id.matr);
        department = findViewById(R.id.depart);
        year = findViewById(R.id.year);
        seme = findViewById(R.id.semes);
        download = findViewById(R.id.downlo);
        layout = findViewById(R.id.linear);

        loadingPB = findViewById(R.id.idProgressBar);
        resultsRV = findViewById(R.id.resultList);
        resultsArrayList = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        if (!(extras == null)) {
            name = extras.getString("name");
            mat = extras.getString("mat");
            prog = extras.getString("prog");
            dept = extras.getString("dept");
            yr = extras.getString("year");
            sem = extras.getString("semester");

            fname.setText("Name: "+ name);
            matricule.setText("Matricule: " + mat);
            program.setText("Cycle: " + prog);
            department.setText("Department: " + dept);
            year.setText("Academic Year: "+yr);
            seme.setText("Semester: "+sem);
        }

        // calling a method to
        // load recycler view.
        prepareResultsRV();

        // calling a method to get
        // the data from database.
        getDataFromServer();

        btnDown();


        if ( isExternalStorageWritable() ) {

            File appDirectory = new File( Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/com.ldd.e_noticeboarduma/files");
            File logDirectory = new File( appDirectory + "/logs" );
            File logFile = new File( logDirectory, "logcat_" + System.currentTimeMillis() + ".txt" );

            // create app folder
            if ( !appDirectory.exists() ) {
                appDirectory.mkdir();
            }

            // create log folder
            if ( !logDirectory.exists() ) {
                logDirectory.mkdir();
            }

            // clear the previous logcat and then write the new one to the file
            try {
//                Process process/* = Runtime.getRuntime().exec("logcat -c")*/;
               Process process = Runtime.getRuntime().exec("logcat -f " + logFile);
            } catch ( IOException e ) {
                e.printStackTrace();
            }

        } else if ( isExternalStorageReadable() ) {
            // only readable
        } else {
            // not accessible
        }
    }


    private void getDataFromServer() {
        resultsArrayList.clear();
        // Configure Query with our query.
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Results");
        query.addAscendingOrder("code");
        query.whereEqualTo("matricule", mat);
        query.whereEqualTo("semester", sem);
        query.whereEqualTo("year", yr);
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
                        int n = i + 1;
                        String code = objects.get(i).getString("code");
                        String title = objects.get(i).getString("title");
                        String semester = objects.get(i).getString("semester");
                        String year = objects.get(i).getString("year");
                        String matricule = objects.get(i).getString("matricule");
                        String cc = objects.get(i).getString("cc");
                        String ee = objects.get(i).getString("ee");
                        String tpe = objects.get(i).getString("tpe");
                        String total = objects.get(i).getString("total");
                        String moy = objects.get(i).getString("moy");
                        String dec = objects.get(i).getString("dec");
                        String men = objects.get(i).getString("men");

                        // on below line we are adding data to our array list.
                        resultsArrayList.add(new Results(n, matricule, semester, year, code, title, cc, ee, tpe, total, moy, dec, men));
                    }
                    // notifying adapter class on adding new data.
                    resultsRVAdapter.notifyDataSetChanged();
                } else {
                    // handling error if we get any error.
                    Toast.makeText(ViewresultsActivity.this, "Fail to get data..", Toast.LENGTH_SHORT).show();
                    //                    agame.setText("Check your network and try again");
                }
                loadingPB.setVisibility(View.GONE);
            }
        });
    }


    private void prepareResultsRV() {
        resultsRV.setHasFixedSize(true);
        resultsRV.setLayoutManager(new LinearLayoutManager(this));

        // adding our array list to our recycler view adapter class.
        resultsRVAdapter = new ResultsRVAdapter(this, resultsArrayList);

        // setting adapter to our recycler view.
        resultsRV.setAdapter(resultsRVAdapter);
    }

    private void btnDown(){
        download.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                Log.d("size", "" + layout.getWidth() + " " + layout.getWidth());
                bitmap = loadBitmap(layout, layout.getWidth(), layout.getHeight());
//                createPdf();
                checkPermissionAndOpenStorage();
            }
        });
    }

    private Bitmap loadBitmap(View v, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    private void createPdf() {
        WindowManager windowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float width = displayMetrics.widthPixels;
        float height = displayMetrics.heightPixels;
        int convertWidth = (int) width, convertHeight = (int) height;

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(/*842*/convertHeight, /*595*/convertWidth, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);
        bitmap = Bitmap.createScaledBitmap(bitmap, /*842*/convertHeight, /*595*/convertWidth, true);

        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        //target pdf document
//        String targetPdf = "/sdcard/page.pdf";
        String targetPdf = Environment.getExternalStorageDirectory().getAbsolutePath()+"/UMaStudent";
        File dir = new File(targetPdf);
        if (!dir.exists())
            dir.mkdirs();

        Random random = new  Random();
        String rand = String.valueOf(random.nextInt(1000)) ;

        File file = new File(dir, Utility.mat+rand+".pdf");
        try {
            document.writeTo(new FileOutputStream(file));

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something is wrong try again "+ e.getMessage(), Toast.LENGTH_SHORT).show();

        }
            //after close the document
            document.close();
            Toast.makeText(this, "Successfully Downloaded", Toast.LENGTH_SHORT).show();

            //this method is use to open pdf file that was downloaded
//            openPdf();
    }

    private void openPdf() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/PDF", "results.pdf");
        if (file.exists()){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Successfully Downloaded", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 6) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createPdf();
            }
        }
    }

    /**
   /*  * This method is user to grant access to gallery
     */
    void checkPermissionAndOpenStorage() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 6);
        } else {
            createPdf();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ViewresultsActivity.this, StudentProfile.class));
        finish();
    }


    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if ( Environment.MEDIA_MOUNTED.equals( state ) ) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if ( Environment.MEDIA_MOUNTED.equals( state ) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals( state ) ) {
            return true;
        }
        return false;
    }


}