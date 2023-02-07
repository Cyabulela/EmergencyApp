package com.example.sosapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sosapp.db.DatabaseManagement;
import com.example.sosapp.helper.Helper;
import com.example.sosapp.verify.Valid;

public class EditNumber extends AppCompatActivity {
    private EditText nameET , numberET;
    private String name , number;
    private AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_number);
        nameET = findViewById(R.id.nameET);
        numberET = findViewById(R.id.phoneET);
        Button save = findViewById(R.id.saveBE);
        Button cancel = findViewById(R.id.cancel);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            name = bundle.getString("name");
            number = bundle.getString("number");
        }

        nameET.setText(name);
        numberET.setText(number);

        save.setOnClickListener(v -> {
            DatabaseManagement db = new DatabaseManagement(this);
            Helper helper = new Helper(nameET.getText().toString() , numberET.getText().toString());
            Helper oldHelper = new Helper(name , number);
            if (!helper.getName().isEmpty() && !helper.getNumber().isEmpty() && Valid.validNumber(helper.getNumber())){
                boolean isAdded = db.update(helper , oldHelper);
                if (isAdded) {
                    alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle("Updated!");
                    alertDialog.setMessage("Contact info has been updated");
                    alertDialog.setCancelable(false);
                    alertDialog.setPositiveButton("OK" , (design , position) -> {
                        startActivity(new Intent(this , ViewNumbers.class));
                        finish();
                    });
                    alertDialog.show();
                }
                else Toast.makeText(getApplicationContext() , "Something went wrong" , Toast.LENGTH_SHORT).show();
            }
            else if (helper.getName().isEmpty()) nameET.setError("Contact name is empty!!");
            else if (helper.getNumber().isEmpty() || !Valid.validNumber(helper.getNumber())) numberET.setError("Invalid number");
            else Toast.makeText(this, "Something is wrong, please re-check your input", Toast.LENGTH_SHORT).show();
        });

        cancel.setOnClickListener(v -> {
            startActivity(new Intent(this , ViewNumbers.class));
            finish();
        });
    }
}