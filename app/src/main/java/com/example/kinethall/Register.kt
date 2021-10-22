package com.example.kinethall

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()

        bRegister.setOnClickListener {
            CreateAccount()
        }
    }

    private fun CreateAccount() {
        val userName = etUsername1.text.toString()
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        when{
            TextUtils.isEmpty(userName) -> Toast.makeText(this, "username is required.", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(email) -> Toast.makeText(this, "email is required.", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(password) -> Toast.makeText(this, "password is required.", Toast.LENGTH_LONG).show()

            else ->{

                val progressDialog = ProgressDialog(this@Register)
                progressDialog.setTitle("Sign Up")
                progressDialog.setMessage("Please wait, this may take a while...")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task->
                        if (task.isSuccessful)
                        {
                            saveUserInfo(userName, email, progressDialog)
                        }
                        else{
                            val message = task.exception!!.toString()
                            Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
                            mAuth.signOut()
                            progressDialog.dismiss()
                        }
                    }
            }
        }

    }

    private fun saveUserInfo(userName: String, email: String, progressDialog: ProgressDialog) {
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val userRef: DatabaseReference = FirebaseDatabase.getInstance("https://speedtest-b22c2-default-rtdb.europe-west1.firebasedatabase.app").reference.child("Users")
        val userMap = HashMap<String, Any>()
        userMap["uid"] = currentUserId
        userMap["username"] = userName
        userMap["email"] = email
        userMap["bio"] = "Hi, I'm an entrepreneur"
        userMap["image"] = "https://firebasestorage.googleapis.com/v0/b/kinethall-5940f.appspot.com/o/Default%20Images%2Fprofile.png?alt=media&token=b0ce4a36-aacb-4dd8-8dad-cb31c4cb54b2"

        userRef.child(currentUserId).setValue(userMap)
            .addOnCompleteListener { task->
                if (task.isSuccessful)
                {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Account has been created successfully.", Toast.LENGTH_LONG).show()

                    val intent = Intent(this@Register, Activity2::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }else
                {
                    val message = task.exception!!.toString()
                    Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
                    FirebaseAuth.getInstance().signOut()
                    progressDialog.dismiss()
                }
            }
    }
}