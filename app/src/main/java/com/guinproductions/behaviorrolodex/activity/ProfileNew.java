package com.guinproductions.behaviorrolodex.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guinproductions.behaviorrolodex.R;
import com.guinproductions.behaviorrolodex.model.Profile;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProfileNew extends AppCompatActivity {

    private static final String TAG = "NewProfileActivity";
    private static final String REQUIRED = "Required";

    private DatabaseReference mDatabase;
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

        mSaveProfile = (FloatingActionButton) findViewById(R.id.profile_save);
        mFName = (EditText) findViewById(R.id.edit_fname);
        mLName = (EditText) findViewById(R.id.edit_lname);
        mNote = (EditText) findViewById(R.id.edit_note);
        mImageSelect = (ImageView) findViewById(R.id.view_image);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.profile_save);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfile();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void saveProfile() {
        final String fname = mFName.getText().toString();
        final String lname = mLName.getText().toString();
        final String noted = mNote.getText().toString();
        Picasso.with(getApplicationContext()).load("gs://behaviorrolodex.appspot.com/").into(mImageSelect);

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

        String profileId = profile.setUid(mDatabase.child("profiles").push().getKey());
        profile = new Profile(profileId, fname, lname, noted);
        Map<String, Object> profileValue = profile.profileMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/profiles/" + profileId, profileValue);


        mDatabase.updateChildren(childUpdates);

    }

}