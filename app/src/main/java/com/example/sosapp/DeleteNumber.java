package com.example.sosapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.sosapp.adapters.CustomListAdapter;
import com.example.sosapp.helper.Helper;

public class DeleteNumber extends AppCompatActivity {
    /*private ListView listView;
    private Helper helpers[] = {new Helper("Cya" , "023") , new Helper("fvfd", "dfvf")};*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_number);
        /*listView = findViewById(R.id.helperList);
        CustomListAdapter customListAdapter = new CustomListAdapter(getApplicationContext() , helpers);
        listView.setAdapter(customListAdapter);*/
    }
}