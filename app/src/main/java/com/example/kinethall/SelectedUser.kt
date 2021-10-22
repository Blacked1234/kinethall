package com.example.kinethall

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kinethall.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_selected_user.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class SelectedUser : AppCompatActivity() {

    private lateinit var profileId: String
    private lateinit var firebaseUser: FirebaseUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_user)

        supportActionBar?.hide()

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val pref = getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        if (pref != null) {
            this.profileId = pref.getString("profileId", "none").toString()
        }

        checkFollowAndFollowingButtonStatus()

        buttonAdd.setOnClickListener {
            val getButtonText = buttonAdd.text.toString()

            when
            {
                getButtonText == "Add" -> {

                    firebaseUser?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Add").child(it1.toString())
                            .child("Following").child(profileId)
                            .setValue(true)
                    }

                    firebaseUser?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Add").child(profileId)
                            .child("Followers").child(it1.toString())
                            .setValue(true)
                    }
                }

                getButtonText == "Remove" -> {

                    firebaseUser?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Add").child(it1.toString())
                            .child("Following").child(profileId)
                            .removeValue()
                    }

                    firebaseUser?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Add").child(profileId)
                            .child("Followers").child(it1.toString())
                            .removeValue()
                    }
                }
            }
        }

        getFollowers()
        getFollowings()
        userInfo()
    }


    private fun checkFollowAndFollowingButtonStatus()
    {
        val followingRef = firebaseUser?.uid.let { it1 ->
            FirebaseDatabase.getInstance("https://speedtest-b22c2-default-rtdb.europe-west1.firebasedatabase.app").reference
                .child("Add").child(it1.toString())
                .child("Following")
        }

        if (followingRef != null)
        {
            followingRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.child(profileId).exists()) {
                        buttonAdd?.text = "Remove"
                    } else {
                        buttonAdd?.text = "Add"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    private fun getFollowers() {
        val followersRef = firebaseUser?.uid.let { it1 ->
            FirebaseDatabase.getInstance("https://speedtest-b22c2-default-rtdb.europe-west1.firebasedatabase.app").reference
                .child("Add").child(it1.toString())
                .child("Followers")

        }
        followersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    //view?.total_friends_count?.text = p0.childrenCount.toString()
                    print("yes")
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun getFollowings() {
        val followingsRef = firebaseUser?.uid.let { it1 ->
            FirebaseDatabase.getInstance("https://speedtest-b22c2-default-rtdb.europe-west1.firebasedatabase.app").reference
                .child("Add").child(it1.toString())
                .child("Following")

        }
        followingsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    //view?.total_friends_count?.text = p0.childrenCount.toString()
                    print("yes")
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun userInfo()
    {
        val usersRef = FirebaseDatabase.getInstance("https://speedtest-b22c2-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Users").child(profileId)

        usersRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists())
                {
                    val user = p0.getValue<User>(User::class.java)

                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.ic_dogprofile).into(profile_image_someone)
                    username_someone?.text = user!!.getUsername()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}