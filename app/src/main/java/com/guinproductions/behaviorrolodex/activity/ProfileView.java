package com.guinproductions.behaviorrolodex.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.guinproductions.behaviorrolodex.R;
import com.guinproductions.behaviorrolodex.model.Profile;
import com.guinproductions.behaviorrolodex.adapters.ProfileRV;

public class ProfileView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "ProfileView";

    public static final String EXTRA_PROFILE_KEY = "profile_key";

    public static String setProfileId;
    public static String setFname;
    public static String setLname;
    public static String setNoted;
    public static String setImage;

    public static String profileRef;

    public static String getProfileId;
    public static String getFname;
    public static String getLname;
    public static String getNoted;
    public static String getImage;

    DatabaseReference mDatabase;
    Context mContext;

    ProfileRV profileRV;
    Profile profile;

    TextView fNameView;
    TextView lNameView;
    ImageView imageView;

    Button newNoteButton;

    FloatingActionMenu materialDesignFAM;
    FloatingActionButton fabBehavior, fabTreatment, fabAntecedent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("profiles");


        Intent i = getIntent();

        setProfileId = i.getStringExtra(ProfileRV.profileID);
        setFname = i.getStringExtra(ProfileRV.fNameRef);
        setLname = i.getStringExtra(ProfileRV.lNameRef);
        setNoted = i.getStringExtra(ProfileRV.notedRef);
        setImage = i.getStringExtra(ProfileRV.imageRef);


        newNoteButton = (Button) findViewById(R.id.nav_new_note);

        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        fabBehavior = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_behavior);
        fabTreatment = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_treatment);
        fabAntecedent = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_antecedent);


        fabBehavior.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int id = v.getId();

                if (id == R.id.fab_behavior) {

                    Intent i = getIntent();

                    getProfileId = i.getStringExtra(ProfileRV.profileID);

                    Intent intent = new Intent(getApplicationContext(), BehaviorActivity.class);

                    intent.putExtra(getProfileId, ProfileView.setProfileId);

                    getApplicationContext().startActivity(intent);
                }

            }
        });

        fabTreatment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = getIntent();


                getProfileId = i.getStringExtra(ProfileRV.profileID);

                Intent intent = new Intent(getApplicationContext(), TreatmentActivity.class);

                intent.putExtra(getProfileId, ProfileView.setProfileId);

                getApplicationContext().startActivity(intent);

            }
        });

        fabAntecedent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = getIntent();


                getProfileId = i.getStringExtra(ProfileRV.profileID);

                Intent intent = new Intent(getApplicationContext(), AntecedentActivity.class);

                intent.putExtra(getProfileId, ProfileView.setProfileId);

                getApplicationContext().startActivity(intent);

            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

        fNameView = (TextView) hView.findViewById(R.id.nav_fname);
        lNameView = (TextView) hView.findViewById(R.id.nav_lname);

        fNameView.setText(setFname);
        lNameView.setText(setLname);

        newNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = getIntent();

                getProfileId = i.getStringExtra(ProfileRV.profileID);
                getFname = i.getStringExtra(ProfileRV.fNameRef);
                getLname = i.getStringExtra(ProfileRV.lNameRef);


                Intent intent = new Intent(getApplicationContext(), NoteNew.class);

                intent.putExtra(getProfileId, ProfileView.setProfileId);
                intent.putExtra(getFname, ProfileView.setFname);
                intent.putExtra(getLname, ProfileView.setLname);

                getApplicationContext().startActivity(intent);


            }
        });


        navigationView.setNavigationItemSelectedListener(this);



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_view, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_new_note) {

            Intent i = getIntent();

            getProfileId = i.getStringExtra(ProfileRV.profileID);
            getFname = i.getStringExtra(ProfileRV.fNameRef);
            getLname = i.getStringExtra(ProfileRV.lNameRef);


            Intent intent = new Intent(getApplicationContext(), NoteNew.class);

            intent.putExtra(getProfileId,ProfileView.setProfileId);
            intent.putExtra(getFname,ProfileView.setFname);
            intent.putExtra(getLname,ProfileView.setLname);

          getApplicationContext().startActivity(intent);


        } else if (id == R.id.nav_view_notes) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
