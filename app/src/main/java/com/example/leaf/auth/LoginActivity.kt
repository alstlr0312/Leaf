package com.example.leaf.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.leaf.MainActivity
import com.example.leaf.R
import com.example.leaf.Search.SearchActivity
import com.example.leaf.Utils.FBAuth
import com.example.leaf.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        FBAuth.auth

        binding.loginJoinBtn.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }
        binding.loginBtn.setOnClickListener {

            val email = binding.emailArea.text.toString()
            val password = binding.passwordArea.text.toString()
            if (email.isNullOrEmpty() && password.isNullOrEmpty()) {
                Toast.makeText(this, "아이디와 비밀번호를 반드시 입력하시오", Toast.LENGTH_SHORT).show()

            } else if (email.isNullOrEmpty()) {
                Toast.makeText(this, "사용자 아이디를 반드시 입력하시오", Toast.LENGTH_SHORT).show()
            } else if (password.isNullOrEmpty()) {
                Toast.makeText(this, "사용자 비밀번호를 반드시 입력하시오", Toast.LENGTH_SHORT).show()
            } else {
                FBAuth.auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP //회원가입하면 뒤에있는 엑티비티 없애기
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                        }

                    }
            }
        }
    }

}