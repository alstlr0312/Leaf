package com.example.leaf.beauty

class beautyModel (
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