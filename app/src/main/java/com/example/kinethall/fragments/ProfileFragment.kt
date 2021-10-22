package com.example.kinethall.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kinethall.*
import com.example.kinethall.Adapters.MyFotosAdapter
import com.example.kinethall.Model.Post
import com.example.kinethall.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlin.collections.ArrayList


class ProfileFragment : Fragment() {

    private lateinit var profileId: String
    private lateinit var firebaseUser: FirebaseUser
    private var postList: MutableList<Post>? = null
    private var myFotoAdapter: MyFotosAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        if (pref != null) {
            this.profileId = pref.getString("profileId", "none").toString()
        }

        if (profileId == firebaseUser.uid)
        {
            view.button5.text = "Edit Profile"
        }

        var recyclerView: RecyclerView? = null
        recyclerView = view.findViewById(R.id.view_of_pics_profile)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = GridLayoutManager(context, 3)
        recyclerView.layoutManager = linearLayoutManager
        linearLayoutManager.reverseLayout = true
        //linearLayoutManager.stackFromEnd = true


        postList = ArrayList()
        myFotoAdapter = context?.let { MyFotosAdapter(it, postList as ArrayList<Post>) }
        recyclerView.adapter = myFotoAdapter


        /*else if (profileId != firebaseUser.uid)
        {
            checkFollowAndFollowingButtonStatus()
        }*/

        view.button5.setOnClickListener {
            startActivity(Intent(context, ProfileSettings::class.java))
        }

        view.friends.setOnClickListener {
            startActivity(Intent(context, Friends::class.java))
        }

        /*view.dashboard.setOnClickListener {
            startActivity(Intent(context, Board::class.java))
        }*/

        //getFollowers()
        //getFollowings()
        userInfo()
        myFotos()

        view.bLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        return view
    }

    private fun userInfo()
    {
        val usersRef = FirebaseDatabase.getInstance("https://speedtest-b22c2-default-rtdb.europe-west1.firebasedatabase.app").reference.child("Users").child(firebaseUser.uid)

        usersRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {
                    val user = p0.getValue<User>(User::class.java)
                    view?.etusername?.text = user!!.getUsername()
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.ic_dogprofile).into(view?.profile_image_2)
                    /*profile_image_2.load(user!!.getImage()) {
                        crossfade(true)
                        placeholder(R.drawable.ic_dogprofile)*/
                    //}
                }
            }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

    private fun myFotos() {
        var myRef = FirebaseDatabase.getInstance("https://speedtest-b22c2-default-rtdb.europe-west1.firebasedatabase.app").reference.child("Posts")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                postList?.clear()
                for (snapShot in snapshot.children) {
                    val post = snapShot.getValue(Post::class.java)
                    if (post!!.getPublisher() != profileId) {
                        postList!!.add(post)
                    }
                }
                //postList?.reverse()
                myFotoAdapter!!.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


    /*private fun checkFollowAndFollowingButtonStatus()
    {
        val followingRef = firebaseUser?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Add").child(it1.toString())
                .child("Following")
        }

        if (followingRef != null)
        {
            followingRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.child(profileId).exists()) {
                        view?.button5?.text = "Remove"
                    } else {
                        view?.button5?.text = "Add"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }*/

    /*private fun getFollowers() {
        val followersRef = FirebaseDatabase.getInstance().reference
                .child("Add").child(profileId)
                .child("Followers")

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
        val followingsRef = FirebaseDatabase.getInstance().reference
                .child("Add").child(profileId)
                .child("Following")

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
    }*/

    /*private fun userInfo()
    {
        val usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(profileId)
        usersRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                //if (context != null)
                //{
                //    return
                //}

                if (p0.exists())
                {
                    val user = p0.getValue<User>(User::class.java)

                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.ic_dogprofile).into(view?.profile_image_2)
                    view?.etusername?.text = user!!.getUsername()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }*/

    /*override fun onStop() {
        super.onStop()

        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUser.uid)
        pref?.apply()

    }

    override fun onDestroy() {
        super.onDestroy()

        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUser.uid)
        pref?.apply()

    }

    override fun onPause() {
        super.onPause()

        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUser.uid)
        pref?.apply()

    }*/


}