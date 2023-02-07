package com.example.sosapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sosapp.message.Message;

public class setMassage extends AppCompatActivity {
    private EditText editText;
    Message message = new Message(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_massage);
        editText = findViewById(R.id.message);
        editText.setText(message.getMessage());
        Button button = findViewById(R.id.save);
        button.setOnClickListener(v -> saveMessage());
    }

    private void saveMessage() {
        String text = editText.getText().toString();
        boolean isSaved = message.setMessage(text);
        if (isSaved) {
            Toast.makeText(this, "Message saved", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Message not saved!!!", Toast.LENGTH_SHORT).show();
        }
    }
}