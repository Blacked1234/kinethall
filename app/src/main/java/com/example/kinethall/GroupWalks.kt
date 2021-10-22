package com.example.kinethall

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kinethall.Adapters.UserAdapter
import com.example.kinethall.Model.User

class GroupWalks : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var mGroup: MutableList<User>? = null
    private var groupAdapter:

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_walks)

        recyclerView = findViewById(R.id.recycler_view_search_groupwalks)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(this)

        mGroup = ArrayList()



    }
}