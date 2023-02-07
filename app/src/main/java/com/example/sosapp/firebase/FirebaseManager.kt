package com.example.sosapp.firebase

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.sosapp.MainActivity
import com.example.sosapp.db.DatabaseManagement
import com.example.sosapp.filemanager.FileManager
import com.example.sosapp.helper.Helper
import com.example.sosapp.helper.User
import com.example.sosapp.state.LockFunctions
import com.example.sosapp.state.Result
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.HashMap

class FirebaseManager(private val context: Activity) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val databaseReference: DatabaseReference

    init {
        val db = FirebaseDatabase.getInstance()
        databaseReference = db.getReference("Users")
    }

    fun signup(user: User) {
        Result.Failed
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnSuccessListener {
                Objects.requireNonNull(FirebaseAuth.getInstance().currentUser)?.let {
                    databaseReference.child(it.uid)
                        .setValue(user).addOnCompleteListener { task: Task<Void?> ->
                            if (task.isSuccessful) {
                                updateUserName(user.username)
                                context.startActivity(Intent(context , MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK })
                                context.finishAffinity()
                            } else Toast.makeText(context , "Failed to register account, try again" , Toast.LENGTH_LONG).show()
                        }
                }
            }
    }

    fun login(userEmail: String?, password: String?) {
        auth.signInWithEmailAndPassword(userEmail!!, password!!)
            .addOnSuccessListener {
                LockFunctions.unlock()
                loadData()
                auth.currentUser?.let {
                        it1 -> databaseReference.child(it1.uid).addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val name = snapshot.child("username").getValue(String::class.java).toString()
                        updateUserName(name)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context , "Cancelled" , Toast.LENGTH_SHORT).show()
                    }

                })
                }
                context.startActivity(Intent(context , MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK })
                context.finishAffinity()
            }.addOnFailureListener { e: Exception ->
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun sync() {
        Log.d("ListContact" , "Sync start...")
        val db = DatabaseManagement(context)
        val currentContactList = db.readData()
        val backupContactList = ArrayList<Helper>()
        auth.currentUser?.let { firebaseUser ->
            databaseReference.child(firebaseUser.uid).child("contacts").addValueEventListener( object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("ListContact" , "Server data collecting...")
                    backupContactList.clear()
                    for (dataSnapshot in snapshot.children){
                        val map = dataSnapshot.value as HashMap<String , Any>
                        val name = map["name"]
                        val number = map["number"]
                        if (name != null && number != null) {
                            backupContactList.add(Helper(name as String , number as String))
                        }
                    }
                    Log.d("ListContact" , "Server data collected")
                    val contactsToAdd = currentContactList.filter { helper ->
                        var hasContact = false
                        for(contact in backupContactList){
                            if (contact.name == helper.name && contact.number == helper.number) hasContact = true
                            break
                        }
                        !hasContact
                    }
                    Log.d("ListContact" , currentContactList.size.toString() + " number of contact on your phone")
                    backupContactList.forEach { contact->
                        Log.d("ListContact" , contact.name + " number of contact on your server account")
                    }

                    insectContact(contactsToAdd)
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context , "Cancelled" , Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun insectContact(helpers : List<Helper>) {
        if(helpers.isEmpty()){
            Log.d("ListContact" , "List to add is empty")
            return
        }
        try{
            helpers.forEach { helper ->
                if (helper.name == null || helper.number == null) return
                auth.currentUser?.let {
                    helper?.number?.let { it1 ->
                        databaseReference.child(it.uid).child("contacts").child(it1).setValue(helper).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("ListContact" , "contact added successful")
                            }
                        }
                    }
                }
            }
        } catch (e : Exception) {
            e.message?.let { Log.d("ListContact" , it) }
        }
    }

    fun updateUserName(name : String) {
        FileManager("user.sos" , context).writeFile(name)
        Log.d("DatabaseS" , FileManager("user.sos" , context).readFile())
    }

    private fun loadData() {
        Log.d("ListContact" , "Loading contacts from the database")
        auth.currentUser?.let {
            databaseReference.child(it.uid).child("contacts").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val contacts : ArrayList<Helper> = ArrayList()
                    for (dataSnapshot in snapshot.children){
                        val map = dataSnapshot.value as HashMap<String , Any>
                        val name = map["name"]
                        val number = map["number"]
                        if (name != null && number != null) {
                            contacts.add(Helper(name as String , number as String))
                        }
                    }
                    Log.d("ListContact" , contacts.size.toString() + " number of contacts from server backup")
                    try {
                        val databaseManagement = DatabaseManagement(context)
                        contacts.forEach { contact ->
                            if (contact.name != null && contact.number != null) databaseManagement.insertData(contact)
                        }
                    }catch (e : Exception) {
                        Log.d("ListContact" , "Failed to add on the local database")
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context , "Cancelled" , Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun login(): Result {
        val user = auth.currentUser!!
        return if (user.isAnonymous) Result.Failed else Result.Success
    }

    fun hasLoggedInBefore(): Boolean {
        return auth.currentUser != null
    }

    fun signOut() {
        auth.signOut()
        LockFunctions.lock()
    }

    fun resetPassword(email : String) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener{ task ->
            if (task.isSuccessful) Toast.makeText(context , "Email to reset password has been resend" , Toast.LENGTH_LONG).show()
            else Toast.makeText(context , "Failed to send email, try again!" , Toast.LENGTH_LONG).show()
        }
    }

    fun getEmail() : String?{
        return auth.currentUser?.email
    }
}