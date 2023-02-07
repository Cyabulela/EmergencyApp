package com.example.sosapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.sosapp.firebase.FirebaseManager

class SignIn : AppCompatActivity() {
    private lateinit var emailEditText : EditText
    private lateinit var passwordEditText : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        emailEditText = findViewById(R.id.login_email)
        passwordEditText = findViewById(R.id.login_password)
        val signIn = findViewById<Button>(R.id.sign_button)
        signIn.setOnClickListener{signIn()}
        val cancel = findViewById<Button>(R.id.cancel_sign_in)
        cancel.setOnClickListener{
            startActivity(Intent(applicationContext , MainActivity::class.java))
            finishAffinity()
        }
        val text = findViewById<TextView>(R.id.to_signup_activity)
        text.setOnClickListener{
            startActivity(Intent(applicationContext , SignUp::class.java))
        }
        val forgot_password = findViewById<TextView>(R.id.to_forgot_password_activity)
        forgot_password.setOnClickListener{
            startActivity(Intent(applicationContext , ForgotPassword::class.java))
        }
    }

    private fun signIn() {
        if (emailEditText.text.isNotBlank()) {
            if (passwordEditText.text.isNotBlank()) {
                val db = FirebaseManager(this)
                db.login(emailEditText.text.toString() , passwordEditText.text.toString())
            }else passwordEditText.error = "Password cannot be blank"
        }else emailEditText.error = "Email cannot be blank"
    }
}