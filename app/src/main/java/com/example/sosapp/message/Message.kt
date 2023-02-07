package com.example.sosapp.message

import android.content.Context
import android.widget.Toast
import com.example.sosapp.filemanager.FileManager
import java.io.FileNotFoundException
import java.io.IOException

class Message(var context: Context) {
    private val file: FileManager = FileManager("message.sos", context)

    val message: String?
        get() {
            try {
                return file.readFile()
            } catch (e: FileNotFoundException) {
                return ""
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }

    fun setMessage(message: String?): Boolean {
        return try {
            file.writeFile(message!!)
            true
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            false
        }
    }
}