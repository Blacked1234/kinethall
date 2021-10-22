package com.example.kinethall

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.kinethall.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_change_username.*
import kotlinx.android.synthetic.main.activity_profile_settings.*

class ChangeUsername : AppCompatActivity() {

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var profileId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_username)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        supportActionBar?.hide()

        bConfirmChangeUsername.setOnClickListener {
                udpateUserInfoOnly()
        }

        userInfo()
    }

    private fun udpateUserInfoOnly()
    {
        when {
            etChangeUsername1.text.toString() == "" -> {
                Toast.makeText(this, "Please write username first!", Toast.LENGTH_LONG).show()
            }
            TextUtils.isEmpty(etChangeUsername1.text.toString()) -> {
                Toast.makeText(this, "Please write username first!", Toast.LENGTH_LONG).show()
            }
            else -> {
                val usersRef = FirebaseDatabase.getInstance("https://speedtest-b22c2-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Users")

                val userMap = HashMap<String, Any>()
                userMap["username"] = etChangeUsername1.text.toString()

                usersRef.child(firebaseUser.uid).updateChildren(userMap)

                Toast.makeText(this, "Username changed successfully!", Toast.LENGTH_LONG).show()

                val intent = Intent(this@ChangeUsername, Activity2::class.java)
                startActivity(intent)
                finish()
            }
        }

    }

    private fun userInfo()
    {
        val usersRef = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid)

        usersRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists())
                {
                    val user = p0.getValue<User>(User::class.java)
                    etChangeUsername1.setText(user!!.getUsername())
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}