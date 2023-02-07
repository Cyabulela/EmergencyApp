package com.example.sosapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.sosapp.state.LockFunctions;
import com.example.sosapp.state.Status;

public class MenuActivity2 extends AppCompatActivity implements View.OnClickListener {
    private CardView instructions ,addNumber,viewNumber,setMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu2);
        // defining cards

        instructions = findViewById(R.id.instruction);
        addNumber = findViewById(R.id.addNumber);
        viewNumber = findViewById(R.id.viewAddNumber);
        setMessage = findViewById(R.id.setMessage);

        // add click listerners to the cards

        instructions.setOnClickListener(this);
        addNumber.setOnClickListener(this);
        if (LockFunctions.getState() != Status.Locked) viewNumber.setOnClickListener(this);
        setMessage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i ;
        switch (v.getId()){
        case R.id.instruction : i = new Intent(this,instruction.class);startActivity(i);break;
        case R.id.addNumber :   i = new Intent(this,AddNoActivity2.class);startActivity(i);break;
        case R.id. viewAddNumber: i = new Intent(this,ViewNumbers.class);startActivity(i);break;
        case R.id.setMessage : i = new Intent(this,setMassage.class);startActivity(i);break;
        default:break;
        }
    }

}