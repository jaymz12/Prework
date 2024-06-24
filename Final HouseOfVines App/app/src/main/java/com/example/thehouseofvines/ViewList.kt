package com.example.thehouseofvines

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ViewList : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var mRecycler: RecyclerView
    lateinit var totalnoofitem: TextView
    lateinit var totalnoofsum: TextView
    private val counttotalnoofitem = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_list)

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://wineapplication-a9530-default-rtdb.firebaseio.com/")
        totalnoofitem = findViewById(R.id.totalnoitem)
        totalnoofsum = findViewById(R.id.totalsum)
        val usernameInFirebase = auth.currentUser
        val userID = usernameInFirebase?.email?.replace(".", "")
        val username = userID.toString()

        val jayUserRef = databaseReference.child("users").child(username).child("wine categories")


    }
}