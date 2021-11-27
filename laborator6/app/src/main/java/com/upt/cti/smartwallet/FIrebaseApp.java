package com.upt.cti.smartwallet;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class FIrebaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /* Enable disk persistence  */
        FirebaseDatabase.getInstance("https://smart-wallet-27310-default-rtdb.europe-west1.firebasedatabase.app/").setPersistenceEnabled(true);
    }
}