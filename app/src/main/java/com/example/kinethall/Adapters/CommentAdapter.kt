package com.example.kinethall.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.kinethall.Activity2
import com.example.kinethall.Model.Comment
import com.example.kinethall.Model.Post
import com.example.kinethall.Model.User
import com.example.kinethall.R
import com.example.kinethall.fragments.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class CommentAdapter(private val mContext: Context,
                     private val mComment: List<Comment>) : RecyclerView.Adapter<CommentAdapter.ViewHolder>()
{

    private var firebaseUser: FirebaseUser? = null


    inner class ViewHolder (@NonNull itemView: View) : RecyclerView.ViewHolder(itemView)
    {

        var imageprofile: ImageView
        var username: TextView
        var comment: TextView

        init {
            imageprofile = itemView.findViewById(R.id.image_profile_com_show)
            username = itemView.findViewById(R.id.username_com_show)
            comment = itemView.findViewById(R.id.comment)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        val comment = mComment[position]
        holder.comment.text = comment.getComment()
        getUserInfo(holder.imageprofile, holder.comment, comment.getPublisher())

        holder.comment.setOnClickListener(View.OnClickListener {
            var intent = Intent(mContext, HomeFragment::class.java)
            intent.putExtra("publisherid", comment.getPublisher())
            mContext.startActivity(intent)
        })
    }

    override fun getItemCount(): Int {
        return mComment.size
    }

    private fun getUserInfo(imageView: ImageView, username: TextView, publisherid: String)
    {
        var refer = FirebaseDatabase.getInstance("https://speedtest-b22c2-default-rtdb.europe-west1.firebasedatabase.app").reference.child("Users")
            .child(publisherid)

        refer.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                Picasso.get().load(user!!.getImage()).fit().placeholder(R.drawable.ic_dogprofile).into(imageView)
                username.text = user.getUsername()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}