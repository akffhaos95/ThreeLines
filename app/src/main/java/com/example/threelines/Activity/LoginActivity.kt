package com.example.threelines.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.threelines.Network.RetrofitClient
import com.example.threelines.Network.RetrofitService
import com.example.threelines.R
import com.example.threelines.Data.Result
import com.example.threelines.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

/*
* LoginActivity
* 로그인 화면
* editText_id, editText_pw -> EditText
* loginBtn -> Button
* saveBtn -> CheckBox
* 로그인 정보 저장시에 ID, PW를 SharedPreference에 저장
* 로그인 : Retrofit
* */
class LoginActivity : AppCompatActivity() {
    private var TAG = "LOGIN"
    private lateinit var retrofit : Retrofit
    private lateinit var retrofitService: RetrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Retrofit
        initRetrofit()

        // btn_login
        btn_login.setOnClickListener{
            var user_id = editText_id.text.toString()
            var passwd = editText_pw.text.toString()
            Log.d(TAG, "user_id: $user_id, passwd: $passwd");

            postLogin(retrofitService, user_id!!, passwd!!)!!
        }

        // btn_register
        btn_register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getRetrofit()
        retrofitService = retrofit.create(RetrofitService::class.java)
    }

    private fun postLogin(service : RetrofitService, user_id : String, passwd : String) {
        service.login(user_id, passwd).enqueue(object : Callback<Result>{
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                Log.d(TAG, "Access Success")
                if(response.body()!!.status.toString() == "0"){ // 로그인 실패
                    Log.d(TAG, "Login Failed")
                } else { // 로그인 성공
                    Log.d(TAG, "Login Success")

                    val intent = Intent(applicationContext, RecordActivity::class.java)
                    intent.putExtra("user_id", user_id)
                    startActivity(intent)
                }
            }
            override fun onFailure(call: Call<Result>, t: Throwable) {
                Log.d(TAG, "Access Fail : {$t}")
                Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }
}