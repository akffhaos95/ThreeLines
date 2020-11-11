package com.example.threelines.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.threelines.Network.RetrofitClient
import com.example.threelines.Network.RetrofitService
import com.example.threelines.R
import com.example.threelines.Data.Result
import com.example.threelines.LoginPreferences
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
    companion object { lateinit var prefs : LoginPreferences }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        prefs = LoginPreferences(applicationContext)

        if (prefs.user_id != "") {
            /* 로그인 정보 존재 */
            user_id = prefs.user_id
            passwd = prefs.passwd
            Log.d("test", user_id +  passwd)

            // Retrofit
            initRetrofit()
            postLogin(retrofitService, user_id!!, passwd!!)!!
        } else {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
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
                if(response.body()!!.status.toString() == "0"){ // 데이터가 없을 시
                    Log.d(TAG, "Login Failed")
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                    finish()
                } else {
                    Log.d(TAG, "Login Success")
                    Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, RecordActivity::class.java)
                    intent.putExtra("user_id", user_id)
                    startActivity(intent)
                }
                finish()
            }
            override fun onFailure(call: Call<Result>, t: Throwable) {
                Log.d(TAG, "Fail : {$t}")
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }
}