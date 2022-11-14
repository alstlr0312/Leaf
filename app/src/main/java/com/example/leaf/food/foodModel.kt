package com.example.leaf.food

import com.example.leaf.Utils.FBAuth

class foodModel  (
    val title : String = "",
    val uid : String ="",
    val oneline : String = "",
    val board : String = "",
    val date : String = "",
    val imUrl : String = "",
    val star : String = "",
    val key : String ="",
    val favorite :MutableMap<String,Boolean> = HashMap()
)