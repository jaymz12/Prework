package com.example.thehouseofvines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Achievement : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var txtStarter:TextView
    private lateinit var txtCollector:TextView
    private lateinit var txtPackrat: TextView
    private lateinit var message: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievement)

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://wineapplication-a9530-default-rtdb.firebaseio.com/")
        val usernameInFirebase = auth.currentUser
        val userID = usernameInFirebase?.email?.replace(".", "")
        val username = userID.toString()

        txtStarter = findViewById(R.id.tvStarter)
        txtCollector = findViewById(R.id.tvCollector)
        txtPackrat = findViewById(R.id.tvPackrat)
        message = findViewById(R.id.message)

        val jayUserRef = databaseReference.child("users").child(username).child("wine categories")

        // Read the categories from Firebase
        jayUserRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val categories = ArrayList<String>()
                for (snapshot in dataSnapshot.children) {
                    val categoryName = snapshot.key
                    categories.add(categoryName.toString())

                    if (snapshot.childrenCount.toInt() < 0){
                        message.visibility = View.VISIBLE
                    }
                    else if (snapshot.childrenCount.toInt() == 1 && snapshot.childrenCount.toInt() < 3){
                        txtStarter.visibility = View.VISIBLE
                    }
                    else if(snapshot.childrenCount.toInt() >= 3  && snapshot.childrenCount.toInt() < 10){
                        txtCollector.visibility = View.VISIBLE
                    }
                    else if(snapshot.childrenCount.toInt() > 10){
                        txtPackrat.visibility = View.VISIBLE
                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database read error
            }
        })
    }
}