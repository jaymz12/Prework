package com.example.thehouseofvines

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth

class Dashboard : AppCompatActivity() {
    private lateinit var addCategory: CardView
    private lateinit var addItem: CardView
    private lateinit var viewProduct: CardView
    private lateinit var viewList: CardView
    private lateinit var firebasenameview: TextView
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        firebasenameview = findViewById(R.id.username)

        val users = firebaseAuth.currentUser
        val finaluser = users!!.email
        val result = finaluser!!.substring(0, finaluser!!.indexOf("@"))
        val resultemail = result.replace(".", "")
        firebasenameview.text = "Welcome, $resultemail"

        addCategory = findViewById<androidx.cardview.widget.CardView>(R.id.addCategory)
        addItem = findViewById<androidx.cardview.widget.CardView>(R.id.addItem)
        viewProduct = findViewById<androidx.cardview.widget.CardView>(R.id.viewProduct)
        viewList = findViewById<androidx.cardview.widget.CardView>(R.id.viewList)

        addCategory.setOnClickListener(){
            onClick(addCategory)
        }
        addItem.setOnClickListener(){
            onClick(addItem)
        }
        viewProduct.setOnClickListener(){
            onClick(viewProduct)
        }
        viewList.setOnClickListener(){
            onClick(viewList)
        }

    }

    fun onClick(view: View) {
        val i: Intent
        when (view.id) {
            R.id.addCategory -> {
                i = Intent(this, AddCategory::class.java)
                startActivity(i)
            }
            R.id.addItem -> {
                i = Intent(this, AddProduct::class.java)
                startActivity(i)
            }
            R.id.viewProduct -> {
                i = Intent(this, Achievement::class.java)
                startActivity(i)
            }
            R.id.viewList -> {
                i = Intent(this, Feed::class.java)
                startActivity(i)
            }
            else -> {}
        }
    }
}