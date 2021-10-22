package com.example.kinethall

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        val button3 = findViewById<Button>(R.id.bSignUp)
        button3.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
    }
        mAuth = FirebaseAuth.getInstance()

        bLogin.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser()
    {
        val email: String = etEmail2.text.toString()
        val password: String = etPassword2.text.toString()

        if(email.isNullOrEmpty())
        {
            Toast.makeText(this@Login, "Provide email!", Toast.LENGTH_LONG).show()
        }
        else if(password.isNullOrEmpty())
        {
            Toast.makeText(this@Login, "Provide password!", Toast.LENGTH_LONG).show()
        }
        else
        {
            mAuth?.signInWithEmailAndPassword(email, password)
                ?.addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        val intent = Intent(this@Login, Activity2::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@Login, "Error with signing ip! " + task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}