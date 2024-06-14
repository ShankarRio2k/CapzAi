package com.capztone.capzai

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.capztone.capzai.fragments.HomeFragment
import com.capztone.capzai.fragments.IdeaFragment
import com.capztone.capzai.fragments.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Change the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = getColor(R.color.pale_white)

            // Optionally, set the status bar text/icons color for API 23+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR // Use this for dark text/icons
            }
        }

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