package com.example.thehouseofvines

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var UserRegisterBtn: Button
    private lateinit var UserLoginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        UserRegisterBtn = findViewById(R.id.signup)
        UserLoginBtn = findViewById(R.id.login)

        UserRegisterBtn.setOnClickListener(){
            startActivity(
                Intent(
                    this@MainActivity,
                    Signup::class.java
                )
            )
        }
        UserLoginBtn.setOnClickListener(){
            startActivity(
                Intent(
                    this@MainActivity,
                    Login::class.java
                )
            )
        }

    }
}