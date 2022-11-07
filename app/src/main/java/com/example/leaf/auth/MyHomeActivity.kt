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