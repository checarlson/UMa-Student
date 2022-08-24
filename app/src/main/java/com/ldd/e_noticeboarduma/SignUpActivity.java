package com.ldd.e_noticeboarduma;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

/**
 * This class is use to create an account
 */

public class SignUpActivity extends AppCompatActivity {

    public static TextView passwarn, warn, passmatch, termsAndPrivacy, dlogo;
    public static EditText pass, pass1, email, etname, lname;
    public static Button vBtn;
    public static CheckBox show, num, letter, xter;
    private TextView signIn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        this.setTitle(R.string.signup);

        passwarn = findViewById(R.id.Warn);
        pass = findViewById(R.id.createPass);
        pass1 = findViewById(R.id.confirmPass);
        warn = findViewById(R.id.passWarn);
        email = findViewById(R.id.emailAddress);
        etname = findViewById(R.id.first);
        vBtn = findViewById(R.id.register);
        passmatch = findViewById(R.id.passMatchWarn);
        show = findViewById(R.id.showPass);

        num = findViewById(R.id.number);
        letter = findViewById(R.id.letersymb);
        xter = findViewById(R.id.character);
        signIn = findViewById(R.id.clicksin);


        handler();
        verifyEmail();
        showPassword();
        signup();
    }


    /**
     * This method check the password during input if it satisfy the password criteria provided.
     */
    public void handler(){
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 8){
                    xter.post(new Runnable() {
                        @Override
                        public void run() {
                            xter.setChecked(true);
                        }
                    });
                } else if (start < 8){
                    xter.post(new Runnable() {
                        @Override
                        public void run() {
                            xter.setChecked(false);
                        }
                    });
                }

                if (!Utility.containNumber(s)){
                    num.post(new Runnable() {
                        @Override
                        public void run() {
                            num.setChecked(false);
                        }
                    });
                } else {
                    num.post(new Runnable() {
                        @Override
                        public void run() {
                            num.setChecked(true);
                        }
                    });
                }

                if (Utility.containSymbol(s) && !Utility.isAlpha(s)){
                    letter.post(new Runnable() {
                        @Override
                        public void run() {
                            letter.setChecked(false);
                        }
                    });
                } else {
                    letter.post(new Runnable() {
                        @Override
                        public void run() {
                            letter.setChecked(true);
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    public void verifyEmail(){
        vBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    register();
                    registerStudent();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * this method compare the 2 passwords provided by the user if they match
     * @param pass1; the first password
     * @param pass2; the second password to confirm the first
     * @return returns a boolean value if the passwords match or not.
     */
    private boolean passwordMatch(String pass1, String pass2){
        return pass1.equals(pass2);
    }

    /**
     * this method is used to check if the password provided by the user fulfil the password rules
     * @param x; a checkbox to check if the password is at least 8 characters
     * @param y; a checkbox to check if the password contains an alphabet letter
     * @param z; a checkbox to check if the password contains a symbol or a number
     * @return returns a boolean value if rules are fulfilled or not.
     */
    private boolean checkPassword(CheckBox x, CheckBox y, CheckBox z){
        return x.isChecked() && y.isChecked() && z.isChecked();
    }

    /**
     * this method is use to display the user's password
     */
    public void showPassword(){
        show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pass1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    pass1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    public void registerStudent() throws Exception {
        String name = etname.getText().toString();
        String matricule = email.getText().toString();
        String password = pass.getText().toString();

        String Pass1 = pass.getText().toString();
        String Pass2 = pass1.getText().toString();

        String pass1 = Utility.encrypt(password, Utility.en_pass);

         /*if (Utility.isEmailValid(Email)) {
            Toast.makeText(getApplicationContext(), "Valid Email Address.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "InValid Email Address.", Toast.LENGTH_SHORT).show();
            warn.setText("wrong or invalid email");
        }*/

       /* if (passwordMatch(Pass1, Pass2) && Pass1.length() >= 8 && checkPassword(xter, num, letter)) {
            Toast.makeText(getApplicationContext(), "Passwords okay", Toast.LENGTH_SHORT).show();
        } else if(!passwordMatch(Pass1, Pass2)) {
            passmatch.setText(R.string.pass_match);
        } else if (passwordMatch(Pass1, Pass2) && Pass1.length() < 8 && !checkPassword(xter, num, letter)) {
            passmatch.setText(R.string.pass_req);
        }*/

        if (!name.isEmpty() && !matricule.isEmpty() && !password.isEmpty() && passwordMatch(Pass1, Pass2) && checkPassword(xter, num, letter)) {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Students");
            query.whereEqualTo("Matricule", matricule);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        String id = object.getObjectId();
                        query.getInBackground(id, new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                if (e == null) {
                                    // adding our data with their key value in our object.
                                    object.put("Name", name);
                                    object.put("Password", pass1);


                                    // adding a progress dialog box
                                    final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
                                    progressDialog.setCancelable(false);
                                    progressDialog.setCanceledOnTouchOutside(false);
                                    progressDialog.setTitle(R.string.please_wait);
                                    progressDialog.setMessage("Registering user...");
                                    progressDialog.show();

                                    object.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (progressDialog.isShowing())
                                                progressDialog.dismiss();
                                            // checking if the error is null or not.
                                            if (e == null) {
                                                Toast.makeText(getApplicationContext(), "Register Successfully 🤩🤩", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(SignUpActivity.this, SignInEmailActivity.class));
                                                finish();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Failed to register user", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(getApplicationContext(), "Failed to register object", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.your_mat_no, Toast.LENGTH_SHORT).show();
                        passwarn.setText(R.string.your_mat_no);
                    }
                }
            });

        }else {
            etname.setError("required");
            email.setError("required");
            pass.setError("required");
        }
    }


    public void signup(){
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, SignInEmailActivity.class));
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