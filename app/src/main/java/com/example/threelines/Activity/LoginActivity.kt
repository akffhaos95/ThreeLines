package com.example.threelines.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.threelines.Data.Login
import com.example.threelines.Network.LoginService
import com.example.threelines.R
import kotlinx.android.synthetic.main.activity_login.*
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

    private var Login: Login? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /* retrofit */
        var retrofit = Retrofit.Builder()
                .baseUrl("https://192.168.1.8:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUnsafeOkHttpClient()?.build())
                .build()

        var loginService: LoginService = retrofit.create(LoginService::class.java)

        /* recordActivity */
        nextBtn.setOnClickListener {
            val nextIntent = Intent(this, RecordActivity::class.java)
            startActivity(nextIntent)
        }

        /* loginBtn */
        btn_login.setOnClickListener{
            var id = editText_id.text.toString()
            var pw = editText_pw.text.toString()

            Log.d("LOGIN", "id : $id pw : $pw");
            loginService.requestLogin(id, pw).enqueue(object : Callback<Login> {
                override fun onFailure(call: Call<Login>, t: Throwable) {
                    Log.e("LOGIN", t.message!!)
                    var dialog = AlertDialog.Builder(this@LoginActivity)
                    dialog.setTitle("에러")
                    dialog.setMessage("호출 실패")
                    dialog.show()
                }

                override fun onResponse(call: Call<Login>, Login: Response<Login>) {
                    this@LoginActivity.Login = Login.body()
                    Log.d("LOGIN", "msg : " + this@LoginActivity.Login?.msg)
                    Log.d("LOGIN", "code : " + this@LoginActivity.Login?.code)
                }
            })
        }
    }

    // 안전하지 않음으로 HTTPS를 통과합니다.
    fun getUnsafeOkHttpClient(): OkHttpClient.Builder? {
        return try {
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate?>? {
                        return arrayOf()
                    }
                }
            )
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            val sslSocketFactory = sslContext.socketFactory
            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { hostname, session -> true }
            builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}