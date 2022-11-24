package com.example.leaf.Utils.MyPostList

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.leaf.R
import com.example.leaf.Utils.FBRef
import com.example.leaf.auth.UserModel
import com.example.leaf.beauty.beautyModel
import com.example.leaf.food.foodpostActivity
import com.example.leaf.movie.movieModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyPostMovieAdapter (val item : ArrayList<movieModel>) : RecyclerView.Adapter<MyPostMovieAdapter.Viewholder>() {
    private val movieKeyList = arrayListOf<String>()
    private val movieDataList = arrayListOf<movieModel>()

    private val TAG = MyPostListFoodAdapter::class.java.simpleName

    lateinit var auth: FirebaseAuth
    lateinit var uid: String
    lateinit var mydata : UserModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_rv_list,parent,false)
        return Viewholder(view)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        var query = FBRef.userRef.child(uid)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mydata = snapshot.getValue(UserModel::class.java)!!
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        getData()
        val context = holder.itemView.context
        val imView = item.get(position).imUrl
        CoroutineScope(Dispatchers.Main).launch {
            holder.apply {
                Glide.with(context)
                    .load(imView)
                    .into(holder.image)
            }


        }
        holder.title.text = item.get(position).title
        holder.date.text = item.get(position).date
        holder.oneline.text = item.get(position).oneline
        holder.star.text = item.get(position).star

        holder.itemView.setOnClickListener {
            onClick(context, position)
        }
    }



    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.my_rv_title)
        val date = itemView.findViewById<TextView>(R.id.my_rv_date)
        val image = itemView.findViewById<ImageView>(R.id.my_rv_photo)
        val oneline = itemView.findViewById<TextView>(R.id.my_rv_review)
        val star = itemView.findViewById<TextView>(R.id.my_rv_star)
    }


    fun onClick(context: Context, position: Int) {
        val intent = Intent(context, foodpostActivity::class.java)
        intent.putExtra("key",movieKeyList[position])
        context.startActivity(intent)
    }


    fun getData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children){
                    val item = dataModel.getValue(movieModel::class.java)
                    if(uid==item!!.uid) {
                        movieDataList.add(item!!)
                        movieKeyList.add(dataModel.key.toString())
                    }
                }
                movieKeyList.reverse()
                movieDataList.reverse()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.movieRef.addValueEventListener(postListener)
    }

}

