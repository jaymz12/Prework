package com.example.thehouseofvines

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Success : AppCompatActivity() {
    private lateinit var MainBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)

        MainBtn = findViewById(R.id.doneBtn)

        MainBtn.setOnClickListener(){
            startActivity(
                Intent(
                    this@Success,
                    Dashboard::class.java
                )
            )
        }
    }
}