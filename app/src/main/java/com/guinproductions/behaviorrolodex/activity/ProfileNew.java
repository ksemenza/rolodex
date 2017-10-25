package com.guinproductions.behaviorrolodex.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.guinproductions.behaviorrolodex.R;
import com.guinproductions.behaviorrolodex.model.Profile;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileNew extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "NewProfileActivity";
    private static final String REQUIRED = "Required";

    int PICK_IMAGE_REQUEST = 234;
    public static final String STORAGE_PATH_UPLOADS = "uploads/";

    private DatabaseReference mDatabase;
    private Uri filePath;
    private StorageReference mStorageRef;


    Profile profile;

    private ImageView mImageSelect;
    private EditText mFName;
    private EditText mLName;
    private EditText mNote;
    private FloatingActionButton mSaveProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://behavior-rolodex.appspot.com/");


        mSaveProfile = (FloatingActionButton) findViewById(R.id.profile_save);
        mFName = (EditText) findViewById(R.id.edit_fname);
        mLName = (EditText) findViewById(R.id.edit_lname);
        mNote = (EditText) findViewById(R.id.edit_note);
        mImageSelect = (ImageView) findViewById(R.id.view_image);

        mImageSelect.setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.profile_save);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                saveProfile();
                uploadFile();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

   //Image Methods

    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
           filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                mImageSelect.setImageBitmap(bitmap);
//                mImageSelect.setImageURI(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {


        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            //getting the storage reference
            StorageReference sRef = mStorageRef.child(STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));

            //adding the file to reference
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
                            progressDialog.dismiss();

                            //displaying success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();

                            //creating the upload object to store uploaded image details
//                            Profile profile = new Profile(editTextName.getText().toString().trim(), taskSnapshot.getDownloadUrl().toString());

                            //adding an upload to firebase database
                            String uploadId = mDatabase.push().getKey();
                            mDatabase.child(uploadId).setValue(profile);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            //display an error if no file is selected
        }
    }

    private void saveProfile() {
        final String fname = mFName.getText().toString();
        final String lname = mLName.getText().toString();
        final String noted = mNote.getText().toString();

        Picasso.with(getApplicationContext()).load("gs://behavior-rolodex.appspot.com/").into(mImageSelect);

        // Title is required
        if (TextUtils.isEmpty(fname)) {
            mFName.setError(REQUIRED);
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(lname)) {
            mLName.setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(noted)) {
            mNote.setError(REQUIRED);
            return;
        }

        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        Toast.makeText(this, "Saving...", Toast.LENGTH_SHORT).show();

        mDatabase.child("profiles").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        addNewProfile(fname, lname, noted);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

    }

    private void setEditingEnabled(boolean enabled) {
        mFName.setEnabled(enabled);
        mLName.setEnabled(enabled);
        mNote.setEnabled(enabled);
        if (enabled) {
            mSaveProfile.setVisibility(View.VISIBLE);
        } else {
            mSaveProfile.setVisibility(View.GONE);
        }
    }


    private void addNewProfile(String fname, String lname, String noted){

        profile = new Profile();

        String profileId = profile.setProfileid(mDatabase.child("profiles").push().getKey());
        profile = new Profile(profileId, fname, lname, noted);
        Map<String, Object> profileValue = profile.profileMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/profiles/" + profileId, profileValue);


        mDatabase.updateChildren(childUpdates);
        finish();

        Toast.makeText(this, "Profile added", Toast.LENGTH_LONG).show();

    }

    //ImageView method
    @Override
    public void onClick(View v) {

        showFileChooser();

    }
}