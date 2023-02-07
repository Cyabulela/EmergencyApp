package com.example.sosapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.sosapp.firebase.FirebaseManager
import com.example.sosapp.helper.User
import com.example.sosapp.verify.Valid
import com.example.sosapp.state.Result

class SignUp : AppCompatActivity() {
    private lateinit var username_editText : EditText
    private lateinit var email_account_editText : EditText
    private lateinit var password_editText : EditText
    private lateinit var confirm_password_editText : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        username_editText = findViewById(R.id.sign_up_username)
        email_account_editText = findViewById(R.id.sign_up_email)
        password_editText = findViewById(R.id.sign_up_password)
        confirm_password_editText = findViewById(R.id.sign_up_password_confirm)
        val signUp = findViewById<Button>(R.id.sign_up_button)
        signUp.setOnClickListener{signUp()}
    }

    private fun signUp() {
        if (!isBlank()) {
            if (Valid.validEmail(email_account_editText.text.toString())){
                if (password_editText.text.toString().length >= 8){
                    if (password_editText.text.toString().equals(confirm_password_editText.text.toString() , false)) {
                        val user = User(username = username_editText.text.toString() , email = email_account_editText.text.toString() , password = password_editText.text.toString())
                        FirebaseManager(this).signup(user)
                        finish()
                    }else confirm_password_editText.error = "Does not match password"
                }else password_editText.error = "Password should have 8 or more characters"
            }else email_account_editText.error = "Please enter valid email account"
        }

    }

    private fun isBlank() : Boolean {
        var isBlank = false
        if (username_editText.text.toString().isBlank()) {
            username_editText.error = "Please enter your username"
            isBlank = true
        }
        if (email_account_editText.text.toString().isBlank()) {
            email_account_editText.error = "Please enter your email account"
            isBlank = true
        }
        if (password_editText.text.toString().isBlank()) {
            password_editText.error = "Please create your password"
            isBlank = true
        }
        if (confirm_password_editText.text.toString().isBlank()) {
            confirm_password_editText.error = "Please create your password"
            isBlank = true
        }
        return isBlank
    }
}