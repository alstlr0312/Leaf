package com.example.leaf.movie

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
import com.example.leaf.databinding.ActivityMoviewriteBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class MoviewriteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMoviewriteBinding

    private val TAG = MoviewriteActivity::class.java.simpleName

    private var isImageUpload = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_moviewrite)

        binding.pingping.setOnClickListener {
            val title = binding.writeTitle.text.toString()
            val ukey = FBAuth.getUid()
            // val eid = FBAuth.getDisplayName()
            val oneline = binding.writeContents.text.toString()
            val board = binding.writeEdit.text.toString()
            val time = FBAuth.getTime()
            val star = binding.movieratingBar.rating.toString()
            Log.d(TAG,title)

            //파이어 베이스 storge에 이미지를 저장
            //게시글을 클릭했을떄, 게시글에 대한 정보 전달
            //이미지 이름을 key값으로 저장
            val key = FBRef.movieRef.push().key.toString()

            //board
            //  -key
            //      -boardModel(title, content, uid, time)

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
                        FBRef.movieRef
                            .child(key)
                            .setValue(movieModel(title,ukey,oneline,board,time,imuri,star))
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



    private fun imageUpload(key : String){
        // Get the data from an ImageView as bytes
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