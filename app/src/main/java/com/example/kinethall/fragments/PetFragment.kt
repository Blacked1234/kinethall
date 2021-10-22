package com.example.kinethall.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kinethall.GroupWalks
import com.example.kinethall.R
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_pet.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class PetFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pet, container, false)

        view.imageButton1.setOnClickListener {
            startActivity(Intent(context, GroupWalks::class.java))
        }

        view.imageButton2.setOnClickListener {
            startActivity(Intent(context, GroupWalks::class.java))
        }



        return view
    }

}