package com.example.leaf.Utils
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

object FBRef {

    private val database = Firebase.database

    val movieRef = database.getReference("movie")

    val houseRef = database.getReference("house")

    val foodRef = database.getReference("food")

    val beautyRef = database.getReference("beauty")

    val profileRef = database.getReference("profile")
    val userRef = database.getReference("User")


}