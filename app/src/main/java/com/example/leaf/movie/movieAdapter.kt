package com.example.leaf.movie

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
import com.example.leaf.feed.FeedFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class movieAdapter(val item : ArrayList<movieModel>) : RecyclerView.Adapter<movieAdapter.Viewholder>() {
    private val movieKeyList = arrayListOf<String>()
    private val movieDataList = arrayListOf<movieModel>()

    private val TAG = FeedFragment::class.java.simpleName


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): movieAdapter.Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_list,parent,false)
        return Viewholder(view)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: movieAdapter.Viewholder, position: Int) {
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
        holder.title.text=item.get(position).title
        Log.d("check33", item.get(position).title)
        holder.writer.text=item.get(position).uid
        holder.date.text=item.get(position).date
        holder.online.text=item.get(position).oneline
        holder.star.text=item.get(position).star

        holder.itemView.setOnClickListener{
            onClick(context,position)
        }
    }



    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.re_title)
        val date = itemView.findViewById<TextView>(R.id.rv_date)
        val writer = itemView.findViewById<TextView>(R.id.rv_writer)
        val image = itemView.findViewById<ImageView>(R.id.rv_photo)
        val online = itemView.findViewById<TextView>(R.id.rv_review)
        val star = itemView.findViewById<TextView>(R.id.star)
    }


    fun onClick(context: Context, position: Int) {
        val intent = Intent(context,moviepostActivity::class.java)
        intent.putExtra("key",movieKeyList[position])
        context.startActivity(intent)
    }


    fun getData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children){
                    val item = dataModel.getValue(movieModel::class.java)
                    movieDataList.add(item!!)
                    movieKeyList.add(dataModel.key.toString())
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
