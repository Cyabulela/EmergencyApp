package com.example.sosapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.sosapp.firebase.FirebaseManager
import com.example.sosapp.verify.Valid

class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        val email_text_edit = findViewById<EditText>(R.id.forgot_password_email)
        val reset_password = findViewById<Button>(R.id.reset_password)
        reset_password.setOnClickListener{
            if (email_text_edit.text.isNotBlank()) {
                if (Valid.validEmail(email_text_edit.text.toString())) {
                    FirebaseManager(this).resetPassword(email_text_edit.text.toString())
                }else email_text_edit.error = "Invalid email"
            } else email_text_edit.error = "Please enter your password"
        }
    }
}