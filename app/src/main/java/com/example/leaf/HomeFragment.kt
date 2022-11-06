package com.example.leaf

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.FragmentContainer
import com.example.leaf.Utils.FBAuth
import com.example.leaf.Utils.FBRef
import com.example.leaf.auth.ProfileModel
import com.example.leaf.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {
    companion object {
        //경로
        private val fireDatabase = FirebaseDatabase.getInstance().getReference("profile")
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }


    private lateinit var auth: FirebaseAuth

    //프래그먼트와 레이아웃을 연결시켜주는 부분
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        val profileName = binding.mainProfile
        val profileintroduce = binding.mainIntroduce.text.toString()

        auth = Firebase.auth
        profileName.setText(FBAuth.getDisplayName())
        initView()

        return binding.root
    }
}
    private fun initView() {

    FBRef.profileRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot){
            val value = dataSnapshot.getValue<String>()

        }
        override fun onCancelled(error: DatabaseError){

        }
    })
}


