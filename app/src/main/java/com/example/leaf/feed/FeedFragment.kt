package com.example.leaf.feed

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.example.leaf.Utils.FBRef
import com.example.leaf.beauty.beautyAdapter
import com.example.leaf.beauty.beautyModel
import com.example.leaf.databinding.FragmentFeedBinding
import com.example.leaf.food.foodAdapter
import com.example.leaf.food.foodModel
import com.example.leaf.house.houseAdapter
import com.example.leaf.house.houseModel
import com.example.leaf.movie.movieAdapter
import com.example.leaf.movie.movieModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class FeedFragment : Fragment() {
    lateinit var  movieAdapter : movieAdapter
    lateinit var  foodAdapter : foodAdapter
    lateinit var  houseAdapter : houseAdapter
    lateinit var  beautyAdapter : beautyAdapter
    private val TAG = FeedFragment::class.java.simpleName
    private val movieDataList = arrayListOf<movieModel>()
    private val movieKeyList = arrayListOf<String>()

    private val beautyDataList = arrayListOf<beautyModel>()
    private val beautyKeyList = arrayListOf<String>()

    private val foodDataList = arrayListOf<foodModel>()
    private val foodKeyList = arrayListOf<String>()

    private val houseDataList = arrayListOf<houseModel>()
    private val houseKeyList = arrayListOf<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): FrameLayout {
       val binding = FragmentFeedBinding.inflate(inflater,container,false)

        binding.moBtn.setOnClickListener {
            binding.rvPostList.apply {
                getmovieData()
                layoutManager = GridLayoutManager(requireContext(),1)
                setHasFixedSize(true)
                movieAdapter =  movieAdapter(movieDataList)
                binding.rvPostList.adapter =  movieAdapter
            }

        }

        binding.foBtn.setOnClickListener {
            binding.rvPostList.apply {
                getfoodData()
                layoutManager = GridLayoutManager(requireContext(),1)
                setHasFixedSize(true)
                foodAdapter= foodAdapter(foodDataList)
                binding.rvPostList.adapter = foodAdapter
            }

        }

        binding.beBtn.setOnClickListener {
            binding.rvPostList.apply {
                getbeautyData()
                layoutManager = GridLayoutManager(requireContext(),1)
                setHasFixedSize(true)
                beautyAdapter = beautyAdapter(beautyDataList)
                binding.rvPostList.adapter = beautyAdapter
            }

        }

        binding.hoBtn.setOnClickListener {
            binding.rvPostList.apply {
                gethouseData()
                layoutManager = GridLayoutManager(requireContext(),1)
                setHasFixedSize(true)
                houseAdapter = houseAdapter(houseDataList)
                binding.rvPostList.adapter = houseAdapter
            }

        }


        return binding.root

    }


    private fun getmovieData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                movieDataList.clear()
                for(dataModel in dataSnapshot.children){

                    Log.d(TAG,dataModel.toString())

                    val item = dataModel.getValue(movieModel::class.java)
                    movieDataList.add(item!!)
                    movieKeyList.add(dataModel.key.toString())
                }
                movieKeyList.reverse()
                movieDataList.reverse()
                movieAdapter.notifyDataSetChanged()
                Log.d(TAG,movieDataList.toString())
            }
            override fun onCancelled(databaseError: DatabaseError) {

                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.movieRef.addValueEventListener(postListener)
    }

    private fun getfoodData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                foodDataList.clear()
                for(dataModel in dataSnapshot.children){

                    Log.d(TAG,dataModel.toString())

                    val item = dataModel.getValue(foodModel::class.java)
                    foodDataList.add(item!!)
                    foodKeyList.add(dataModel.key.toString())
                }
                foodKeyList.reverse()
                foodDataList.reverse()
               foodAdapter.notifyDataSetChanged()
                Log.d(TAG,foodDataList.toString())
            }
            override fun onCancelled(databaseError: DatabaseError) {

                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.foodRef.addValueEventListener(postListener)
    }

    private fun getbeautyData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                beautyDataList.clear()
                for(dataModel in dataSnapshot.children){

                    Log.d(TAG,dataModel.toString())

                    val item = dataModel.getValue(beautyModel::class.java)
                    beautyDataList.add(item!!)
                    beautyKeyList.add(dataModel.key.toString())
                }
                beautyKeyList.reverse()
                beautyDataList.reverse()
                beautyAdapter.notifyDataSetChanged()
                Log.d(TAG,beautyDataList.toString())
            }
            override fun onCancelled(databaseError: DatabaseError) {

                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.beautyRef.addValueEventListener(postListener)
    }

    private fun gethouseData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                houseDataList.clear()
                for(dataModel in dataSnapshot.children){

                    Log.d(TAG,dataModel.toString())

                    val item = dataModel.getValue(houseModel::class.java)
                    houseDataList.add(item!!)
                    houseKeyList.add(dataModel.key.toString())
                }
                houseKeyList.reverse()
                houseDataList.reverse()
                houseAdapter.notifyDataSetChanged()
                Log.d(TAG,houseDataList.toString())
            }
            override fun onCancelled(databaseError: DatabaseError) {

                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.houseRef.addValueEventListener(postListener)
    }
}