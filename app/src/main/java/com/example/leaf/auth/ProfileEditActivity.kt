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
import androidx.databinding.DataBindingUtil
import com.example.leaf.databinding.ActivityProfileEditBinding
import com.example.leaf.R
import com.example.leaf.Utils.FBAuth
import com.example.leaf.Utils.FBRef
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class ProfileEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileEditBinding
    lateinit var profileview: ImageView
    private var imageUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_edit)
        initImageViewProfile()

        val key = FBRef.profileRef.push().key.toString()
        val profileName = binding.editName
        profileName.setText(FBAuth.getDisplayName())
        imageUpload(key)
            binding.profileBtn.setOnClickListener {
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

        FBRef.profileRef.addValueEventListener(object : ValueEventListener {
            //onDataChange는 데이터가 변경될때마다 호출된다.
            override fun onDataChange(dataSnapshot: DataSnapshot){
                val value = dataSnapshot.getValue<String>()
                Log.d(TAG, "Value is: $value")
                binding.editIntroduce.setText(value)
            }
            override fun onCancelled(error: DatabaseError){

            }
        })

    }
    private fun initImageViewProfile() {

        binding.profileImageview.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)

            startActivityForResult(gallery,100)
         }
    }

    private fun getData() {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val FBRef: DatabaseReference = database.getReference("introduce")

        FBRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot?.value
                binding.editIntroduce.text.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                println("Failed to read value.")

            }
        })
    }

        private fun imageUpload(key : String) {
            //이미지 이름을 key값으로 저장
            //val key = FBRef.profileRef.push().key.toString()
            val storage = Firebase.storage
            val storageRef = storage.reference //경로 설정
            val mountainsRef = storageRef.child(key +"png")

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


        }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100){
            binding.profileImageview.setImageURI(data?.data)
        }
    }


}