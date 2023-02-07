package com.example.sosapp.db.Table;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.sosapp.MainActivity;
import com.example.sosapp.filemanager.FileManager;
import com.example.sosapp.firebase.FirebaseManager;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;


public final class Instances extends Activity {
    @NotNull
    public String TABLE_NAME = "ContactNumbersTable";
    @NotNull
    public static final String NAME = "Name";
    @NotNull
    public static final String PHONE_NUMBER = "Number";
    @NotNull
    public String DATABASE;

    public Instances(Activity context){
        try{
           String email = new FirebaseManager(context).getEmail();
           String name = email.substring(0 , email.indexOf("@"));
           DATABASE = name + "Database";
           TABLE_NAME = name + TABLE_NAME;
            Log.d("DatabaseS" , "Database name changed");
        } catch (Exception e) {
            DATABASE = "UnknownUser";
            TABLE_NAME = "Unknown" + TABLE_NAME;
            Log.d("DatabaseS" , e.getMessage());
        }
    }
}

