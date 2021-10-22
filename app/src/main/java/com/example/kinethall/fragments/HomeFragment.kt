package com.example.kinethall.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kinethall.Adapters.PostAdapter
import com.example.kinethall.AddPost
import com.example.kinethall.CameraActivity
import com.example.kinethall.Model.Post
import com.example.kinethall.ProfileSettings
import com.example.kinethall.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


class HomeFragment : Fragment() {

    private var postAdapter: PostAdapter? = null
    private var postList: MutableList<Post>? = null
    private var followingList: MutableList<Post>? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        view.bAddPost_ProfFrag.setOnClickListener {
            startActivity(Intent(context, CameraActivity::class.java))
        }

        var recyclerView: RecyclerView? = null
        recyclerView = view.findViewById(R.id.recycler_view_home)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        postList = ArrayList()
        postAdapter = context?.let { PostAdapter(it, postList as ArrayList<Post>) }
        recyclerView.adapter = postAdapter

        progressBar = view.findViewById(R.id.progress_circular)

        checkFollowings()

        return view
    }

    private fun checkFollowings() {
        followingList = ArrayList()

        val followingRef = FirebaseDatabase.getInstance("https://speedtest-b22c2-default-rtdb.europe-west1.firebasedatabase.app").reference
            .child("Add").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("Following")

        followingRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    (followingList as ArrayList<String>).clear()

                    for (snapShot in snapshot.children)
                    {
                        snapShot.key?.let { (followingList as ArrayList<String>).add(it) }
                    }

                    retrievePosts()

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


    }

    private fun retrievePosts() {
        val postsRef = FirebaseDatabase.getInstance("https://speedtest-b22c2-default-rtdb.europe-west1.firebasedatabase.app").reference.child("Posts")

        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                postList?.clear()

                for (snapShot in snapshot.children)
                {
                    val post = snapShot.getValue(Post::class.java)

                    for (id in (followingList as ArrayList<String>))
                    {
                        if (post!!.getPublisher() == id)
                        {
                            postList!!.add(post)
                        }
                        postAdapter!!.notifyDataSetChanged()
                        progressBar?.visibility = View.GONE
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}