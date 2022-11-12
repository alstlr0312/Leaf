package com.example.leaf.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.leaf.HomeFragment
import android.widget.Button
import com.example.leaf.R
import com.example.leaf.databinding.ActivityMyHomeBinding
import com.example.leaf.feed.FeedFragment
import com.example.leaf.setting.settingFragment
import com.example.leaf.databinding.ActivityMyHomeBinding
import com.example.leaf.databinding.ActivityProfileEditBinding

private const val TAG_HOME = "home_fragment"
private const val TAG_FEED = "feed_fragment"
private const val TAG_SET = "setting_fragement"
class MyHomeActivity : AppCompatActivity() {
    private lateinit var binding :  ActivityMyHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragment(TAG_HOME, HomeFragment())

        binding.navigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.homeFragment -> setFragment(TAG_HOME, HomeFragment())
                R.id.feedFragment -> setFragment(TAG_FEED, FeedFragment())
                R.id.settingFragment->setFragment(TAG_SET,settingFragment())
            }
            true
        }
    }

    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null){
            fragTransaction.add(R.id.mainFrameLayout, fragment, tag)
        }

        val home = manager.findFragmentByTag(TAG_HOME)
        val feed = manager.findFragmentByTag(TAG_FEED)
        val set = manager.findFragmentByTag(TAG_SET)


        if (home != null){
            fragTransaction.hide(home)
        }


        if (feed != null){
            fragTransaction.hide(feed)
        }

        if (set != null){
            fragTransaction.hide(set)
        }

        if (tag == TAG_HOME) {
            if (home!=null){
                fragTransaction.show(home)
            }
        }
        else if (tag == TAG_FEED) {
            if (feed != null) {
                fragTransaction.show(feed)
            }
        }

        else if (tag == TAG_SET){
            if (set != null){
                fragTransaction.show(set)
            }
        }

        fragTransaction.commitAllowingStateLoss()
        binding = ActivityMyHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mainEditbtn.setOnClickListener {
            val intent = Intent(this, ProfileEditActivity::class.java)
            startActivity(intent)
        }

    }
}