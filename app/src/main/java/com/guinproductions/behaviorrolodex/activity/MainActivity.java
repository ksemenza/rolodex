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
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.guinproductions.behaviorrolodex.model.Profile;
import com.guinproductions.behaviorrolodex.adapters.ProfileRV;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements DeletionListener{

    private static final String TAG = "MainActivity";

    public static String profileId;

    ProfileRV pList;
    TextView fNameView;
    TextView lNameView;
    ImageView imageView;
    Profile profile;

    List<Profile> profileAdapter;

    DatabaseReference mDatabase;
    DatabaseReference mProfileDatabase;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mProfileDatabase = mDatabase.child("profiles");

        pList = new ProfileRV(Collections.<Profile>emptyList());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, ProfileNew.class));


                Toast.makeText(MainActivity.this, "Complete all boxes", Toast.LENGTH_LONG).show();


            }


        });

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.profile_list) ;
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
                new SwipeController(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, MainActivity.this));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(pList);

    }

    @Override
    protected void onResume(){
        super.onResume();

        mDatabase.child("profiles").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                profileAdapter= new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    profile = postSnapshot.getValue(Profile.class);

                    profileAdapter.add(profile);

                }

                pList.updateList(profileAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void itemRemoved(int position) {

        pList.removeItem(position);

    }
}
