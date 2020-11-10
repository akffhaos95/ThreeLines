package com.example.threelines

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.threelines.Activity.LoginActivity
import com.example.threelines.Activity.RecordActivity
import com.example.threelines.Data.Record
import com.example.threelines.Data.Result
import com.example.threelines.Network.RetrofitClient
import com.example.threelines.Network.RetrofitService
import kotlinx.android.synthetic.main.activity_record.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class RegisterActivity : AppCompatActivity() {
    private var TAG = "REGISTER"
    private lateinit var retrofit : Retrofit
    private lateinit var retrofitService: RetrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Retrofit
        initRetrofit()


    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getRetrofit()
        retrofitService = retrofit.create(RetrofitService::class.java)
    }

    private fun postRegister(service : RetrofitService, user_id : String, passwd : String) {
        service.register(user_id, passwd).enqueue(object : Callback<Result>{
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                Log.d(TAG, "Access Success")
                if(response.body()!!.status.toString() == "0"){ // 로그인 실패
                    Log.d(TAG, "Register Failed")
                } else { // 로그인 성공
                    Log.d(TAG, "Register Success")

                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
            override fun onFailure(call: Call<Result>, t: Throwable) {
                Log.d(TAG, "Access Fail : {$t}")
                Toast.makeText(applicationContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }
}