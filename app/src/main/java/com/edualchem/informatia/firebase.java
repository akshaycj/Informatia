package com.edualchem.informatia;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Cj on 10-05-2017.
 */

public class firebase extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
