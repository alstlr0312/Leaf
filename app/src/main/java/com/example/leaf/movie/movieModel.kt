package com.example.leaf.movie

class movieModel  (
    val title : String = "",
    val uid : String ="",
    val oneline : String = "",
    val board : String = "",
    val date : String = "",
    val imUrl : String = "",
    val star : String = "",
    val favorite :MutableMap<String,Boolean> = HashMap()
)