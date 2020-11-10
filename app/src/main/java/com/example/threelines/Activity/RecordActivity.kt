package com.example.threelines.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.threelines.R
import com.example.threelines.Data.Record
import com.example.threelines.Network.RetrofitClient
import com.example.threelines.Network.RetrofitService
import com.example.threelines.RecordAdapter
import kotlinx.android.synthetic.main.activity_record.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
/*
* RecordActivity
* 녹음 리스트 화면
* retrofit으로 user_id에 해당하는 record list를
* recyclerView로 만든다
* RecyclerView -> record_item
* getIntent -> user_id
* setIntent -> user_id, record_id
* */

class RecordActivity : AppCompatActivity() {
    private var TAG = "RECORD"
    private lateinit var retrofit : Retrofit
    private lateinit var retrofitService: RetrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        // Retrofit
        initRetrofit()

        // Intent get
        var intent : Intent = getIntent();
        var user_id : String? = intent.getStringExtra("user_id")

        if (user_id != null) {
            getRecordList(retrofitService, user_id)
        }
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getRetrofit()
        retrofitService = retrofit.create(RetrofitService::class.java)
    }

    private fun getRecordList(service : RetrofitService, user_id : String) {
        var data : MutableList<Record>? = null
        service.getRecord(user_id).enqueue(object : Callback<List<Record>>{
            override fun onResponse(call: Call<List<Record>>, response: Response<List<Record>>) {
                Log.d(TAG, "Success")
                if(response.body() == null){ // 데이터가 없을 시
                    Log.d(TAG, "No data")
                } else {
                    Log.d(TAG, "test")
                    Log.d(TAG, response.body().toString())
                    data = response.body()!!.toMutableList()

                    // RecyclerView
                    var adapter = RecordAdapter()
                    adapter.listData = data!!

                    Log.d(TAG, "3")
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                }
            }
            override fun onFailure(call: Call<List<Record>>, t: Throwable) {
                Log.d(TAG, "Fail : {$t}")
            }
        })
    }
}