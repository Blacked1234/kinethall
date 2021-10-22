package com.example.kinethall

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.get
import com.example.kinethall.fragments.HomeFragment
import com.example.kinethall.fragments.PetFragment
import com.example.kinethall.fragments.ProfileFragment
import com.example.kinethall.fragments.SearchFragment
import com.example.kinethall.fragments.adapters.ViewPagerAdapter
import com.example.kinethall.fragmentsc.CameraFragment
import kotlinx.android.synthetic.main.activity_2.*

class Activity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)

        supportActionBar?.hide()

        setUpTabs()

        val intent: Bundle? = intent.extras
        if (intent != null)
        {
            val publisher : String? = intent.getString("publisherid")

            val editor: SharedPreferences.Editor  = getSharedPreferences("PREFS", MODE_PRIVATE).edit()
            editor.putString("profileid", publisher)
            editor.apply()

            //supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ProfileFragment()).commit()
        }
        else
        {
            //supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
        }


    }

    private fun setUpTabs()
    {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(HomeFragment(), "Home")
        adapter.addFragment(SearchFragment(), "Search")
        adapter.addFragment(CameraFragment(), "Camera")
        adapter.addFragment(ProfileFragment(), "Profile")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_home_24_white)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_baseline_search_24_white)
        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_baseline_camera_24_white)
        tabs.getTabAt(3)!!.setIcon(R.drawable.ic_paw__1_)
        tabs.getTabAt(4)!!.setIcon(R.drawable.ic_baseline_apps_24_white)
    }
}