package com.example.leaf.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.leaf.MainActivity
import com.example.leaf.R
import com.example.leaf.Utils.FBAuth
import com.example.leaf.Utils.FBRef
import com.example.leaf.databinding.ActivityJoinBinding

class JoinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)

        FBAuth.auth
        binding.joinAppBtn.setOnClickListener {
            if (validation()) {
                val email = binding.emailArea.text.toString()
                val password = binding.passwordArea1.text.toString()
                val nickname = binding.nicknameArea.text.toString()

                FBAuth.auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val uid = task.result.user?.uid
                            FBRef.userRef.child(uid!!).setValue(UserModel(uid,password,email,nickname,""))
                            FBAuth.setDisplayName(nickname) // displayName의 값을 nickname으로 변경
                            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP //회원가입하면 뒤에있는 엑티비티 없애기
                            startActivity(intent)

                        } else {
                            Toast.makeText(this, "no", Toast.LENGTH_SHORT).show()
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