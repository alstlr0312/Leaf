package com.example.leaf.Utils.MyPostList

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.leaf.R
import com.example.leaf.Utils.FBAuth
import com.example.leaf.Utils.FBRef
import com.example.leaf.auth.UserModel
import com.example.leaf.beauty.beautyAdapter
import com.example.leaf.beauty.beautyModel
import com.example.leaf.food.foodModel
import com.example.leaf.house.houseModel
import com.example.leaf.movie.movieModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MyPostListActivity() : AppCompatActivity() {
    lateinit var  foodAdapter : MyPostListFoodAdapter
    private val foodDataList = arrayListOf<foodModel>()
    private val foodKeyList = arrayListOf<String>()

    lateinit var  movieAdapter : MyPostMovieAdapter
    private val movieDataList = arrayListOf<movieModel>()
    private val movieKeyList = arrayListOf<String>()

    lateinit var  houseAdapter : MyPostHouseAdapter
    private val houseKeyList = arrayListOf<String>()
    private val houseDataList = arrayListOf<houseModel>()

    lateinit var  beautyAdapter : MyPostBeautyAdapter
    private val beautyKeyList = arrayListOf<String>()
    private val beautyDataList = arrayListOf<beautyModel>()

    private val TAG = MyPostListActivity::class.java.simpleName
    private lateinit var myData : UserModel
    lateinit var auth: FirebaseAuth
    lateinit var kind : String
    lateinit var uid: String

    lateinit var empty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_post_list)
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

        kind = intent.getStringExtra("Kind").toString()

        val myPostListView = findViewById<RecyclerView>(R.id.MyPostListView)
        empty = findViewById(R.id.my_Post_Empty)
        getMyData()

        myPostListView.apply{
            if(kind == "Food") {
                getfoodData()
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                foodAdapter = MyPostListFoodAdapter(foodDataList)
                Log.d(TAG, foodDataList.toString())
                myPostListView.adapter = foodAdapter
                if(foodDataList.size == 0)
                {
                    empty.text = "작성한 글이 없습니다"
                }
            }
            else if(kind == "Beauty") {
                getBeautyData()
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                beautyAdapter = MyPostBeautyAdapter(beautyDataList)
                myPostListView.adapter = beautyAdapter
                if(beautyDataList.size == 0)
                {
                    empty.text = "작성한 글이 없습니다"
                }
            }
            else if(kind == "Movie") {
                getMovieData()
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                movieAdapter = MyPostMovieAdapter(movieDataList)
                myPostListView.adapter = movieAdapter
                if(movieDataList.size == 0)
                {
                    empty.text = "작성한 글이 없습니다"
                }
            }
            else if(kind == "House") {
                getHouseData()
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                houseAdapter = MyPostHouseAdapter(houseDataList)
                myPostListView.adapter = houseAdapter
                if(houseDataList.size == 0)
                {
                    empty.text = "작성한 글이 없습니다"
                }
            }
        }
    }
    private fun getfoodData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                foodDataList.clear()
                foodKeyList.clear()
                for(dataModel in dataSnapshot.children){

                    Log.d(TAG,dataModel.toString())

                    val item = dataModel.getValue(foodModel::class.java)
                    if(uid == item!!.uid) {
                        foodDataList.add(item)
                        foodKeyList.add(dataModel.key.toString())
                        empty.text = ""
                    }
                }
                foodKeyList.reverse()
                foodDataList.reverse()
                foodAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {

                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.foodRef.addValueEventListener(postListener)
    }
    private fun getBeautyData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                beautyDataList.clear()
                beautyKeyList.clear()
                for(dataModel in dataSnapshot.children){

                    Log.d(TAG,dataModel.toString())

                    val item = dataModel.getValue(beautyModel::class.java)
                    if(uid == item!!.uid) {
                        beautyDataList.add(item)
                        beautyKeyList.add(dataModel.key.toString())
                        empty.text = ""
                    }
                }
                beautyKeyList.reverse()
                beautyDataList.reverse()
                beautyAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {

                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.beautyRef.addValueEventListener(postListener)
    }
    private fun getMovieData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                movieDataList.clear()
                movieKeyList.clear()
                for(dataModel in dataSnapshot.children){

                    Log.d(TAG,dataModel.toString())

                    val item = dataModel.getValue(movieModel::class.java)
                    if(uid == item!!.uid) {
                        movieDataList.add(item)
                        movieKeyList.add(dataModel.key.toString())
                        empty.text = ""
                    }
                }
                movieDataList.reverse()
                movieKeyList.reverse()
                movieAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {

                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.movieRef.addValueEventListener(postListener)
    }
    private fun getHouseData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                houseDataList.clear()
                houseKeyList.clear()
                for(dataModel in dataSnapshot.children){

                    Log.d(TAG,dataModel.toString())

                    val item = dataModel.getValue(houseModel::class.java)
                    if(uid == item!!.uid) {
                        houseDataList.add(item)
                        houseKeyList.add(dataModel.key.toString())
                        empty.text = ""
                    }
                }
                houseKeyList.reverse()
                houseDataList.reverse()
                houseAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {

                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.houseRef.addValueEventListener(postListener)
    }

    private fun getMyData(){
        val userListner = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children){
                    val item = dataModel.getValue(UserModel::class.java)
                    if(item?.uid == uid){
                        myData = item
                        Log.d(TAG,myData.toString())
                    }
                }
                //Log.d(TAG,userDataList.toString())
            }
            override fun onCancelled(databaseError: DatabaseError) {

                Log.w(TAG, "Feed:onCancelled", databaseError.toException())
            }
        }
        FBRef.userRef.addValueEventListener(userListner)
    }
}