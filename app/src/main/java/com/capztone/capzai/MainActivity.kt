package com.capztone.capzai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.capztone.capzai.fragments.HomeFragment
import com.capztone.capzai.fragments.IdeaFragment
import com.capztone.capzai.fragments.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.navigation_home -> selectedFragment = HomeFragment()
                R.id.navigation_idea -> selectedFragment = IdeaFragment()
                R.id.navigation_settings -> selectedFragment = SettingsFragment()
            }
            selectedFragment?.let {
                supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, it).commit()
            }
            true
        }

        // Set the default fragment
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.navigation_home
        }

    }
}