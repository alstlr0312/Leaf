package com.example.leaf.beauty

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
import com.example.leaf.databinding.ActivityBeautywriteBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class BeautywriteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBeautywriteBinding

    private val TAG = BeautywriteActivity::class.java.simpleName

    private var isImageUpload = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_beautywrite)

        binding.pingping.setOnClickListener {
            val title = binding.writeTitle.text.toString()
            val username = FBAuth.getDisplayName()
            val oneline = binding.writeContents.text.toString()
            val board = binding.writeEdit.text.toString()
            val time = FBAuth.getTime()
            val star = binding.beautyratingBar.rating.toString()
            val uid = FBAuth.getUid()
            Log.d(TAG,title)
            val key = FBRef.beautyRef.push().key.toString()
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
                        FBRef.beautyRef
                            .child(key)
                            .setValue(beautyModel(title,username,oneline,board,time,imuri,star,uid))
                        Log.d("check", downloadUri.toString())
                    }
                }

            }
            /*FBRef.boardRef
                .child(key)
                .setValue(BoardModel(title,eid,ukey,dogname,breed,lostday,content,time))*/

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