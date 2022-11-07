package com.example.leaf.Search

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.leaf.R
import com.example.leaf.Utils.FBRef
import com.example.leaf.auth.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchAdapter (val item : ArrayList<UserModel>) : RecyclerView.Adapter<SearchAdapter.Viewholder>() {
    private val userKeyList = arrayListOf<String>()
    private val userDataList = arrayListOf<UserModel>()
    private val TAG = SearchActivity::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item_list,parent,false)
        return Viewholder(view)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: SearchAdapter.Viewholder, position: Int) {
        getData()
        val context = holder.itemView.context
        //val imView = item.get(position).이미지
        //Log.d("checkim",item.get(position).이미지)
        Log.d("checkuid",item.get(position).uid)
        Log.d("checkemail",item.get(position).email)
        Log.d("checkdisplayName",item.get(position).displayName)
        Log.d("checkdescription",item.get(position).description)


        /*CoroutineScope(Dispatchers.Main).launch {
            holder.apply {
                Glide.with(context)
                    .load(imView)
                    .into(holder.image)
            }
        }*/
        holder.nickname.text=item.get(position).displayName
        holder.email.text=item.get(position).email
        holder.description.text = item.get(position).description
        holder.followBtn.setOnClickListener{
            println("Follow btn Click!")
        }
    }



    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nickname = itemView.findViewById<TextView>(R.id.user_search_nickname)
        val email = itemView.findViewById<TextView>(R.id.user_search_id)
        val description = itemView.findViewById<TextView>(R.id.user_search_description)
        val image = itemView.findViewById<ImageView>(R.id.user_search_image)
        val followBtn = itemView.findViewById<Button>(R.id.user_search_follow_btn)
    }



    fun getData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children){
                    val item = dataModel.getValue(UserModel::class.java)
                    userDataList.add(item!!)
                    userKeyList.add(dataModel.key.toString())
                }
                userKeyList.reverse()
                userDataList.reverse()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.userRef.addValueEventListener(postListener)
    }

}