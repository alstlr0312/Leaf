package com.example.leaf.house

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.leaf.R
import com.example.leaf.Utils.FBAuth
import com.example.leaf.Utils.FBRef
import com.example.leaf.auth.MyHomeActivity
import com.example.leaf.databinding.ActivityHousewriteBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class housewriteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHousewriteBinding

    private val TAG = housewriteActivity::class.java.simpleName

    private var isImageUpload = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_housewrite)

        binding.pingping.setOnClickListener {
            val title = binding.writeTitle.text.toString()
            val ukey = FBAuth.getUid()
            val oneline = binding.writeContents.text.toString()
            val board = binding.writeEdit.text.toString()
            val time = FBAuth.getTime()
            val star = binding.houseratingBar.rating.toString()
            val key = FBRef.houseRef.push().key.toString()
            val Uname = FBAuth.getDisplayName()
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
                uploadTask.addOnFailureListener {
                    // Handle unsuccessful uploads
                }.addOnSuccessListener { taskSnapshot ->
                    // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                    // ...
                }

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
                        FBRef.houseRef
                            .child(key)
                            .setValue(houseModel(title,ukey,oneline,board,time,imuri,star,key,Uname))
                        Log.d("check", downloadUri.toString())
                    }
                }

            }
            Log.d("clickgdfsfsfd","click g")
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100){
            binding.writeCamera.setImageURI(data?.data)
        }
    }
}