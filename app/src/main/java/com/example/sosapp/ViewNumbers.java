package com.example.sosapp;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sosapp.adapters.CustomListAdapter;
import com.example.sosapp.db.DatabaseManagement;
import com.example.sosapp.helper.Helper;

public class ViewNumbers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_numbers);
        ListView listView = findViewById(R.id.helperList);
        DatabaseManagement databaseManagement = new DatabaseManagement(this);
        Helper[] helpers = databaseManagement.readData().toArray(new Helper[0]);
        CustomListAdapter customListAdapter = new CustomListAdapter(this , helpers);
        listView.setAdapter(customListAdapter);
    }
}