package com.example.leaf.Utils.Search

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.leaf.R
import com.example.leaf.Utils.FBRef
import com.example.leaf.auth.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class SearchAdapter (val item : ArrayList<UserModel>) : RecyclerView.Adapter<SearchAdapter.Viewholder>() {
    private val userKeyList = arrayListOf<String>()
    private val userDataList = arrayListOf<UserModel>()
    lateinit var auth: FirebaseAuth
    lateinit var uid: String
    lateinit var mydata : UserModel
    private val TAG = SearchActivity::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item_list,parent,false)
        return Viewholder(view)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        var query = FBRef.userRef.child(uid)
        query.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mydata = snapshot.getValue(UserModel::class.java)!!
                Log.d("mycheckuid",mydata.uid)
                Log.d("mycheckemail",mydata.email)
                Log.d("mycheckdisplayName",mydata.displayName)
                Log.d("mycheckdescription",mydata.description)
                Log.d("mycheckfollower",mydata.followers.toString())
                Log.d("mycheckfollowing",mydata.followings.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        //getData()
        val context = holder.itemView.context
        //val imView = item.get(position).이미지
        //Log.d("checkim",item.get(position).이미지)
        Log.d("checkuid",item.get(position).uid)
        Log.d("checkemail",item.get(position).email)
        Log.d("checkdisplayName",item.get(position).displayName)
        Log.d("checkdescription",item.get(position).description)
        Log.d("checkfollower",item.get(position).followers.toString())
        Log.d("checkfollowing",item.get(position).followings.toString())


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
        if(item.get(position).followers.contains(uid)){
            holder.followBtn.text = "UNFOLLOW"
            holder.followBtn.setBackgroundColor(Color.LTGRAY)
            holder.followBtn.setTextColor(Color.BLACK)
            holder.followBtn.setOnClickListener{
                item.get(position).followerCount--
                FBRef.userRef.child(item.get(position).uid).child("followers")
                    .child(mydata.uid).removeValue()
                FBRef.userRef.child(item.get(position).uid).child("followerCount")
                    .setValue(item.get(position).followerCount)

                mydata.followingCount--
                FBRef.userRef.child(uid).child("followings")
                    .child(item.get(position).uid).removeValue()
                FBRef.userRef.child(uid).child("followingCount")
                    .setValue(mydata.followingCount)

            }
        }
        else{
            holder.followBtn.text = "FOLLOW"
            holder.followBtn.setBackgroundColor(Color.BLUE)
            holder.followBtn.setTextColor(Color.WHITE)
            holder.followBtn.setOnClickListener{
                item.get(position).followers.put(uid, true)
                item.get(position).followerCount++
                FBRef.userRef.child(item.get(position).uid).child("followers")
                    .setValue(item.get(position).followers)
                FBRef.userRef.child(item.get(position).uid).child("followerCount")
                    .setValue(item.get(position).followerCount)

                mydata.followings.put(item.get(position).uid,true)
                mydata.followingCount++
                FBRef.userRef.child(uid).child("followings")
                    .setValue(mydata.followings)
                FBRef.userRef.child(uid).child("followingCount")
                    .setValue(mydata.followingCount)

            }
        }
    }



    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nickname = itemView.findViewById<TextView>(R.id.user_search_nickname)
        val email = itemView.findViewById<TextView>(R.id.user_search_id)
        val description = itemView.findViewById<TextView>(R.id.user_search_description)
        val image = itemView.findViewById<ImageView>(R.id.user_search_image)
        val followBtn = itemView.findViewById<Button>(R.id.user_search_follow_btn)
    }

}