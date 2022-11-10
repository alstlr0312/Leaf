package com.example.leaf.auth
data class UserModel (
    val uid : String = "",
    val password : String = "",
    val email : String = "",
    val displayName : String = "",
    val description : String = "",
    var followerCount : Int = 0,
    //중복을 방지하기 위해서
    var followers : MutableMap<String,Boolean> = HashMap(),
    //팔로잉 카운터
    var followingCount : Int = 0,
    //중복 방지
    var followings : MutableMap<String,Boolean> = HashMap()
)