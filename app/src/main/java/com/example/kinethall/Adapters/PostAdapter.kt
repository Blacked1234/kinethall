package com.example.kinethall.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.kinethall.*
import com.example.kinethall.Model.Post
import com.example.kinethall.Model.User
import com.example.kinethall.R
import com.example.kinethall.fragments.PostDetailFragment
import com.example.kinethall.fragments.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_change_username.*
import kotlinx.android.synthetic.main.activity_image_change.*
import kotlinx.android.synthetic.main.fragment_profile.*

class PostAdapter(private val mContext: Context,
                  private val mPost: List<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>()
{
    private var firebaseUser: FirebaseUser? = null

    inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var profileImage: CircleImageView
        var postImage: ImageView
        var commentButton: ImageView
        var saveButton: ImageView
        var userName: TextView
        var publisher: TextView
        var description: TextView
        var comments: TextView

        init {
            profileImage = itemView.findViewById(R.id.user_profile_image_home)
            postImage = itemView.findViewById(R.id.post_image_home)
            commentButton = itemView.findViewById(R.id.post_image_comment_btn)
            saveButton = itemView.findViewById(R.id.post_save_comment_btn)
            userName = itemView.findViewById(R.id.user_name_search)
            publisher = itemView.findViewById(R.id.publisher)
            description = itemView.findViewById(R.id.description)
            comments = itemView.findViewById(R.id.comments)
        }
    }

    private fun getComments(postid: String, comments: TextView)
    {
        var refe: DatabaseReference = FirebaseDatabase.getInstance("https://speedtest-b22c2-default-rtdb.europe-west1.firebasedatabase.app").reference.child("Comments").child(postid)

        refe.addValueEventListener(object: ValueEventListener
        {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                comments.text = "All " + snapshot.childrenCount + " comments"
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(mContext).inflate(R.layout.posts_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        firebaseUser = FirebaseAuth.getInstance().currentUser


        val post = mPost[position]
        Picasso.get().load(post.getPostimage()).into(holder.postImage)


        publisherInfo(holder.profileImage, holder.userName, holder.description, holder.publisher, post.getPublisher(), post.getDescription())
        isSaved(post.getPostid(), holder.saveButton)
        getComments(post.getPostid(), holder.comments)

        /*holder.profileImage.setOnClickListener{
            val editor: SharedPreferences.Editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            editor.putString("profileid", post.getPublisher())
            editor.apply()

            val intent = Intent(mContext, PostDetailsActivity::class.java)
            mContext.startActivity(intent)

            //(mContext as FragmentActivity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ProfileFragment()).commit()
        }*/

        /*holder.userName.setOnClickListener{
            val editor: SharedPreferences.Editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            editor.putString("profileid", post.getPublisher())
            editor.apply()

            val intent = Intent(mContext, PostDetailsActivity::class.java)
            mContext.startActivity(intent)

            //(mContext as FragmentActivity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ProfileFragment()).commit()
        }*/

        /*holder.publisher.setOnClickListener{
            val editor: SharedPreferences.Editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            editor.putString("profileid", post.getPublisher())
            editor.apply()

            val intent = Intent(mContext, PostDetailsActivity::class.java)
            mContext.startActivity(intent)

            //(mContext as FragmentActivity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ProfileFragment()).commit()
        }*/

        holder.postImage.setOnClickListener{
            val editor: SharedPreferences.Editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            editor.putString("postid", post.getPostid())
            editor.apply()

            //(mContext as FragmentActivity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container, PostDetailFragment()).commit()

            val intent = Intent(mContext, PostDetailsActivity::class.java)
            mContext.startActivity(intent)
        }

        /*if (post.getDescription().isEmpty())
        {

        }*/
        holder.saveButton.setOnClickListener {
            if (holder.saveButton.getTag() == "save") {
                FirebaseDatabase.getInstance("https://speedtest-b22c2-default-rtdb.europe-west1.firebasedatabase.app").reference.child("Saves").child(firebaseUser!!.uid)
                    .child(post.getPostid()).setValue(true)
            }
            else
            {
                FirebaseDatabase.getInstance("https://speedtest-b22c2-default-rtdb.europe-west1.firebasedatabase.app").reference.child("Saves").child(firebaseUser!!.uid)
                    .child(post.getPostid()).removeValue()
            }
        }

        holder.commentButton.setOnClickListener(View.OnClickListener {
            var intent = Intent(mContext, CommentsActivity::class.java)
            intent.putExtra("postid", post.getPostid())
            intent.putExtra("publisherid", post.getPublisher())
            mContext.startActivity(intent)
        })
    }

    private fun publisherInfo(profileImage: CircleImageView, userName: TextView, description: TextView, publisher: TextView, publisherID: String, descriptionID: String)
    {
        val usersRef = FirebaseDatabase.getInstance("https://speedtest-b22c2-default-rtdb.europe-west1.firebasedatabase.app").reference.child("Users").child(publisherID)

        usersRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists())
                {
                    val user = p0.getValue<User>(User::class.java)

                    Picasso.get().load(user!!.getImage()).fit().placeholder(R.drawable.ic_dogprofile).into(profileImage)
                    userName.text = user!!.getUsername()
                    publisher.text = user!!.getUsername()
                    description.text = descriptionID
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun getItemCount(): Int
    {
        return mPost.size
    }

    private fun isSaved(postid: String, imageView: ImageView)
    {
        firebaseUser = FirebaseAuth.getInstance().currentUser

        var refer = FirebaseDatabase.getInstance("https://speedtest-b22c2-default-rtdb.europe-west1.firebasedatabase.app").reference.child("Saves")
            .child(firebaseUser!!.uid)

        refer.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(postid).exists())
                {
                    imageView.setImageResource(R.drawable.ic_baseline_done_24_white)
                    imageView.tag = "saved"
                }
                else
                {
                    imageView.setImageResource(R.drawable.ic_baseline_save_alt_24_white)
                    imageView.tag = "save"
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}