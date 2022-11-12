package com.example.leaf.auth

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Gallery
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.leaf.databinding.ActivityProfileEditBinding
import com.example.leaf.R
import com.example.leaf.Utils.FBAuth
import com.example.leaf.Utils.FBRef
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.FirebaseStorageKtxRegistrar
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class ProfileEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileEditBinding
    private val fireStorage = FirebaseStorage.getInstance().reference
    private val fireDatabase = FirebaseDatabase.getInstance().reference
    private  var isImageUpload = false
    lateinit var profileview: ImageView
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_edit)
        initImageViewProfile()
        //이미지이름을 key값으로 저장
        val key = FBRef.profileRef.key.toString()
        val profileName = binding.editName
        profileName.setText(FBAuth.getDisplayName())
        binding.profileBtn.setOnClickListener {
            imageUpload(key)
            val introduce = binding.editIntroduce.text.toString() //자기소개
            //데이터 1개가 계속 수정되는 방식
            FBRef.profileRef
                .setValue(ProfileModel(introduce)) //파이어베이스에 저장
            FBAuth.setDisplayName(profileName.text.toString())
            val intent = Intent(this, MyHomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initView() {

        FBRef.profileRef.child("introduce").addValueEventListener(object : ValueEventListener {
            //onDataChange는 데이터가 변경될때마다 호출된다.
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue<String>()
                Log.d(TAG, "Value is: $value")
                binding.editIntroduce.setText(value)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    private fun initImageViewProfile() {

        binding.profileImageview.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)

            startActivityForResult(gallery,100)
        }
    }

    private fun imageUpload(key: String) {
        //이미지 이름을 key값으로 저장
        //val key = FBRef.profileRef.push().key.toString()
        val storage = Firebase.storage
        val storageRef = storage.reference //경로 설정
        val mountainsRef = storageRef.child(key + ".png")

        val imageView = binding.profileImageview
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
        val urlTask = uploadTask.continueWithTask{
            task ->
            if(!task.isSuccessful){
                task.exception?.let {
                    throw it
                }
            }
            mountainsRef.downloadUrl
        }.addOnCompleteListener {
            task ->
            if(task.isSuccessful){
                val downloadUri = task.result
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            imageUri = data?.data //원래 사진경로
            binding.profileImageview.setImageURI(imageUri) //바뀐경로로

        }


    }
}