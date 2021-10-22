package com.example.kinethall

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kinethall.Adapters.PostAdapter
import com.example.kinethall.Model.Post
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostDetailsActivity : AppCompatActivity() {

    private var postAdapter: PostAdapter? = null
    private var postList: MutableList<Post>? = null
    private var postid: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)

        supportActionBar?.hide()

        var recyclerView: RecyclerView? = null
        var preferences: SharedPreferences? = getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        postid = preferences?.getString("postid", "none")

        recyclerView = findViewById(R.id.recycler_view_det)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        postList = ArrayList()
        postAdapter = this?.let { PostAdapter(it, postList as ArrayList<Post>) }
        recyclerView.adapter = postAdapter

        readPost()
    }

    private fun readPost() {
        val reffs = postid?.let {
            FirebaseDatabase.getInstance("https://speedtest-b22c2-default-rtdb.europe-west1.firebasedatabase.app").getReference("Posts").child(it)
        }

        reffs?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                postList?.clear()
                val post = snapshot.getValue(Post::class.java)
                if (post != null) {
                    postList?.add(post)
                }
                postAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}