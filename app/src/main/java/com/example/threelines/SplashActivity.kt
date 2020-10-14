package com.example.threelines

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
* SplashActivity
* 앱 실행시에 처음 시작
* 1. SharedPreferences에 로그인 정보가 있을 시에 로그인 시도
* 2. 로그인 정보가 없다 || 로그인 실패시 -> LoginActivity로 이동
* 3. 로그인 성공시 -> 인증값으로 MainFragment로 이동
*/
class SplashActivity : AppCompatActivity() {
    private var Login: Login? = null
    var id: String? = ""
    var pw: String? = ""
    var loginSuccess: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val pref = this.getPreferences(0)
        if (pref.contains("id") and pref.contains("pw")) {
            /* 로그인 정보 존재 */
            id = pref.getString("id", "")
            pw = pref.getString("pw", "")

            /* 로그인 시도, retrofit */
            var retrofit = Retrofit.Builder()
                .baseUrl("http://localhost:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            var loginService: LoginService = retrofit.create(LoginService::class.java)

            loginService.requestLogin(id, pw).enqueue(object: Callback<Login> {
                override fun onFailure(call: Call<Login>, t: Throwable) {
                    Log.e("LOGIN", t.message!!)
                }

                override fun onResponse(call: Call<Login>, Login: Response<Login>) {
                    this@SplashActivity.Login = Login.body()
                    Log.d("LOGIN", "msg : " + this@SplashActivity.Login?.msg)
                    Log.d("LOGIN", "code : " + this@SplashActivity.Login?.code)
                    if (this@SplashActivity.Login?.code.equals("0000")){
                        // 로그인 성공
                        loginSuccess = true
                    }
                }
            })
        }
        if (loginSuccess){ // 로그인 성공
            //메인으로 이동
            //val intent = Intent(this, LoginActivity::class.java)
            //startActivity(intent);
        } else { // 로그인 실패
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent);
            finish()
        }
    }
}