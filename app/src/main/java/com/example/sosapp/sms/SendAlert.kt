package com.example.sosapp.sms

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sosapp.db.DatabaseManagement
import com.example.sosapp.helper.Helper
import com.example.sosapp.locator.Locator
import com.example.sosapp.message.Message
import com.example.sosapp.state.LockFunctions
import com.example.sosapp.state.Status

class SendAlert(private val applicationContext : Activity) {
    fun sendAlert(){
        if (LockFunctions.getState() == Status.Locked) {
            Toast.makeText(applicationContext, "Please kindly login or signup first to activate the app", Toast.LENGTH_SHORT).show()
            return
        }
        val helperList: List<Helper>
        val db: DatabaseManagement
        try {
            if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
            ) {
                db = DatabaseManagement(applicationContext)
                helperList = db.readData()
                val locator = Locator(applicationContext)
                if (helperList.isNotEmpty()) {
                    for (helper in helperList) {
                        val sms = SMS(Message(applicationContext), helper.number)
                        if (locator.locationNotNull()) sms.setMessage(locator)
                        else sms.setMessage()
                        Send.sendMessage(sms)
                    }
                    with(AlertDialog.Builder(applicationContext)) {
                        setTitle("Succeeded")
                        setMessage("Alert message has been sent")
                        setCancelable(true)
                        setPositiveButton("Ok", null)
                        show()
                    }

                } else Toast.makeText(applicationContext, "You haven't added any emergency contact", Toast.LENGTH_SHORT).show()
            } else ActivityCompat.requestPermissions(
                applicationContext, arrayOf(Manifest.permission.SEND_SMS), 100
            )
        } catch (e: Exception) {
            with(AlertDialog.Builder(applicationContext)) {
                setTitle("Error!!!")
                setMessage(e.message)
                setPositiveButton("Ok", null)
                show()
            }
        }
    }
}