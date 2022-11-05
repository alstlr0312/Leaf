package com.example.leaf.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.leaf.HomeFragment
import com.example.leaf.R
import com.example.leaf.databinding.ActivityMyHomeBinding
import com.google.android.material.navigation.NavigationBarView

class MyHomeActivity : AppCompatActivity() {
    private val homeFragment: HomeFragment by lazy { HomeFragment() }
    private val binding by lazy { ActivityMyHomeBinding.inflate(layoutInflater) }

    var backKeyPressedTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().add(R.id.bottom_containers, mapFragment).commit()
        val navigationBarView = binding.bottomNavigationview
        navigationBarView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.mainMapFragment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.bottom_containers, mapFragment)
                        .commit()
                    return@OnItemSelectedListener true
                }
                R.id.postFragment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.bottom_containers, homeFragment)
                        .commit()
                    return@OnItemSelectedListener true
                }
                R.id.profileFragment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.bottom_containers, profileFragment)
                        .commit()
                    return@OnItemSelectedListener true
                }
            }
            false
        })
    }

    override fun onBackPressed() {
        //super.onBackPressed()

        if(System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            return;
        } else {
            finishAffinity()
        }
    }
}