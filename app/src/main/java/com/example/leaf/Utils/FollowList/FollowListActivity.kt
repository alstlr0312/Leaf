package com.example.leaf.Utils.FollowList

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.leaf.R
import com.example.leaf.Utils.FBAuth
import com.example.leaf.Utils.FBRef
import com.example.leaf.Utils.Search.SearchActivity
import com.example.leaf.Utils.Search.SearchAdapter
import com.example.leaf.auth.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class FollowListActivity : AppCompatActivity() {
    lateinit var  searchAdapter : SearchAdapter
    private val userDataList = arrayListOf<UserModel>()
    private val userKeyList = arrayListOf<String>()
    private val TAG = FollowListActivity::class.java.simpleName
    private lateinit var myData : UserModel
    lateinit var auth: FirebaseAuth
    lateinit var uid : String
    lateinit var kind : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow_list)
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

        kind = intent.getStringExtra("Kind").toString()

        val followListView = findViewById<RecyclerView>(R.id.followListView)
        getMyData()
        followListView.apply {
            if (kind == "Follower") {
                getFollowerData()
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                searchAdapter = SearchAdapter(userDataList)
                followListView.adapter = searchAdapter
            }
            else if (kind == "Following") {
                getFollowingData()
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                searchAdapter = SearchAdapter(userDataList)
                followListView.adapter = searchAdapter
            }
        }

    }

    private fun getMyData(){
        val userListner = object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children){

                    //Log.d(TAG,dataModel.toString())

                    val item = dataModel.getValue(UserModel::class.java)
                    if(item?.uid == uid){
                        myData = item!!
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

    private fun getFollowerData(){
        val userListner = object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userDataList.clear()
                for(dataModel in dataSnapshot.children){

                    //Log.d(TAG,dataModel.toString())

                    val item = dataModel.getValue(UserModel::class.java)
                    if(myData.followers.contains(item!!.uid)){
                        userDataList.add(item!!)
                        userKeyList.add(dataModel.key.toString())
                    }
                }
                userKeyList.reverse()
                userDataList.reverse()
                searchAdapter.notifyDataSetChanged()
                //Log.d(TAG,userDataList.toString())
            }
            override fun onCancelled(databaseError: DatabaseError) {

                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.userRef.addValueEventListener(userListner)
    }
    private fun getFollowingData(){
        val userListner = object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userDataList.clear()
                for(dataModel in dataSnapshot.children){

                    //Log.d(TAG,dataModel.toString())

                    val item = dataModel.getValue(UserModel::class.java)
                    if(myData.followings.contains(item!!.uid)){
                        userDataList.add(item!!)
                        userKeyList.add(dataModel.key.toString())
                    }
                }
                userKeyList.reverse()
                userDataList.reverse()
                searchAdapter.notifyDataSetChanged()
                //Log.d(TAG,userDataList.toString())
            }
            override fun onCancelled(databaseError: DatabaseError) {

                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.userRef.addValueEventListener(userListner)
    }
}