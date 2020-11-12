package com.example.threelines.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.example.threelines.Network.RetrofitClient
import com.example.threelines.Network.RetrofitService
import com.example.threelines.R
import com.example.threelines.Data.Result
import com.example.threelines.Data.LoginPreferences
import kotlinx.android.synthetic.main.activity_splash.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*
/*
* SplashActivity
* LoginPreferences에 로그인 정보가 있다 -> postLogin
* LoginPreferences에 로그인 정보가 없다 -> LoginActivity
*
* 로그인 성공 -> RecordActivity(user_id)
* 로그인 실패 -> LoginActivity
*/
class SplashActivity : AppCompatActivity() {
    private var TAG = "SPLASH"
    private var SPLASH_DELAY : Long = 1000

    private lateinit var retrofit: Retrofit
    private lateinit var retrofitService: RetrofitService
    companion object { lateinit var prefs : LoginPreferences }

    var user_id: String? = ""
    var passwd: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        prefs = LoginPreferences(applicationContext)

        // 2초 로딩
        Handler().postDelayed(({
            // 로그인 정보 존재
            if (prefs.user_id != "") {
                text_info.text = "로그인 정보가 존재합니다..."
                Log.d(TAG, "Have LoginPreferences")
                user_id = prefs.user_id
                passwd = prefs.passwd

                // Retrofit
                initRetrofit()
                postLogin(retrofitService, user_id!!, passwd!!)
            } else {
                text_info.text = "로그인 정보가 없습니다."
                Log.d(TAG, "Not Have LoginPreferences")
                // LoginActivity로 이동
                gotoLogin()
            }
        }), SPLASH_DELAY)

    }

    // LoginActivity로 이동
    private fun gotoLogin() {
        text_info.text = "로그인 화면으로 이동합니다..."
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    // RecordActivity로 이동
    private fun gotoRecord(user_id: String) {
        text_info.text = "녹음 리스트로 이동합니다..."
        val intent = Intent(applicationContext, RecordActivity::class.java)
        intent.putExtra("user_id", user_id)
        startActivity(intent)
        finish()
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getRetrofit()
        retrofitService = retrofit.create(RetrofitService::class.java)
    }

    // 로그인
    private fun postLogin(service : RetrofitService, user_id : String, passwd : String) {
        service.login(user_id, passwd).enqueue(object : Callback<Result>{
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                Log.d(TAG, "Access Success")
                if(response.body()!!.status.toString() == "0"){ // 로그인 실패
                    Log.d(TAG, "Login Failed")
                    gotoLogin()
                } else {
                    Log.d(TAG, "Login Success") // 로그인 성공
                    gotoRecord(user_id)
                }
            }
            // 접속 불가
            override fun onFailure(call: Call<Result>, t: Throwable) {
                Log.d(TAG, "Fail : {$t}")
                gotoLogin()
            }
        })
    }
}