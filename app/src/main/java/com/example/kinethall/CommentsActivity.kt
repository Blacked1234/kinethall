package com.example.kinethall

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kinethall.Adapters.CommentAdapter
import com.example.kinethall.Model.Comment
import com.example.kinethall.Model.Post
import com.example.kinethall.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CommentsActivity : AppCompatActivity() {

    private var commentList: MutableList<Comment>? = null
    private var commentAdapter: CommentAdapter? = null

    private lateinit var profileImage: CircleImageView
    private lateinit var postComment: ImageButton
    private lateinit var addcoment: EditText

    private lateinit var postId: String
    private lateinit var publisherId: String

    private var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        supportActionBar?.hide()

        var recyclerView: RecyclerView? = null
        recyclerView = findViewById(R.id.recycler_view_com)
        recyclerView.setHasFixedSize(true)
        var linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        commentList = ArrayList()
        commentAdapter = CommentAdapter(this, commentList as ArrayList<Comment>)
        recyclerView.adapter = commentAdapter

        addcoment = findViewById(R.id.add_comment)
        postComment = findViewById(R.id.postt)
        profileImage = findViewById(R.id.image_profile_com)

        firebaseUser = FirebaseAuth.getInstance().currentUser

        var intent = Intent()
        postId = intent.getStringExtra("postid").toString()
        publisherId = intent.getStringExtra("publisherid").toString()

        postComment.setOnClickListener(View.OnClickListener {
            if(addcoment.text.toString().isEmpty())
            {
                Toast.makeText(this@CommentsActivity, "You can't send empty comment!", Toast.LENGTH_SHORT).show()
            }
            else
            {
                addComent()
            }
        })

        getImage()
        readComments()
    }

    private fun addComent() {
        val postRef: DatabaseReference = FirebaseDatabase.getInstance("https://speedtest-b22c2-default-rtdb.europe-west1.firebasedatabase.app").getReference("Comments").child(postId)
        val hashmap = HashMap<String, Any>()
        hashmap["comment"] = addcoment.text.toString()
        hashmap["publisher"] = firebaseUser!!.uid

        postRef.push().setValue(hashmap)
        addcoment.setText("")

    }
    private fun getImage()
    {
        val imageRef = FirebaseDatabase.getInstance("https://speedtest-b22c2-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users").child(firebaseUser!!.uid)

        imageRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    val user = snapshot.getValue(User::class.java)
                    Picasso.get().load(user!!.getImage()).fit().placeholder(R.drawable.ic_dogprofile).into(profileImage)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun readComments()
    {
        //Skorzystać z let z PostDetailFragment
        val comRef: DatabaseReference = FirebaseDatabase.getInstance("https://speedtest-b22c2-default-rtdb.europe-west1.firebasedatabase.app").getReference("Comments").child(postId)

        comRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                commentList?.clear()
                for (snapShot in snapshot.children)
                {
                    val comment: Comment? = snapShot.getValue(Comment::class.java)

                    comment?.let { commentList?.add(it) }
                }
                commentAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}