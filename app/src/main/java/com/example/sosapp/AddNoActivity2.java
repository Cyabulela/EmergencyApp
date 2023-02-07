package com.example.sosapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sosapp.db.DatabaseManagement;
import com.example.sosapp.helper.Helper;
import com.example.sosapp.state.LockFunctions;
import com.example.sosapp.state.Status;
import com.example.sosapp.verify.Valid;


public class AddNoActivity2 extends AppCompatActivity {

    private EditText nameText, numberText;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_no2);
        Button button = findViewById(R.id.saveB);
        nameText = findViewById(R.id.nameT);
        numberText = findViewById(R.id.phoneT);
        button.setOnClickListener(view -> register());
    }

    public void register() {
        if (LockFunctions.getState() == Status.Locked) {
            Toast.makeText(getApplicationContext(), "Please kindly login or signup first to activate the app", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = nameText.getText().toString();
        String number = numberText.getText().toString();
        if (!name.isEmpty() && !number.isEmpty()){
            if(Valid.validNumber(number)){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddNoActivity2.this);
                alertDialog.setCancelable(false);
                Helper helper = new Helper(nameText.getText().toString() , numberText.getText().toString());
                DatabaseManagement db = new DatabaseManagement(this);
                boolean isAdded = db.insertData(helper);
                if(!isAdded){
                    alertDialog.setTitle("Contact not added");
                    alertDialog.setMessage("The contact was unable to be added on the database");
                }
                else{
                    alertDialog.setTitle("Number added");
                    alertDialog.setMessage("New helper has been added");
                }
                alertDialog.setPositiveButton("OK", (dialogInterface, i) -> {
                    numberText.setText(null);
                    nameText.setText(null);
                });
                alertDialog.show();
            }
            else numberText.setError("Invalid number");
        }
        else if (name.isEmpty()) nameText.setError("Contact name is empty!!");
        else Toast.makeText(this, "Something is wrong, please re-check your input", Toast.LENGTH_SHORT).show();
    }

}

