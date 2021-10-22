package com.example.kinethall.Adapters

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
import com.example.kinethall.Model.Post
import com.example.kinethall.PostDetailsActivity
import com.example.kinethall.R
import com.example.kinethall.fragments.PostDetailFragment
import com.example.kinethall.fragments.ProfileFragment
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile.*

class MyFotosAdapter(private val mContext: Context,
                     private val mPost: List<Post>) : RecyclerView.Adapter<MyFotosAdapter.ViewHolder>()
{
    inner class ViewHolder (@NonNull itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var postImage: ImageView

        init {
            postImage = itemView.findViewById(R.id.post_image)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fotos_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = mPost[position]
        Picasso.get().load(post.getPostimage()).into(holder.postImage)

        holder.postImage.setOnClickListener{
            val editor: SharedPreferences.Editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            editor.putString("postid", post.getPostid())
            editor.apply()

            val intent = Intent(mContext, PostDetailsActivity::class.java)
            mContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mPost.size
    }


}