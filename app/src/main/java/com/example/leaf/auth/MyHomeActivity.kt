package com.example.leaf.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.leaf.R
import com.example.leaf.databinding.ActivityMyHomeBinding
import com.example.leaf.databinding.ActivityProfileEditBinding

class MyHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mainEditbtn.setOnClickListener {
            val intent = Intent(this, ProfileEditActivity::class.java)
            startActivity(intent)
        }

    }
}