package com.example.leaf.Utils.FavoriteList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.leaf.R
import com.example.leaf.Utils.Search.SearchAdapter
import com.example.leaf.auth.UserModel
import com.example.leaf.beauty.beautyModel
import com.google.firebase.auth.FirebaseAuth

class FavoriteAdapter (val item : ArrayList<UserModel>, var beautyfavorite : beautyModel) : RecyclerView.Adapter<FavoriteAdapter.Viewholder>() {
    private val userKeyList = arrayListOf<String>()
    private val userDataList = arrayListOf<UserModel>()
    lateinit var auth: FirebaseAuth
    lateinit var uid: String
    lateinit var mydata : UserModel
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdapter.Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item_list,parent,false)
        return FavoriteAdapter.Viewholder(view)
    }
    override fun getItemCount(): Int {
        return item.size
    }
    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
    }
    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nickname = itemView.findViewById<TextView>(R.id.user_search_nickname)
        val email = itemView.findViewById<TextView>(R.id.user_search_id)
        val description = itemView.findViewById<TextView>(R.id.user_search_description)
        val image = itemView.findViewById<ImageView>(R.id.user_search_image)
        val followBtn = itemView.findViewById<Button>(R.id.user_search_follow_btn)
    }


}
