package com.example.leaf.food

class foodModel  (
    val title : String = "",
    val uid : String ="",
    val oneline : String = "",
    val board : String = "",
    val date : String = "",
    val imUrl : String = "",
    val star : String = "",
    val key : String ="",
    val prouri : String = "",
    var favorite :MutableMap<String,Boolean> = HashMap(),
    var favoriteCount : Int = 0
)