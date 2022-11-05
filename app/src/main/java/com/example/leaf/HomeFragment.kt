package com.example.leaf

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.leaf.beauty.BeautywriteActivity
import com.example.leaf.databinding.FragmentHomeBinding

import com.example.leaf.food.FoodwriteActivity
import com.example.leaf.house.housewriteActivity
import com.example.leaf.movie.MoviewriteActivity


class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): FrameLayout {
        val binding = FragmentHomeBinding.inflate(inflater,container,false)
        binding.foodplusBtn.setOnClickListener {
            startActivity(Intent(activity, FoodwriteActivity::class.java))
        }

        binding.movieplusBtn.setOnClickListener {
            startActivity(Intent(activity, MoviewriteActivity::class.java))
        }

        binding.beautyplusBtn.setOnClickListener {
            startActivity(Intent(activity, BeautywriteActivity::class.java))
        }

        binding.houseplusBtn.setOnClickListener {
            startActivity(Intent(activity, housewriteActivity::class.java))
        }

        return binding.root
    }


}