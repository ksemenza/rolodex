package com.guinproductions.behaviorrolodex;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by guinp on 9/1/2017.
 */

public class FirebaseDataPersistence extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
