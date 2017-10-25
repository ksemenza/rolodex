package com.guinproductions.behaviorrolodex.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guinproductions.behaviorrolodex.DeletionListener;
import com.guinproductions.behaviorrolodex.R;
import com.guinproductions.behaviorrolodex.SwipeController;
import com.guinproductions.behaviorrolodex.list.BehaviorList;
import com.guinproductions.behaviorrolodex.model.Behavior;
import com.guinproductions.behaviorrolodex.model.Profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BehaviorActivity extends AppCompatActivity implements DeletionListener{

    private static final String TAG = "BehaviorActivity";
    private static final String REQUIRED = "Required";

    public static String setProfileId;

    DatabaseReference mDatabase;
    DatabaseReference mProfileDB;
    DatabaseReference profileKey;
    DatabaseReference behaviorDB;

    private EditText behaviorEdit;
  TextView behaviorView;

    List<Behavior> behaviorsList;
    BehaviorList list;

    FloatingActionButton mAdd;

    String behaviorId;
    Behavior behavior;

    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mProfileDB = mDatabase.child("profiles");
        profileKey = mProfileDB.child(ProfileView.getProfileId);
        behaviorDB = profileKey.child("behaviors_profileid");

        list = new BehaviorList(Collections.<Behavior>emptyList());

        Intent i = getIntent();

        setProfileId = i.getStringExtra(ProfileView.getProfileId);
        behaviorEdit = (EditText) findViewById(R.id.behavior_edit);

        behaviorView = (TextView) findViewById(R.id.behavior_view);


        mAdd = (FloatingActionButton) findViewById(R.id.fab);
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addBehavior();


            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.behavior_list) ;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

        final int offset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration(){
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                    outRect.bottom = offset;
                }
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new SwipeController(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, BehaviorActivity.this));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(list);

    }

    @Override
    protected void onResume(){
        super.onResume();

        mDatabase.child("behaviors_profileid").child(setProfileId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                behaviorsList= new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    behavior = postSnapshot.getValue(Behavior.class);

                    behaviorsList.add(behavior);

                }

             list.updateList(behaviorsList);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addBehavior(){

        final String description = behaviorEdit.getText().toString();

        if(TextUtils.isEmpty(description)){
            behaviorEdit.setError(REQUIRED);


            return;
        }

        setEditingEnabled(false);
        Toast.makeText(this, "Saving...", Toast.LENGTH_SHORT).show();

        final  String profileId = setProfileId;
        profileKey.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        profile = dataSnapshot.getValue(Profile.class);


                        // [START_EXCLUDE]
                        if (profile == null) {
                            // User is null, error out
                            Log.e(TAG, "Profile" + profileId + " is unexpectedly null");
                            Toast.makeText(BehaviorActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            behaviorAdd(description);
                        }
                        setEditingEnabled(true);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                });

    }



    private void setEditingEnabled(boolean enabled) {
                        behaviorEdit.setEnabled(enabled);


                        if (enabled) {
                            mAdd.setVisibility(View.VISIBLE);
                        } else {
                            mAdd.setVisibility(View.GONE);
                        }
                    }



    private void behaviorAdd(String description){

        behavior = new Behavior(behaviorId, description);

        String behaviorId =  behavior.setBehaviorid(profileKey.child("behaviors").push().getKey());
        behavior = new Behavior(behaviorId, description);


        Map<String, Object> behaviorValues = behavior.behaviorMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/behaviors/" + "/" + behaviorId, behaviorValues);
        childUpdates.put("/behaviors_profileid/" + setProfileId + "/" + behaviorId + "/", behaviorValues);


        mDatabase.child("behaviors").child(behaviorId).setValue(behaviorValues);
        mDatabase.updateChildren(childUpdates);

        behaviorEdit.setText("");



        Toast.makeText(this, "Behavior Added", Toast.LENGTH_LONG).show();

    }


    @Override
    public void itemRemoved(int position){
        behaviorsList.remove(position);
    list.notifyItemRemoved(position);
    }
}
