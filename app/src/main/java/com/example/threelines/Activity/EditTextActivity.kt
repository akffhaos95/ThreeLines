package com.example.threelines.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.threelines.Data.Result
import com.example.threelines.Network.RetrofitClient
import com.example.threelines.Network.RetrofitService
import com.example.threelines.R
import kotlinx.android.synthetic.main.activity_edit_text.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class EditTextActivity : AppCompatActivity() {
    private var TAG = "EDIT_TEXT"
    private lateinit var retrofit: Retrofit
    private lateinit var retrofitService: RetrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_text)

        // Intent get
        var intent: Intent? = intent
        var idx: String? = intent!!.getStringExtra("idx")
        var content: String? = intent.getStringExtra("content")

        Log.d(TAG, "idx : $idx, content : $content")

        // retrofit
        initRetrofit()

        text_bf_edit.text = content
        text_af_edit.setText(content)

        btn_edit_text.setOnClickListener {
            edit_text_app(retrofitService, idx!!.toInt(), text_af_edit.text.toString())
            finish()
        }
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getRetrofit()
        retrofitService = retrofit.create(RetrofitService::class.java)
    }

    private fun edit_text_app(service: RetrofitService, idx: Int, content: String) {
        service.edit_text(idx, content).enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                Log.d(TAG, "Success")
                Log.d(TAG, response.body().toString())
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                Log.d(TAG, "Fail : {$t}")
            }
        })
    }
}