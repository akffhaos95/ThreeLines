package com.example.threelines

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private var Login: Login? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* retrofit */
        var retrofit = Retrofit.Builder()
                .baseUrl("http://localhost:8000")
                .addConverterFactory(GsonConverterFactory.create())
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
            loginService.requestLogin(id, pw).enqueue(object: Callback<Login> {
                override fun onFailure(call: Call<Login>, t: Throwable) {
                    Log.e("LOGIN", t.message!!)
                    var dialog = AlertDialog.Builder(this@MainActivity)
                    dialog.setTitle("에러")
                    dialog.setMessage("호출 실패")
                    dialog.show()
                }

                override fun onResponse(call: Call<Login>, Login: Response<Login>) {
                    this@MainActivity.Login = Login.body()
                    Log.d("LOGIN", "msg : " + this@MainActivity.Login?.msg)
                    Log.d("LOGIN", "code : " + this@MainActivity.Login?.code)
                }
            })
        }
    }
}