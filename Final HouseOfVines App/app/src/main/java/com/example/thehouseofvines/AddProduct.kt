package com.example.thehouseofvines

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.thehouseofvines.dataclass.Item
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddProduct : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var categorySpinner: Spinner
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var WineName: EditText
    private lateinit var WineDescription: EditText
    private lateinit var DateOfAcq: EditText
    private lateinit var Price: EditText
    private lateinit var SerialNumber: EditText
    private lateinit var AddBtn: Button
    private var selectedCategory: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://wineapplication-a9530-default-rtdb.firebaseio.com/")

        // Initialize the spinner
        categorySpinner = findViewById(R.id.categorySpinner)

        // Read the categories from Firebase and populate the spinner
        readCategoriesFromFirebase()

        // Set the item selection listener for the spinner
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedCategory = parent?.getItemAtPosition(position) as String
                // Call the private function with the selected category
                //doSomethingWithSelectedCategory()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case when nothing is selected
            }
        }

        WineName = findViewById(R.id.wineName)
        WineDescription = findViewById(R.id.wineDescription)
        DateOfAcq = findViewById(R.id.purchaseDate)
        Price = findViewById(R.id.purchasePrice)
        SerialNumber = findViewById(R.id.wineSerialNumber)
        AddBtn = findViewById(R.id.saveBtn)

        AddBtn.setOnClickListener(){
            gettingUserInput()
        }

    }

    private fun readCategoriesFromFirebase() {
        val usernameInFirebase = auth.currentUser
        val userID = usernameInFirebase?.email?.replace(".", "")
        val username = userID.toString()

        val jayUserRef = databaseReference.child("users").child(username)
        val categoriesRef = jayUserRef.child("wine categories")

        // Read the categories from Firebase
        categoriesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val categories = ArrayList<String>()
                for (snapshot in dataSnapshot.children) {
                    val categoryName = snapshot.key
                    categories.add(categoryName.toString())
                }

                // Create an ArrayAdapter to populate the spinner
                val adapter = ArrayAdapter(
                    this@AddProduct,
                    android.R.layout.simple_spinner_item,
                    categories
                )
                // Set the dropdown layout style
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Set the adapter to the spinner
                categorySpinner.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database read error
            }
        })
    }

    private fun gettingUserInput(){
        val name = WineName.getText().toString()
        val description = WineDescription.getText().toString()
        val date = DateOfAcq.getText().toString()
        val price = Price.getText().toString()
        val serialNumber = SerialNumber.getText().toString()

        if (name.isEmpty()) {
            WineName.setError("Can not be Empty")
            WineName.requestFocus()
        }
        if (description.isEmpty()) {
            WineDescription.error = "Can not be empty"
            WineDescription.requestFocus()
        }
        if (date.isEmpty()) {
            DateOfAcq.error = "Can not be empty"
            DateOfAcq.requestFocus()
        }
        if (price.isEmpty()) {
            Price.error = "Can not be empty"
            Price.requestFocus()
        }
        if (serialNumber.isEmpty()) {
            SerialNumber.error = "Can not be empty"
            SerialNumber.requestFocus()
        }

        if (name.isNotEmpty() && description.isNotEmpty() && date.isNotEmpty()
            && price.isNotEmpty() && serialNumber.isNotEmpty()){

            val usernameInFirebase = auth.currentUser
            val userID = usernameInFirebase?.email?.replace(".", "")
            val username = userID.toString()
            val productDetails = Item(name, description, date, price, serialNumber)

            val jayUserRef = databaseReference.child("users").child(username)
            val categoriesRef = jayUserRef.child("wine categories").child(selectedCategory)
            val wineDetail = categoriesRef.child("wine details")
            wineDetail.addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild(serialNumber)) {
                        Toast.makeText(this@AddProduct, "Product Details is already registered ", Toast.LENGTH_LONG).show()
                    } else {
                        databaseReference.child("users").child(username).child("wine categories").child(selectedCategory).child("wine details").child(serialNumber).setValue(productDetails)
                        // Category information saved successfully
                        Toast.makeText(this@AddProduct, "Details added successfully", Toast.LENGTH_LONG).show()
                        startActivity(
                            Intent(this@AddProduct, Photograph::class.java).also {
                                it.putExtra("selectedCategory", selectedCategory)
                                startActivity(it)
                                finish()
                            }
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle onCancelled event if needed
                    Toast.makeText(this@AddProduct, "Adding Item Failed", Toast.LENGTH_LONG).show()
                }
            })
            }

    }
}