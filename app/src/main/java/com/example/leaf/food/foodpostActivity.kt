package com.example.leaf.food

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.leaf.R
import com.example.leaf.Utils.FBAuth
import com.example.leaf.Utils.FBRef
import com.example.leaf.databinding.ActivityFoodpostBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class foodpostActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFoodpostBinding
    private lateinit var key: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_foodpost)
        key = intent.getStringExtra("key").toString()

        binding.boardSettingIcon.setOnClickListener {
            showDialog()
        }

        getBoardData(key)
        getImageData(key)
    }


    private fun showDialog(){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog,null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정/삭제")
        val alertDialog = mBuilder.show()
       /* alertDialog.findViewById<Button>(R.id.editbtn)?.setOnClickListener{
            val intent = Intent(this,BoardEditActivity::class.java)
            intent.putExtra("key",key)
            startActivity(intent)
        }*/
        alertDialog.findViewById<Button>(R.id.deletebtn)?.setOnClickListener{
            FBRef.foodRef.child(key).removeValue()
            finish()
        }
    }

    private fun getImageData(key: String) {
        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(key + ".png")

        // ImageView in your Activity
        val imageViewFromFB = binding.ivPostProfile
        CoroutineScope(Dispatchers.Main).launch {
            storageReference.downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Glide.with(this@foodpostActivity)
                        .load(task.result)
                        .into(imageViewFromFB)
                } else {
                }
            }
        }
    }

    private fun getBoardData(key: String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try {
                    val dataModel = dataSnapshot.getValue(foodModel::class.java)
                    Log.d(ContentValues.TAG, dataSnapshot.toString())
                    binding.postTitle.text = dataModel?.title
                    binding.postName.text = dataModel?.uid
                    binding.postDate.text = dataModel?.date
                    binding.starrate.text = dataModel?.star
                    binding.postText1.text = dataModel?.oneline
                    binding.postText.text = dataModel?.board
                    binding.starrate.text = dataModel?.star
                    val mykey = FBAuth.getUid()
                    val writerUid = dataModel?.uid
                    if(mykey.equals(writerUid)){
                        binding.boardSettingIcon.isVisible = true
                    }else{

                    }

                }catch (e: Exception){
                    Log.w(ContentValues.TAG, "삭제완료")
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {

                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.foodRef.child(key).addValueEventListener(postListener)
    }

}