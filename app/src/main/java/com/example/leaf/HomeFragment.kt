package com.example.leaf

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentContainer
import com.example.leaf.Utils.FBAuth
import com.example.leaf.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : FrameLayout{
        val binding = FragmentHomeBinding.inflate(inflater,container,false)
        auth = Firebase.auth
        val profileName = binding.mainProfile
        profileName.setText(FBAuth.getDisplayName())

        return binding.root


    }

}