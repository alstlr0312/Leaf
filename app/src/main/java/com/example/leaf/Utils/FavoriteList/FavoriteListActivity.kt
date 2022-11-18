package com.example.leaf.Utils.FavoriteList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.leaf.R
import com.example.leaf.Utils.Search.SearchAdapter
import com.example.leaf.auth.UserModel
import com.example.leaf.beauty.beautyAdapter
import com.google.firebase.auth.FirebaseAuth

class FavoriteListActivity : AppCompatActivity() {
    lateinit var  beautyAdapter: beautyAdapter
    private val userDataList = arrayListOf<UserModel>()
    private val userKeyList = arrayListOf<String>()
    private lateinit var myData : UserModel
    lateinit var auth: FirebaseAuth
    lateinit var uid : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_list)

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        val favoriteListView = findViewById<RecyclerView>(R.id.favoriteListView)
        getBoardFavoriteData()
    }

    //게시글의 좋아요 수 체크
    private fun getBoardFavoriteData() {

    }
}