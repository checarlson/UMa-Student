package com.ldd.e_noticeboarduma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

public class StudentProfile extends AppCompatActivity {
    private Spinner yr, sem;
    private ViewFlipper vf;
    private TextView homeV, resultV;
    private TextView name1, program, matricule, department, name11;
    private ArrayList<Results> resultsArrayList;
    private ResultsRVAdapter resultsRVAdapter;
    private RecyclerView resultsRV;
    private ProgressBar loadingPB;
    private Button get;
    Student student = new Student();
    private String year = "null", seme = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        yr = findViewById(R.id.year);
        sem = findViewById(R.id.semester);
        vf = findViewById(R.id.vf);
        homeV = findViewById(R.id.home);
        resultV = findViewById(R.id.result);
        get = findViewById(R.id.get);


        name1 = findViewById(R.id.name);
        name11 = findViewById(R.id.name1);
        program = findViewById(R.id.prog);
        matricule = findViewById(R.id.mat);
        department = findViewById(R.id.dept);



        spinnerMethod();
        flipHome();
        flipResult();
        getResults();
        try {
            getStudent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getStudent() {

        final ProgressDialog progressDialog = new ProgressDialog(StudentProfile.this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle(R.string.please_wait);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        // Configure Query with our query.
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Students");
        query.whereEqualTo("Matricule", Utility.mat);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                if (e == null){

                    String name = object.getString("Name");
                    String program1 = object.getString("Program");
                    String department1 = object.getString("Department");
                    String mat = object.getString("Matricule");


                    student.setName(name);
                    student.setMatricule(mat);
                    student.setDepartment(department1);
                    student.setProgram(program1);

                    name1.setText(student.getName()+" ("+student.getMatricule()+")");
                    name11.setText(student.getName());
                    program.setText(student.getProgram());
                    matricule.setText(student.getMatricule());
                    department.setText(student.getDepartment());

                }else {
                    // handling error if we get any error.
                    Toast.makeText(StudentProfile.this, "Fail to getting Student info...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void spinnerMethod(){
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.academic_year));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yr.setAdapter(myAdapter);

        ArrayAdapter<String> myAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.semester));
        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sem.setAdapter(myAdapter1);

        sem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1){
                    seme = "1";
                }else if (i == 2){
                    seme ="2";
                }else if (i == 3){
                    seme = "3";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        yr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1){
                    year = "2019/2020";
                }else if (i == 2){
                    year = "2020/2021";
                }else if (i == 3){
                    year = "2021/2022";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void flipResult(){
        resultV.setOnClickListener(view -> {
            resultV.setBackgroundResource(R.drawable.button_idle);
            homeV.setBackgroundResource(R.drawable.button_idle1);
            resultV.setTextColor(Color.BLACK);
            homeV.setTextColor(Color.WHITE);
            vf.setDisplayedChild(1);
        });
    }

    public void flipHome(){
        homeV.setOnClickListener(view -> {
            homeV.setBackgroundResource(R.drawable.button_idle);
            resultV.setBackgroundResource(R.drawable.button_idle1);
            homeV.setTextColor(Color.BLACK);
            resultV.setTextColor(Color.WHITE);
            vf.setDisplayedChild(0);
        });
    }

    public void getResults(){
        get.setOnClickListener(view -> {
            if (!year.equals("null") && !seme.equals("0")){
                Intent intent = new Intent(getApplicationContext(), ViewresultsActivity.class);
                intent.putExtra("name", student.getName());
                intent.putExtra("mat", student.getMatricule());
                intent.putExtra("prog", student.getProgram());
                intent.putExtra("dept", student.getDepartment());
                intent.putExtra("semester", seme);
                intent.putExtra("year", year);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(getApplicationContext(), "Select Academic Year and Semester", Toast.LENGTH_SHORT).show();
            }


        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(StudentProfile.this, NoticeActivity.class));
        finish();
    }
}