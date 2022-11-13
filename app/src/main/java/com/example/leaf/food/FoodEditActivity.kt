package com.example.leaf.food

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.leaf.R
import com.example.leaf.Utils.FBAuth
import com.example.leaf.Utils.FBRef
import com.example.leaf.auth.MyHomeActivity
import com.example.leaf.databinding.ActivityFoodEditBinding
import com.example.leaf.databinding.ActivityMovieEditBinding
import com.example.leaf.movie.movieModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.lang.Exception

class FoodEditActivity : AppCompatActivity() {
    private lateinit var key: String
    private lateinit var binding: ActivityFoodEditBinding
    private var isImageUpload = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_food_edit)

        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImageData(key)
        binding.pingping.setOnClickListener {
            val title = binding.writeTitle.text.toString()
            val username = FBAuth.getDisplayName()
            val oneline = binding.writeContents.text.toString()
            val board = binding.writeEdit.text.toString()
            val time = FBAuth.getTime()
            val star = binding.beautyratingBar.rating.toString()
            val uid = FBAuth.getUid()
            if(isImageUpload) {

                val storage = Firebase.storage
                val storageRef = storage.reference
                val mountainsRef = storageRef.child(key+".png")

                val imageView = binding.writeCamera
                imageView.isDrawingCacheEnabled = true
                imageView.buildDrawingCache()
                val bitmap = (imageView.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()

                var uploadTask = mountainsRef.putBytes(data)
                uploadTask.addOnFailureListener {}.addOnSuccessListener { taskSnapshot -> }
                val urlTask = uploadTask.continueWithTask { task->
                    if (!task.isSuccessful){
                        task.exception?.let{
                            throw it
                        }
                    }
                    mountainsRef.downloadUrl
                }.addOnCompleteListener{ task->
                    if(task.isSuccessful){
                        val downloadUri = task.result
                        val imuri = downloadUri.toString()
                        FBRef.foodRef
                            .child(key)
                            .setValue(foodModel(title,username,oneline,board,time,imuri,star,uid))
                        Log.d("check", downloadUri.toString())
                    }
                }
            }
            finish()
            val intent = Intent(this, MyHomeActivity::class.java)
            startActivity(intent)
        }

        binding.writeCamera.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
            isImageUpload = true
        }
    }

    private fun getBoardData(key: String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try {
                    val dataModel = dataSnapshot.getValue(foodModel::class.java)
                    Log.d(ContentValues.TAG, dataSnapshot.toString())

                    binding.writeTitle.setText(dataModel?.title)
                    binding.writeContents.setText(dataModel?.oneline)
                    binding.writeEdit.setText(dataModel?.board)

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

    private fun getImageData(key: String){
        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child("$key.png")

        // ImageView in your Activity
        val imageViewFromFB = binding.writeCamera

        storageReference.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            } else {

            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100){
            binding.writeCamera.setImageURI(data?.data)
        }
    }

}


