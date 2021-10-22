package com.example.kinethall

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Friends : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        supportActionBar?.hide()


    }
}