package com.example.sosapp

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.sosapp.filemanager.FileManager
import com.example.sosapp.firebase.FirebaseManager
import com.example.sosapp.sms.SendAlert
import com.example.sosapp.state.LockFunctions
import com.example.sosapp.state.Status
import com.example.sosapp.state.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var infoTextView: TextView
    private lateinit var userNameTextView : TextView
    private lateinit var signout_button : ImageView
    private val firebaseManager = FirebaseManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.WRITE_CONTACTS), PackageManager.PERMISSION_GRANTED
        )
        infoTextView = findViewById(R.id.textView)
        userNameTextView = findViewById(R.id.textView2)
        button = findViewById(R.id.button_menu)
        button.setOnClickListener { openMenu() }
        val helpButton = findViewById<Button>(R.id.button4)
        helpButton.setOnClickListener { helpAlert() }
        signout_button = findViewById(R.id.sign_out_button)
    }

    override fun onStart() {
        super.onStart()
        autoLogIn()
    }

    private fun autoLogIn() {
        val dbg = "debugging"
        if (checkInternetConnection()) {
            Log.d(dbg , "Internet success")
            if (firebaseManager.hasLoggedInBefore()) {
                Log.d(dbg , "Logged before success")
                when(firebaseManager.login()){
                    Result.Success-> {
                        Log.d(dbg , "Login success")
                        LockFunctions.unlock()
                        firebaseManager.sync()
                    }
                    else -> Toast.makeText(applicationContext , "Login in online failed" , Toast.LENGTH_LONG).show()
                }
            }
        }
        else if (isSignedIn()) {
            Log.d(dbg , "Is signin success")
            Toast.makeText(applicationContext , "Couldn't synchronize, check your internet" , Toast.LENGTH_LONG).show()
            LockFunctions.unlock()
        }
        else {
            Log.d(dbg , "No success")
            LockFunctions.lock()
        }
        updateUI()
    }

    override fun onResume() {
        super.onResume()
        autoLogIn()
    }

    private fun openMenu() {
        val intent = Intent(this, MenuActivity2::class.java)
        startActivity(intent)
    }

    companion object{
        fun checkInternetConnection(): Boolean{
            return try {
                val command = "ping -c 1 google.com"
                (Runtime.getRuntime().exec(command).waitFor() == 0)
            } catch (e : Exception) {
                false
            }
        }
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            helpAlert()
            return true
        }
        return super.onKeyLongPress(keyCode, event)
    }

    private fun isSignedIn() : Boolean {
        val name = try {
            FileManager("user.sos" , this).readFile()
        } catch (e : Exception){
            ""
        }
        return name.isNotBlank()
    }

    private fun helpAlert() {
        val alert = AlertDialog.Builder(this)
        alert.setTitle("SOS Alert")
        alert.setMessage("Are you sure you want to send an alert")
        alert.setPositiveButton("Yes") { dialogInterface, i ->
            val sendAlert = SendAlert(this)
            sendAlert.sendAlert()
        }
        alert.setNegativeButton("No" , null)
        alert.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun updateUI(){
        if (LockFunctions.getState() == Status.Locked) {
            userNameTextView.text = "SOS App"
            signout_button.visibility = ImageView.INVISIBLE
            infoTextView.text = "Click here to Sign in/up to your account"
            infoTextView.setTextColor(Color.BLUE)
            infoTextView.setOnClickListener{
                startActivity(Intent(applicationContext , SignIn::class.java))
            }
        }
        else {
            signout_button.visibility = ImageView.VISIBLE
            signout_button.setOnClickListener{
                signOut()
            }
            infoTextView.text = "Press red button incase of an emergency"
            infoTextView.setTextColor(Color.RED)
            val name = try {
                FileManager("user.sos" , this).readFile()
            } catch (e : Exception){
                ""
            }
            if (name.isBlank()) {
                FirebaseAuth.getInstance().currentUser?.let { it ->
                    firebaseManager.databaseReference.child(it.uid).addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            userNameTextView.text = snapshot.child("username").getValue(String::class.java).also {  name ->
                                FileManager("user.sos" , applicationContext).writeFile(name.toString())
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }
            }
            else userNameTextView.text = name
        }
    }

    private fun signOut() {
        firebaseManager.signOut()
        FileManager("user.sos" , this).writeFile("")
        updateUI()
    }

}