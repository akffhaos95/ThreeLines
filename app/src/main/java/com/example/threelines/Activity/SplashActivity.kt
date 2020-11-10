package com.example.threelines.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.threelines.Network.RetrofitClient
import com.example.threelines.Network.RetrofitService
import com.example.threelines.R
import com.example.threelines.Data.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

/*
* SplashActivity
* 앱 실행시에 처음 시작
* 1. SharedPreferences에 로그인 정보가 있을 시에 로그인 시도
* 2. 로그인 정보가 없다 || 로그인 실패시 -> LoginActivity로 이동
* 3. 로그인 성공시 -> 인증값으로 MainActivity로 이동
*/
class SplashActivity : AppCompatActivity() {
    private var TAG = "SPLASH"
    private lateinit var retrofit: Retrofit
    private lateinit var retrofitService: RetrofitService

    var user_id: String? = ""
    var passwd: String? = ""
    var loginSuccess: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val pref = this.getPreferences(0)
        if (pref.contains("user_id") and pref.contains("passwd")) {
            /* 로그인 정보 존재 */
            user_id = pref.getString("user_id", "")
            passwd = pref.getString("passwd", "")

            // Retrofit
            initRetrofit()
            loginSuccess = postLogin(retrofitService, user_id!!, passwd!!)!!
        }
        if (loginSuccess){ // 로그인 성공, 메인 화면으로 이동
            val intent = Intent(this, RecordActivity::class.java)
            startActivity(intent);
        } else { // 로그인 실패, 로그인 화면으로 이동
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent);
            finish()
        }
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getRetrofit()
        retrofitService = retrofit.create(RetrofitService::class.java)
    }

    private fun postLogin(service : RetrofitService, user_id : String, passwd : String) : Boolean{
        var result : Boolean = false
        service.login(user_id, passwd).enqueue(object : Callback<Result>{
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                Log.d(TAG, "Success")
                if(response.body() == null){ // 데이터가 없을 시
                    Log.d(TAG, "Login Failed")
                } else {
                    Log.d(TAG, response.toString())
                    result = true
                }
            }
            override fun onFailure(call: Call<Result>, t: Throwable) {
                Log.d(TAG, "Fail : {$t}")
            }
        })
        return result
    }
}