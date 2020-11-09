package com.example.threelines.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.threelines.Data.Login
import com.example.threelines.Data.Record
import com.example.threelines.Network.LoginService
import com.example.threelines.Network.RetrofitClient
import com.example.threelines.Network.RetrofitService
import com.example.threelines.R
import com.example.threelines.TextAdapter
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_text.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException
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

        /* recordActivity */
        nextBtn.setOnClickListener {
            val nextIntent = Intent(this, MicActivity::class.java)
            startActivity(nextIntent)
        }

        // Retrofit
        initRetrofit()

        // loginBtn
        btn_login.setOnClickListener{
            var user_id = editText_id.text.toString()
            var passwd = editText_pw.text.toString()
            Log.d(TAG, "user_id: $user_id, passwd: $passwd");

            var result : Boolean = postLogin(retrofitService, user_id!!, passwd!!)!!
            Log.d(TAG, "LOGIN $result")
            if(result){
                val intent = Intent(this, RecordActivity::class.java)
                intent.putExtra("user_id", user_id)
                startActivity(intent)
            } else {
                Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getRetrofit()
        retrofitService = retrofit.create(RetrofitService::class.java)
    }

    private fun postLogin(service : RetrofitService, user_id : String, passwd : String) : Boolean{
        var result : Boolean = false
        service.login(user_id, passwd).enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d(TAG, "Success")
                if(response.body()!! == "Failed"){ // 데이터가 없을 시
                    Log.d(TAG, "Login Failed")
                } else {
                    result = true
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d(TAG, "Fail : {$t}")
            }
        })
        return result
    }
}