package com.example.leaf.food

import android.content.Context
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


class foodAdapter(val item : ArrayList<foodModel>) : RecyclerView.Adapter<foodAdapter.Viewholder>() {
    private val foodKeyList = arrayListOf<String>()
    private val foodDataList = arrayListOf<foodModel>()

    private val TAG = FeedFragment::class.java.simpleName


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): foodAdapter.Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_list,parent,false)
        return Viewholder(view)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: foodAdapter.Viewholder, position: Int) {
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
    }


    fun onClick(context: Context, position: Int) {
        //val intent = Intent(context,PostActivity::class.java)
      //  intent.putExtra("key",beuatyKeyList[position])
      //  context.startActivity(intent)
    }


    fun getData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children){
                    val item = dataModel.getValue(foodModel::class.java)
                    foodDataList.add(item!!)
                    foodKeyList.add(dataModel.key.toString())
                }
                foodKeyList.reverse()
                foodDataList.reverse()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.foodRef.addValueEventListener(postListener)
    }
    
}

