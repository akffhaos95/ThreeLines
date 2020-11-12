package com.example.threelines.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.threelines.Data.Result
import com.example.threelines.Network.RetrofitClient
import com.example.threelines.Network.RetrofitService
import com.example.threelines.R
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
/*
* RegisterActivity
* editText_id_r, editText_pw_r, editText_pw_ck -> EditText
* btn_register, btn_cancel -> Button
* 
* 회원가입 성공 -> LoginActivity
* 회원가입 실패 -> 그대로
* Retrofit -> postRegister
*/
class RegisterActivity : AppCompatActivity() {
    private var TAG = "REGISTER"
    private lateinit var retrofit : Retrofit
    private lateinit var retrofitService: RetrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Retrofit
        initRetrofit()

        // btn_register
        btn_register.setOnClickListener{
            var user_id = editText_id_r.text.toString()
            var passwd = editText_pw_r.text.toString()
            var passwd_ck = editText_pw_ck.text.toString()

            if(passwd == passwd_ck) {
                postRegister(retrofitService, user_id, passwd)
            } else {
                Toast.makeText(this, "비밀번호 체크", Toast.LENGTH_SHORT).show()
            }
        }

        btn_cancel.setOnClickListener{
            finish()
        }
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getRetrofit()
        retrofitService = retrofit.create(RetrofitService::class.java)
    }

    // 회원가입
    private fun postRegister(service : RetrofitService, user_id : String, passwd : String) {
        service.register(user_id, passwd).enqueue(object : Callback<Result>{
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                Log.d(TAG, "Access Success")
                if(response.body()!!.status.toString() == "0"){ // 회원가입 실패
                    Log.d(TAG, "Register Failed")
                    Toast.makeText(applicationContext, "이미 존재하는 아이디 입니다", Toast.LENGTH_SHORT).show()
                } else{ // 회원가입 성공
                    Log.d(TAG, "Register Success")
                    Toast.makeText(applicationContext, "회원가입 성공", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            override fun onFailure(call: Call<Result>, t: Throwable) {
                Log.d(TAG, "Access Fail : {$t}")
                Toast.makeText(applicationContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }
}