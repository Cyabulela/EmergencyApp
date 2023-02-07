package com.example.sosapp.filemanager

import android.content.Context
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder

class FileManager(private val filename: String, private val context: Context) {
    @Throws(IOException::class)
    fun writeFile(text: String) {
        val write = context.openFileOutput(filename, Context.MODE_PRIVATE)
        write.write(text.toByteArray())
        write.close()
    }

    @Throws(IOException::class)
    fun readFile(filename: String = this.filename): String {
        val text = StringBuilder()
        val read = context.openFileInput(filename)
        val reader = InputStreamReader(read)
        val inputBuffer = CharArray(255)
        var charRead: Int
        while (reader.read(inputBuffer).also { charRead = it } > 0) {
            val readstring = String(inputBuffer, 0, charRead)
            text.append(readstring)
        }
        reader.close()
        return text.toString()
    }

    fun isEmpty(filename: String): Boolean {
        return this.readFile(filename).isEmpty()
    }
}