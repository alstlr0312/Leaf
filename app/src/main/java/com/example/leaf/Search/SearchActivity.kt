package com.example.leaf.Search

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.leaf.R
import com.example.leaf.Utils.FBRef
import com.example.leaf.auth.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class SearchActivity : AppCompatActivity() {
    lateinit var  searchAdapter : SearchAdapter
    private val userDataList = arrayListOf<UserModel>()
    private val userKeyList = arrayListOf<String>()
    private val TAG = SearchActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        var search_text = findViewById<SearchView>(R.id.Search_user_text)
        var search_user = findViewById<RecyclerView>(R.id.Search_user_list)
        search_text.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search_user.apply {
                    getData(query)
                    layoutManager = LinearLayoutManager(context)
                    setHasFixedSize(true)
                    searchAdapter = SearchAdapter(userDataList)
                    search_user.adapter = searchAdapter
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }
    private fun getData(userId : String?){
        val userListner = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userDataList.clear()
                for(dataModel in dataSnapshot.children){

                    Log.d(TAG,dataModel.toString())

                    val item = dataModel.getValue(UserModel::class.java)
                    if(item?.email == userId){
                    userDataList.add(item!!)
                    userKeyList.add(dataModel.key.toString())
                    }
                }
                userKeyList.reverse()
                userDataList.reverse()
                searchAdapter.notifyDataSetChanged()
                Log.d(TAG,userDataList.toString())
            }
            override fun onCancelled(databaseError: DatabaseError) {

                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.userRef.addValueEventListener(userListner)
    }
}