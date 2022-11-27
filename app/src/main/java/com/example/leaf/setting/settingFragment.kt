package com.example.leaf.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.leaf.R
import com.example.leaf.Utils.FBAuth
import com.example.leaf.Utils.FBRef
import com.example.leaf.auth.UserModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class settingFragment : Fragment() {

    private val user = Firebase.auth.currentUser
    private val uid = user?.uid.toString()
    companion object {
        private var imageUri : Uri? = null
        private val fireStorage = FirebaseStorage.getInstance().reference
        private val fireDatabase = FirebaseDatabase.getInstance().reference
                fun newInstance() : settingFragment {
                    return settingFragment()
        }

    }
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        }

     //프레그먼트를 포함하고 있는 액티비티에 붙었을 때
     override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            if(imageUri == null) {
            imageUri = result.data?.data //이미지 경로 원본
            val photo = view?.findViewById<ImageView>(R.id.profile_imageview)
            photo?.setImageURI(imageUri) //이미지 뷰를 바꿈
            //기존 사진을 삭제 후 새로운 사진을 등록
                fireStorage.child("$uid/photo").delete().addOnSuccessListener {
                    fireStorage.child("$uid/photo").putFile(imageUri!!).addOnSuccessListener {
                        fireStorage.child("$uid/photo").downloadUrl.addOnSuccessListener {
                            val photoUri: Uri = it
                            println("$photoUri")
                            FBRef.userRef.child("$uid/imUrl").setValue(photoUri.toString())
                            Toast.makeText(requireContext(), "프로필사진이 변경되었습니다.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
                Log.d("이미지", "성공")
            }else {

                }
        }

    }

    //뷰가 생성
    //프래그먼트와 레이아웃을 연결시켜주는 부분
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        val name = view?.findViewById<EditText>(R.id.edit_name)
        val introduce = view?.findViewById<EditText>(R.id.edit_introduce)
        val photo = view?.findViewById<ImageView>(R.id.profile_imageview)
        val button = view?.findViewById<Button>(R.id.profile_btn)
        val email = view?.findViewById<TextView>(R.id.email)

        email?.text = FBAuth.getEmail() //이메일 가져옴
        name?.setText(FBAuth.getDisplayName())
        //프로필 구현
        FBRef.userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val userProfile = snapshot.getValue<UserModel>()
                println(userProfile)
                Glide.with(requireContext()).load(userProfile?.imUrl)
                    .apply(RequestOptions().circleCrop())
                    .into(photo!!)
            }

        })
        //프로필 사진 바꾸기

        photo?.setOnClickListener {
            val intentImage = Intent(Intent.ACTION_PICK)
            intentImage.type = MediaStore.Images.Media.CONTENT_TYPE
            getContent.launch(intentImage)
        }
        button?.setOnClickListener{
            //자기소개 변경
            if(introduce?.text!!.isNotEmpty()){
                FBRef.userRef.child("$uid/description").setValue(introduce.text.toString())
                introduce.clearFocus()
            }
            //이름 변경
            if(name?.text!!.isNotEmpty()) {
                FBRef.userRef.child("$uid/displayName").setValue(name.text.toString())
                name.clearFocus()
                Toast.makeText(requireContext(), "이름이 변경되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }


        return view
    }

}
