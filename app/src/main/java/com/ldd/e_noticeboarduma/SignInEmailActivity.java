package com.ldd.e_noticeboarduma;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * This class collects the user email for sign in.
 */
public class SignInEmailActivity extends AppCompatActivity {
    /**
     * Continue button
     */
    public static Button btnContinue;
    /**
     * Edit text box that collect the user's email
     */
    public static EditText matricule, password;
    private TextView signUp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in_email);


//        this.setTitle(R.string.signin);

//        getSupportActionBar().hide();


        btnContinue = findViewById(R.id.btnSignIn);
        matricule = findViewById(R.id.mat);
        password = findViewById(R.id.pass);
        signUp = findViewById(R.id.clicksign);

        onClickContinue();
        signup();
    }

    /**
     * Click event of the continue button
      */
    public void onClickContinue() {
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                   onClickSignIn();
//                    startActivity(new Intent(SignInEmailActivity.this, StudentProfile.class));

                    getStudent();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Activity",e.getMessage());
                    Utility.appendLog(e.getMessage());
                }
            }
        });
    }


    public void onClickSignIn() throws Exception {
        String Email = matricule.getText().toString();
        String pass = password.getText().toString();

       /* if ((!pass.isEmpty() && db.getUser(email1, pass1)) || (!pass.isEmpty() && db.getUserByRole(email1, pass1))){
//
            Toast.makeText(getApplicationContext(), "Signed in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_SHORT).show();
        }*/
    }

    private void getStudent() throws Exception {
        final String user = matricule.getText().toString();
        final String pass = password.getText().toString();

        String pass1 = Utility.encrypt(pass,Utility.en_pass);

        final ProgressDialog progressDialog = new ProgressDialog(SignInEmailActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle(R.string.please_wait);
        progressDialog.setMessage("Logging in...");
        progressDialog.show();
        Utility.appendLog("attempt signing in");

        // Configure Query with our query.
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Students");
        query.whereEqualTo("Matricule", user);
        query.whereEqualTo("Password", pass1);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                if (e == null){

                    String name = object.getString("Name");
                    String program = object.getString("Program");
                    String department = object.getString("Department");
                    String mat = object.getString("Matricule");
                    String level = object.getString("Level");

                    Student student = new Student();
                    student.setName(name);
                    student.setMatricule(mat);
                    student.setDepartment(department);
                    student.setProgram(program);
                    student.setLevel(level);
                    Utility.mat = mat;

                    Intent intent = new Intent(getApplicationContext(), StudentProfile.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Signed in", Toast.LENGTH_SHORT).show();
                    Utility.appendLog("signing in successful");
                    finish();

                }else {
                    // handling error if we get any error.
                    Toast.makeText(SignInEmailActivity.this, "Fail to signing in..", Toast.LENGTH_SHORT).show();
                    Utility.appendLog("attempt signing in failed");
                }
            }
        });
    }


    public void signup(){
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInEmailActivity.this, SignUpActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
        startActivity(intent);
        finish();
    }

}