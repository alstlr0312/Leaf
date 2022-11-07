package com.example.leaf.auth
data class UserModel (
    val uid : String = "",
    val password : String = "",
    val email : String = "",
    val displayName : String = "",
    val description : String = ""
)