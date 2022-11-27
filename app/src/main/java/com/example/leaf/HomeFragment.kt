package com.example.leaf

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.leaf.beauty.BeautywriteActivity
import com.example.leaf.databinding.FragmentHomeBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.leaf.Utils.FBAuth
import com.example.leaf.Utils.FBRef
import com.example.leaf.Utils.FollowList.FollowListActivity
import com.example.leaf.auth.MyHomeActivity
import com.example.leaf.auth.UserModel
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
    private val user = Firebase.auth.currentUser
    private val uid = user?.uid.toString()
    /*lateinit var auth: FirebaseAuth
    lateinit var uid: String*/
    lateinit var mydata : UserModel
    //프래그먼트와 레이아웃을 연결시켜주는 부분
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FrameLayout {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        //val profileName = binding.mainProfile
        val key = FBRef.profileRef.key.toString()
        val following = binding.homeFollowingCount!!.text.toString()
        val follower = binding.homeFollowerCount!!.text.toString()
       // val storageReference = Firebase.storage.reference.child(key + ".png")
        val imageViewFromFB = binding.profileImageview
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

       /* storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful)
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
        })*/

       // auth = FirebaseAuth.getInstance()
      //  uid = auth.currentUser?.uid.toString()
        var imguri = FBRef.userRef.child("$uid/displayName").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue<String>()
                Log.d(ContentValues.TAG, "Value is: $value")
                binding.mainProfile.setText(value)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        FBRef.userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {

                val userProfile = snapshot.getValue<UserModel>()
                println(userProfile)
                Glide.with(requireContext()).load(userProfile?.imUrl)
                    .apply(RequestOptions().circleCrop())
                    .into(imageViewFromFB!!)
            }

        })
        FBRef.userRef.child("$uid/description").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue<String>()
                Log.d(ContentValues.TAG, "Value is: $value")
                binding.mainIntroduce.setText(value)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        var query = FBRef.userRef.child(uid)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                binding.homeFollowerCount.setText(snapshot.getValue(UserModel::class.java)?.followerCount.toString())
                binding.homeFollowingCount.setText(snapshot.getValue(UserModel::class.java)?.followingCount.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        binding.homeFollowerCount.setOnClickListener{
            val intent = Intent(activity, FollowListActivity::class.java)
            intent.putExtra("Kind", "Follower")
            startActivity(intent)
        }


        binding.homeFollowingCount.setOnClickListener{
            val intent = Intent(activity, FollowListActivity::class.java)
            intent.putExtra("Kind", "Following")
            startActivity(intent)
        }

        return binding.root
    }
}


