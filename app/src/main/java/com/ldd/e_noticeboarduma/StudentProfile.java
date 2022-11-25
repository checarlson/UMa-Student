package com.ldd.e_noticeboarduma;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

public class StudentProfile extends AppCompatActivity {
    private Spinner yr, sem;
    private ViewFlipper vf;
    private TextView homeV, resultV, menu;
    private TextView name1, program, matricule, department, name11, level, phone, email, dob;
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
        menu = findViewById(R.id.menu);


        name1 = findViewById(R.id.name);
        name11 = findViewById(R.id.name1);
        program = findViewById(R.id.prog);
        matricule = findViewById(R.id.mat);
        department = findViewById(R.id.dept);
        level = findViewById(R.id.level);
        dob = findViewById(R.id.dob);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);



        spinnerMethod();
        flipHome();
        flipResult();
        getResults();
        try {
            getStudent();
        } catch (Exception e) {
            e.printStackTrace();
        }

        showPopupMenu();
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
                    String lev = object.getString("Level");
                    String dob1 = object.getString("DOB");
                    String phone1 = object.getString("Phone");
                    String email1 = object.getString("Email");


                    student.setName(name);
                    student.setMatricule(mat);
                    student.setDepartment(department1);
                    student.setProgram(program1);
                    student.setLevel(lev);
                    student.setEmail(email1);
                    student.setDob(dob1);
                    student.setPhone(phone1);

                    name1.setText(student.getName()+" ("+student.getMatricule()+")");
                    name11.setText(student.getName());
                    program.setText(student.getProgram());
                    matricule.setText(student.getMatricule());
                    department.setText(student.getDepartment());
                    level.setText(student.getLevel());
                    dob.setText(student.getDob());
                    email.setText(student.getEmail());
                    phone.setText(student.getPhone());

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
                intent.putExtra("level", student.getLevel());
                intent.putExtra("semester", seme);
                intent.putExtra("year", year);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(getApplicationContext(), "Select Academic Year and Semester", Toast.LENGTH_SHORT).show();
            }


        });
    }

  private void showPopupMenu(){
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(StudentProfile.this, menu);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.main_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.about:
                                AlertDialog.Builder about = new AlertDialog.Builder(StudentProfile.this);
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
                                AlertDialog.Builder a_builder = new AlertDialog.Builder(StudentProfile.this);
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

                            default:
                                return false;

                        }

                        return true;
                    }
                });
                popup.show();
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