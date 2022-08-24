package com.ldd.e_noticeboarduma;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddNotice extends AppCompatActivity {
    private static final int PICK_FROM_GALLERY = 1;
    public Button pick, post;
    public EditText name;
    public ImageView logo;
    public byte[] image;
    public Bitmap bitmap = null;
    private String des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notice);

        pick = findViewById(R.id.pick_image);
        post = findViewById(R.id.add);
        name = findViewById(R.id.add_note);
        logo = findViewById(R.id.image);

        onPickClick();
        onAddClick();
        edit();
    }


    public void onPickClick(){
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                pickPhoto();
//                checkPermissionAndOpenGallery();
                pickPhoto();
                System.out.println("Button clicked");
            }
        });
    }

    public void onAddClick(){
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    update();
                }else {
                    save();
                }
            }
        });
    }

    /**
     * used to pick a photo from the user gallery
     */
    public void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_GALLERY) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri selectedImageUri = data.getData();
                    InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    Bitmap reduceSize = Utility.reduceBitmapSize(bitmap, 307200);
                    logo.setImageBitmap(reduceSize);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private byte[] imageToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public void save(){
        //adding on back4app
        // Configure Query
        ParseObject notice = new ParseObject("Notice");

        String description = name.getText().toString();
        image = imageToByte(logo);

        // adding our data with their key value in our object.
        notice.put("PostImage", image);
        notice.put("PostDescription", description);

        // adding a progress dialog box
        final ProgressDialog progressDialog = new ProgressDialog(AddNotice.this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Posting...");
        progressDialog.show();

        // calling a method to save our data in background.
        notice.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                // checking if the error is null or not.
                if (e == null) {
                    // if the error is null we are displaying a simple toast message.
                    Toast.makeText(getApplicationContext(), "Successfully Posted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddNotice.this, AdminNoticeActivity.class));
                    finish();
                } else {
                    // if the error is not null we will displaying an error message to our user.
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void edit(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            /*Uri*/
           Uri path = (Uri) extras.get("image"); //image from gallery
//            bitmap = (Bitmap) extras.get("image");  //image from camera
             des = extras.getString("des");
            bitmap = Utility.bitImage;

            logo.setImageBitmap(bitmap);
            name.setText(des);
            post.setText("Update Notice");
        }
    }

    public void update(){
        String description = name.getText().toString();
        image = imageToByte(logo);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Notice");
        query.whereEqualTo("PostDescription", des);
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
                                object.put("PostImage", image);
                                object.put("PostDescription", description);


                                // adding a progress dialog box
                                final ProgressDialog progressDialog = new ProgressDialog(AddNotice.this);
                                progressDialog.setCancelable(false);
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.setTitle("Please wait");
                                progressDialog.setMessage("Updating...");
                                progressDialog.show();

                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (progressDialog.isShowing())
                                            progressDialog.dismiss();
                                        // checking if the error is null or not.
                                        if (e == null) {
                                            Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(AddNotice.this, AdminNoticeActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Failed to Updated Data", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to update object", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to get object id", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddNotice.this, AdminNoticeActivity.class));
        finish();
    }
}