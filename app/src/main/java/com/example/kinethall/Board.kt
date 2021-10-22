package com.example.kinethall

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kinethall.Adapters.MyFotosAdapter
import com.example.kinethall.Model.Post
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Board : AppCompatActivity() {

    private var mySaves: MutableList<String>? = null
    private var postListSave: MutableList<Post>? = null
    private var myFotoAdapterSave: MyFotosAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        supportActionBar?.hide()

        var recyclerView: RecyclerView? = null
        recyclerView = findViewById(R.id.view_of_pics_profile_save)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = GridLayoutManager(this@Board, 3)
        recyclerView.layoutManager = linearLayoutManager
        linearLayoutManager.reverseLayout = true


        postListSave = ArrayList()
        myFotoAdapterSave = this@Board?.let { MyFotosAdapter(it, postListSave as ArrayList<Post>) }
        recyclerView.adapter = myFotoAdapterSave

        //mysaves()


    }

    private fun mysaves() {
        var myRef = FirebaseDatabase.getInstance("https://speedtest-b22c2-default-rtdb.europe-west1.firebasedatabase.app").reference.child("Saves")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snapShot in snapshot.children) {
                    snapShot.key?.let { mySaves!!.add(it) }

                }
                readSaves()

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun readSaves()
    {
        var myRefs = FirebaseDatabase.getInstance("https://speedtest-b22c2-default-rtdb.europe-west1.firebasedatabase.app").reference.child("Posts")
        myRefs.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                postListSave?.clear()
                for(snapShot in snapshot.children)
                {
                    val post = snapShot.getValue(Post::class.java)
                    for(id in mySaves as ArrayList<String>)
                    {
                        if (post!!.getPostid() == id)
                        {
                            postListSave!!.add(post)
                        }
                    }
                }
                myFotoAdapterSave?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}