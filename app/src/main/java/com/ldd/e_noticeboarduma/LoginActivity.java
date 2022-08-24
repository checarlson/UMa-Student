package com.ldd.e_noticeboarduma;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    public EditText username, password;
    public Button sign_in;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        sign_in = findViewById(R.id.login);


        login();
    }

    public void login() {
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickedLogin();
//                getDataFromServer();
            }
        });
    }

    public void onClickedLogin() {
        final String user = username.getText().toString();
        final String pass = password.getText().toString();

        if (!user.isEmpty() && !pass.isEmpty()) {
            // adding a progress dialog box
            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Logging in...");
            progressDialog.show();

            // getting login call back
            ParseUser.logInInBackground(user, pass, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (progressDialog.isShowing()) progressDialog.dismiss();
                            if (e == null) {
                                Toast.makeText(getApplicationContext(), "Signed in", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), AdminNoticeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error Logging in: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                password.setText(null);
                                username.setText(null);
                            }
                        }
                    }
            );
        } else {
            password.setError("enter password");
            username.setError("enter username");
        }
    }

    private void getDataFromServer()  {
        final String user = username.getText().toString();
        final String pass = password.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        // Configure Query with our query.
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Admin");
        query.whereEqualTo("name", user);
        query.whereEqualTo("password", pass);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                if (e == null){

                    String name = object.getString("name");
                    String pas = object.getString("password");

                    Toast.makeText(getApplicationContext(), "Signed in", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), AdminNoticeActivity.class);
                    startActivity(intent);
                    finish();

                }else {
                    // handling error if we get any error.
                    Toast.makeText(LoginActivity.this, "Fail to signing in..", Toast.LENGTH_SHORT).show();
                }
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