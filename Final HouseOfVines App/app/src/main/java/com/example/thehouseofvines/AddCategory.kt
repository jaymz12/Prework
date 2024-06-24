package com.example.thehouseofvines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thehouseofvines.databinding.ActivityMainBinding
import com.example.thehouseofvines.dataclass.Category
import com.example.thehouseofvines.view.MyAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddCategory : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userList:ArrayList<Category>
    private lateinit var recv: RecyclerView
    private lateinit var userAdapter: MyAdapter
    private lateinit var databaseReference: DatabaseReference
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var addsBtn: FloatingActionButton
    private lateinit var mRecycler: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_add_category)

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://wineapplication-a9530-default-rtdb.firebaseio.com/")
        addsBtn = findViewById(R.id.addingBtn)
        userList = ArrayList()
        userAdapter = MyAdapter(this,userList)

        recv = findViewById(R.id.mRecycler)
        recv.layoutManager = LinearLayoutManager(this)
        recv.adapter = userAdapter

        // Fetch categories from Firebase
        fetchCategoriesFromFirebase()

        mRecycler = findViewById(R.id.mRecycler)

        addsBtn.setOnClickListener { addInfo() }

    }

    private fun fetchCategoriesFromFirebase() {
        val usernameInFirebase = auth.currentUser
        val userID = usernameInFirebase?.email?.replace(".", "")
        val username = userID.toString()

        val jayUserRef = databaseReference.child("users").child(username)
        val categoriesRef = jayUserRef.child("wine categories")

        categoriesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (categorySnapshot in snapshot.children) {
                    val categoryName = categorySnapshot.key
                    val categoryGoal = categorySnapshot.child("categoryGoal").value.toString()
                    val category = Category(categoryName.toString(), categoryGoal)
                    userList.add(category)
                }
                userAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event if needed
                Toast.makeText(this@AddCategory, "Failed to fetch categories", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun addInfo() {
        val inflater = LayoutInflater.from(this)
        val usernameInFirebase = auth.currentUser
        val userID = usernameInFirebase?.email?.replace(".", "")
        val username = userID.toString()
        val v = inflater.inflate(R.layout.activity_add_category_format,null)

        /** Set view */
        val userName = v.findViewById<EditText>(R.id.CategoryName)
        val userNo = v.findViewById<EditText>(R.id.CategoryGoal)

        val addDialog = AlertDialog.Builder(this)

        addDialog.setView(v)
        addDialog.setPositiveButton("Ok") { dialog, _ ->
            val names = userName.text.toString()
            val number = userNo.text.toString()

            /** Create a category object */
            val category = Category(names, number)
            val jayUserRef = databaseReference.child("users").child(username)
            // Save the category information under the "categories" child
            val categoriesRef = jayUserRef.child("wine categories")
            categoriesRef.addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild(names)) {
                        Toast.makeText(this@AddCategory, "Category Name is already registered ", Toast.LENGTH_LONG).show()
                    } else {
                        databaseReference.child("users").child(username).child("wine categories").child(names).setValue(category)
                        // Category information saved successfully
                        Toast.makeText(this@AddCategory, "Category added successfully", Toast.LENGTH_LONG).show()
                        userList.add(category)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle onCancelled event if needed
                    Toast.makeText(this@AddCategory, "Category Failed", Toast.LENGTH_LONG).show()
                }
            })
            dialog.dismiss()
        }

        addDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
            Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
        }

        addDialog.create()
        addDialog.show()
    }


}