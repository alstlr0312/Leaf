package com.example.leaf

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.leaf.beauty.BeautywriteActivity
import com.example.leaf.databinding.FragmentHomeBinding
import com.bumptech.glide.Glide
import com.example.leaf.Utils.FBAuth
import com.example.leaf.Utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

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

        private lateinit var auth: FirebaseAuth
    //프래그먼트와 레이아웃을 연결시켜주는 부분
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FrameLayout {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        val profileName = binding.mainProfile
        val profileintroduce = binding.mainIntroduce
        val key = FBRef.profileRef.key.toString()
        val storageReference = Firebase.storage.reference.child(key +".png")
        val imageViewFromFB = binding.profileImageview

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener {
            task ->
            if(task.isSuccessful)
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
        })

        auth = Firebase.auth
        profileName.setText(FBAuth.getDisplayName()) //프로필 이름
        FBRef.profileRef.child("introduce").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue<String>()
                Log.d(ContentValues.TAG, "Value is: $value")
                binding.mainIntroduce.setText(value)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        return binding.root
    }



}


