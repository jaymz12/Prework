package com.example.thehouseofvines

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.thehouseofvines.dataclass.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Signup : AppCompatActivity() {
    private lateinit var Username: EditText
    private lateinit var Name: EditText
    private lateinit var Email: EditText
    private lateinit var Password: EditText
    private lateinit var ConfirmPassword: EditText
    private lateinit var UserRegisterBtn: Button
    private lateinit var progressBar: ProgressBar
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        Username = findViewById(R.id.username)
        Name = findViewById(R.id.fullName)
        Email = findViewById(R.id.email)
        Password = findViewById(R.id.password)
        ConfirmPassword = findViewById(R.id.confirm_pass)
        UserRegisterBtn = findViewById(R.id.signup_btn)

        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE

        UserRegisterBtn.setOnClickListener(){
            registerUser()
        }
    }

    override fun onStart() {
        super.onStart()

        if (mAuth.currentUser != null) {
            // handle the already logged-in user
        }
    }


    private fun registerUser()
    {
        val name = Username.text.toString()
        val fullname = Name.text.toString()
        val email = Email.text.toString()
        val password = Password.text.toString()
        val cpassword = ConfirmPassword.text.toString()

        if (name.isEmpty()) {
            Username.setError("Can not be Empty")
            Username.requestFocus()
            return
        }
        else if (fullname.isEmpty()) {
            Name.setError("Can not be Empty")
            Name.requestFocus()
            return
        }
        else if (email.isEmpty()) {
            Email.error = "Can not be empty"
            Email.requestFocus()
            return
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Email.error = "Not a valid email address"
            Email.requestFocus()
            return
        }
        else if (password.isEmpty()) {
            Password.error = "Password field cannot be empty"
            Password.requestFocus()
            return
        }
        else if (password.length < 6) {
            Password.error = "Password less than 6"
            Password.requestFocus()
            return
        }
        else if (cpassword != password) {
            ConfirmPassword.setError("Password Do not Match")
            ConfirmPassword.requestFocus()
            return
        }

        else{
            progressBar.visibility = View.VISIBLE
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = User(
                            name,
                            fullname,
                            email,
                            password
                        )
                        val usernameInFirebase = mAuth.currentUser
                        var userID = usernameInFirebase?.email?.replace(".", "")

                        FirebaseDatabase.getInstance().getReference("users")
                            .child(userID.toString()).setValue(user).addOnCompleteListener { task ->
                                progressBar.visibility = View.GONE
                                if (task.isSuccessful) {
                                    Toast.makeText(this@Signup, "Registration Success, Please Sign in", Toast.LENGTH_LONG).show()
                                    startActivity(Intent(this@Signup, Login::class.java))
                                } else {
                                    // display a failure message
                                }
                            }
                    } else {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@Signup, "Registration Failed", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

}