package com.guinproductions.behaviorrolodex.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.guinproductions.behaviorrolodex.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.guinproductions.behaviorrolodex.model.Note;
import com.guinproductions.behaviorrolodex.model.Profile;

import java.util.HashMap;
import java.util.Map;

public class NoteNew extends AppCompatActivity {

    private static final String REQUIRED = "Required";
    private static final String TAG = "NewNoteActivity";

   /* public static String getProfileId;
    public static String getFname;
    public static String getLname;
    public static String getNoted;
    public static String getImage;*/

    public static String setProfileId;
    public static String setFname;
    public static String setLname;

    private TextView fNameView;
    private TextView lNameView;

    DatabaseReference mDatabase;
    DatabaseReference mProfileDB;
    DatabaseReference profileKey;

    ProfileView mProfileView;

    FloatingActionButton mNoteSubmit;

    EditText noteTitle;
    EditText noteContent;
    TextView noteDate;
    TextView noteTime;

    String noteId;

    Profile profile;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance(). getReference();
        mProfileDB = mDatabase.child("profiles");
        profileKey = mProfileDB.child(ProfileView.getProfileId);

        Intent i = getIntent();

       setProfileId = i.getStringExtra(ProfileView.getProfileId);
       setFname = i.getStringExtra(ProfileView.getFname);
       setLname = i.getStringExtra(ProfileView.getLname);

        fNameView = (TextView) findViewById(R.id.view_fname);
        lNameView = (TextView) findViewById(R.id.view_lname);

        fNameView.setText(setFname);
        lNameView.setText(setLname);

        noteTitle = (EditText) findViewById(R.id.title);
        noteContent = (EditText) findViewById(R.id.content);

        noteDate = (TextView) findViewById(R.id.date_view);
        noteTime = (TextView) findViewById(R.id.time_view);

        mNoteSubmit = (FloatingActionButton) findViewById(R.id.fab);
        mNoteSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

          noteSave();

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void noteSave() {

        final String title = noteTitle.getText().toString();
        final String content = noteContent.getText().toString();
        final String date = noteDate.getText().toString();
        final String time = noteTime.getText().toString();

        if(TextUtils.isEmpty(title)){
            noteTitle.setError(REQUIRED);
        return;
        }

        if(TextUtils.isEmpty(content)){
            noteContent.setError(REQUIRED);
        return;
        }

        setEditingEnabled(false);
        Toast.makeText(this, "Saving...", Toast.LENGTH_SHORT).show();


        // [START single_value_read]
         final String profileId = setProfileId;
       profileKey.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        Profile profile = dataSnapshot.getValue(Profile.class);

                        // [START_EXCLUDE]
                        if (profile == null) {
                            // User is null, error out
                            Log.e(TAG, "Profile" + profileId + " is unexpectedly null");
                            Toast.makeText(NoteNew.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            noteAddNew(title, content, date, time);
                        }

                        // Finish this Activity, back to the stream
                        setEditingEnabled(true);
                        finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                });
        // [END single_value_read]
    }

    private void setEditingEnabled(boolean enabled) {
        noteTitle.setEnabled(enabled);
        noteContent.setEnabled(enabled);

        if (enabled) {
            mNoteSubmit.setVisibility(View.VISIBLE);
        } else {
            mNoteSubmit.setVisibility(View.GONE);
        }
    }



   private void noteAddNew(String title, String content, String date, String time){

       note = new Note(noteId, title, content, date, time);

       date = note.setDate(note.getDate().toString());
       time = note.setTime(note.getTime().toString());


       String noteId =  note.setNoteid(profileKey.child("notes").push().getKey());
       note = new Note(noteId, title, content, date, time);


       Map<String, Object> noteValues = note.noteMap();

       Map<String, Object> childUpdates = new  HashMap<>();
       childUpdates.put("/notes/" + "/" + noteId, noteValues);
       childUpdates.put("/notes_profileid/" + setProfileId + "/" + noteId + "/", noteValues);


       mDatabase.child("notes").child(noteId).setValue(noteValues);

       mDatabase.updateChildren(childUpdates);
       finish();

       Toast.makeText(this, "Note Saved", Toast.LENGTH_LONG).show();






    }

}
