package com.example.kinethall.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kinethall.Adapters.PostAdapter
import com.example.kinethall.Model.Post
import com.example.kinethall.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class PostDetailFragment : Fragment() {

    private var postAdapter: PostAdapter? = null
    private var postList: MutableList<Post>? = null
    private var postid: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post_detail, container, false)

        var recyclerView: RecyclerView? = null
        var preferences: SharedPreferences? = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        postid = preferences?.getString("postid", "none")

        recyclerView = view.findViewById(R.id.recycler_view_det)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager

        postList = ArrayList()
        postAdapter = context?.let { PostAdapter(it, postList as ArrayList<Post>) }
        recyclerView.adapter = postAdapter

        readPost()


        return view
    }

    private fun readPost() {
        val reffs = postid?.let {
            FirebaseDatabase.getInstance("https://speedtest-b22c2-default-rtdb.europe-west1.firebasedatabase.app").getReference("Posts").child(it)
        }

        reffs?.addValueEventListener(object: ValueEventListener{
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