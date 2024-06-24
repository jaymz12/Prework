package com.example.thehouseofvines

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var forgotPassword: TextView
    private lateinit var userLoginBtn: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var passwordresetemail: EditText
    private lateinit var processDialog: ProgressDialog
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.username)
        password = findViewById(R.id.passwordtxtBox)
        forgotPassword = findViewById(R.id.forgotPassword)
        userLoginBtn = findViewById(R.id.loginbtn)
        passwordresetemail = findViewById(R.id.username)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE
        processDialog = ProgressDialog(this)

        userLoginBtn.setOnClickListener(){
            validate(email.getText().toString(), password.getText().toString())
        }

        forgotPassword.setOnClickListener {
            resetPassword()
        }
    }

    fun validate(userEmail: String, userPassword: String) {
        val progressDialog = ProgressDialog(this@Login)
        progressDialog.setMessage("................PLEASE WAIT................")
        progressDialog.show()

        auth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressDialog.dismiss()
                    Toast.makeText(this@Login, "Login Successful", Toast.LENGTH_SHORT).show()
                    startActivity(
                        Intent(
                            this@Login, Dashboard::class.java
                        )
                    )
                } else {
                    Toast.makeText(this@Login, "Login Failed", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            }
    }

    fun resetPassword() {
        val resetEmail = passwordresetemail.text.toString()

        if (resetEmail.isEmpty()) {
            passwordresetemail.error = "It's empty"
            passwordresetemail.requestFocus()
            return
        }

        progressBar.visibility = View.VISIBLE
        auth.sendPasswordResetEmail(resetEmail)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@Login, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@Login, "Failed to send reset email!", Toast.LENGTH_SHORT).show()
                }
                progressBar.visibility = View.GONE
            }
    }

}