package com.example.leaf.auth

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.leaf.MainActivity
import com.example.leaf.R
import com.example.leaf.Utils.FBAuth
import com.example.leaf.Utils.FBRef
import com.example.leaf.databinding.ActivityJoinBinding
import com.example.leaf.setting.settingFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.jar.Manifest

class JoinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJoinBinding
    private lateinit var auth: FirebaseAuth
    private var imageUri : Uri? = null
    private val getContent =

        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if(result.resultCode == RESULT_OK) {
                val photo = findViewById<ImageView>(R.id.profile_imageview)
                imageUri = result.data?.data //이미지 경로 원본
                photo.setImageURI(imageUri) //이미지 뷰를 바꿈
                Log.d("이미지", "성공")

            }
            else{
                Log.d("이미지", "실패")
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)
        //이미지 등록

        var profileCheck = false

        binding.profileImageview.setOnClickListener{
            val intentImage = Intent(Intent.ACTION_PICK)
            intentImage.type = MediaStore.Images.Media.CONTENT_TYPE
            getContent.launch(intentImage)
            profileCheck = true
        }
        FBAuth.auth
        binding.joinAppBtn.setOnClickListener {
            if (validation()) {
                val email = binding.emailArea.text.toString()
                val password = binding.passwordArea1.text.toString()
                val nickname = binding.nicknameArea.text.toString()
                val photo = findViewById<ImageView>(R.id.profile_imageview)


                FBAuth.auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = Firebase.auth.currentUser
                            val uid = user?.uid
                            val userIdSt = uid.toString()
                            FirebaseStorage.getInstance()
                                .reference.child("$uid/photo").putFile(imageUri!!).addOnSuccessListener {
                                    var userProfile: Uri? = null
                                    FirebaseStorage.getInstance().reference.child("$uid/photo").downloadUrl
                                        .addOnSuccessListener {
                                            userProfile = it
                                            println("$userProfile")
                                            FBRef.userRef.child(uid!!).setValue(UserModel(uid,password,email,nickname,userProfile.toString(),""))
                                           // FBRef.userRef.child("$uid/imUrl").setValue(photoUri.toString())
                                            Toast.makeText(
                                                this,
                                                "프로필사진이 생성.",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                }
                            //
                            FBAuth.setDisplayName(nickname) // displayName의 값을 nickname으로 변경
                            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MyHomeActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP //회원가입하면 뒤에있는 엑티비티 없애기
                            startActivity(intent)

                        } else {
                            if(!profileCheck) {
                                Toast.makeText(this, "프로필 사진을 등록해주세요", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

            }
        }
    }

    private fun validation(): Boolean {
        if (binding.nicknameArea.text.isEmpty()) {
            Toast.makeText(this, "닉네임을 입력해주세여", Toast.LENGTH_SHORT).show()
            return false
        } else if (binding.emailArea.text.isEmpty()) {
            Toast.makeText(this, "이메일을 입력해주세여", Toast.LENGTH_SHORT).show()
            return false
        } else if(binding.passwordArea1.text.isEmpty()){
            Toast.makeText(this, "비밀번호1를 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        } else if(binding.passwordArea2.text.isEmpty()){
            Toast.makeText(this, "비밀번호2를 확인해주세요", Toast.LENGTH_SHORT).show()
            return false
        } else if(binding.passwordArea1.text.toString() != binding.passwordArea2.text.toString()){
            Toast.makeText(this, "비밀번호를 똑같이 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        } else if(binding.passwordArea1.text.length < 6){
            Toast.makeText(this, "비밀번호를 6자리 이상으로 입력해주세용", Toast.LENGTH_SHORT).show()
            return false
        }

        return true

    }
}