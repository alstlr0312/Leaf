package com.example.leaf.beauty

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.leaf.R
import com.example.leaf.Utils.FBRef
import com.example.leaf.auth.UserModel
import com.example.leaf.feed.FeedFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class beautyAdapter(val item : ArrayList<beautyModel>, var mydata : UserModel) : RecyclerView.Adapter<beautyAdapter.Viewholder>() {
    private val beautyKeyList = arrayListOf<String>()
    private val beautyDataList = arrayListOf<beautyModel>()
    private val TAG = FeedFragment::class.java.simpleName

    lateinit var auth: FirebaseAuth
    lateinit var uid: String
    lateinit var followingsData : UserModel


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): beautyAdapter.Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_list,parent,false)
        return Viewholder(view)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: beautyAdapter.Viewholder, position: Int) {
        getData()
        val context = holder.itemView.context
        val imView = item.get(position).imUrl
        val proView = item.get(position).proUrl

        CoroutineScope(Dispatchers.Main).launch {
            holder.apply {
                Glide.with(context)
                    .load(imView)
                    .into(holder.image)

            }
            holder.apply {
                Glide.with(context)
                    .load(proView)
                    .into(holder.profile)

            }
        }
        holder.title.text=item.get(position).title
        holder.date.text=item.get(position).date
        holder.writer.text=item.get(position).uname
        holder.online.text=item.get(position).oneline
        Log.d("writer",item.get(position).uname)
        holder.star.text=item.get(position).star
        holder.itemView.setOnClickListener{
            onClick(context,position)
        }

        var query = FBRef.userRef.child(item.get(position).uid)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                followingsData = snapshot.getValue(UserModel::class.java)!!
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
   if(item.get(position).favorite.contains(mydata.uid)) {//????????? ?????? ????????????
       holder.favorite.setImageResource(R.drawable.heart_full)
       holder.favorite.setOnClickListener {
           holder.favorite.setImageResource(R.drawable.heart)
           Log.d("clickg","click g")
           item.get(position).favoriteCount--
           FBRef.beautyRef.child(item.get(position).key).child("favorite")
               .child(mydata.uid).removeValue()
           FBRef.beautyRef.child(item.get(position).key).child("favoriteCount")
              .setValue(item.get(position).favoriteCount)
       }
   }else { //????????? ???????????? ??????
       holder.favorite.setImageResource(R.drawable.heart)
       holder.favorite.setOnClickListener {
           holder.favorite.setImageResource(R.drawable.heart_full)
           Log.d("clickfav","click fav")
           item.get(position).favorite.put(mydata.uid, true)
           item.get(position).favoriteCount++
           FBRef.beautyRef.child(item.get(position).key).child("favorite")
               .setValue( item.get(position).favorite)
           FBRef.beautyRef.child(item.get(position).key).child("favoriteCount")
               .setValue(item.get(position).favoriteCount)
          FBRef.beautyRef.child(item.get(position).key).child("favoriteCount")
              .setValue(item.get(position).favoriteCount)
       }
   }
        if (mydata.followings.contains(item.get(position).uid)) {
            holder.follow_btn.text = "UNFOLLOW"
            holder.follow_btn.setBackgroundColor(Color.LTGRAY)
            holder.follow_btn.setTextColor(Color.BLACK)
            holder.follow_btn.setOnClickListener {
                followingsData.followerCount--
                FBRef.userRef.child(item.get(position).uid).child("followers")
                    .child(mydata.uid).removeValue()
                FBRef.userRef.child(item.get(position).uid).child("followerCount")
                    .setValue(followingsData.followerCount)

                mydata.followingCount--
                FBRef.userRef.child(uid).child("followings")
                    .child(item.get(position).uid).removeValue()
                FBRef.userRef.child(uid).child("followingCount")
                    .setValue(mydata.followingCount)
            }
        }
        else {
            holder.follow_btn.text = "FOLLOW"
            holder.follow_btn.setBackgroundColor(Color.BLUE)
            holder.follow_btn.setTextColor(Color.WHITE)
            holder.follow_btn.setOnClickListener {
                followingsData.followers.put(uid, true)
                followingsData.followerCount++
                FBRef.userRef.child(item.get(position).uid).child("followers")
                    .setValue(followingsData.followers)
                FBRef.userRef.child(item.get(position).uid).child("followerCount")
                    .setValue(followingsData.followerCount)

                mydata.followings.put(item.get(position).uid, true)
                mydata.followingCount++
                FBRef.userRef.child(uid).child("followings")
                    .setValue(mydata.followings)
                FBRef.userRef.child(uid).child("followingCount")
                    .setValue(mydata.followingCount)
            }
        }
    }



    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.re_title)
        val date = itemView.findViewById<TextView>(R.id.rv_date)
        val writer = itemView.findViewById<TextView>(R.id.rv_writer)
        val image = itemView.findViewById<ImageView>(R.id.rv_photo)
        val online = itemView.findViewById<TextView>(R.id.rv_review)
        val star = itemView.findViewById<TextView>(R.id.star)
        var favorite = itemView.findViewById<ImageView>(R.id.item_Heart)
        val follow_btn = itemView.findViewById<Button>(R.id.rv_follow)
        val profile = itemView.findViewById<ImageView>(R.id.imageView5)
    }

    fun onClick(context: Context, position: Int) {
        val intent = Intent(context,beautypostAtivity::class.java)
        intent.putExtra("key",beautyKeyList[position])
        context.startActivity(intent)
    }


    fun getData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children){
                    val item = dataModel.getValue(beautyModel::class.java)
                    if(mydata.followings.contains(item!!.uid)) {
                        beautyDataList.add(item!!)
                        beautyKeyList.add(dataModel.key.toString())
                    }
                }
                beautyKeyList.reverse()
                beautyDataList.reverse()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.beautyRef.addValueEventListener(postListener)
    }
    
}

