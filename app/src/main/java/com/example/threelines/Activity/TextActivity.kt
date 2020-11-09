package com.example.threelines.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.threelines.Data.Text
import com.example.threelines.Network.RetrofitClient
import com.example.threelines.Network.RetrofitService
import com.example.threelines.R
import com.example.threelines.TextAdapter
import kotlinx.android.synthetic.main.activity_text.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class TextActivity : AppCompatActivity() {
    private var TAG = "TEXT"
    private lateinit var retrofit : Retrofit
    private lateinit var retrofitService: RetrofitService
    private var data : MutableList<Text>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text)

        var intent : Intent = getIntent();
        var user_id : String? = intent.getStringExtra("user_id")
        var record_id : Int? = intent.getIntExtra("record_id", 0)

        // Retrofit
        initRetrofit()

        // RecyclerView
        var adapter = TextAdapter()
        adapter.listData = getRecordList(retrofitService, user_id!!, record_id!!)!!
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getRetrofit()
        retrofitService = retrofit.create(RetrofitService::class.java)
    }

    private fun getRecordList(service : RetrofitService, user_id : String, record_id : Int) : MutableList<Text>?{
        var data : MutableList<Text>? = null
        service.getText(user_id, record_id).enqueue(object : Callback<List<Text>> {
            override fun onResponse(call: Call<List<Text>>, response: Response<List<Text>>) {
                Log.d(TAG, "Success")
                if(response.body()!!.size == 0){ // 데이터가 없을 시
                    Log.d(TAG, "No data")
                } else {
                    // 데이터 받고 로그로 확인하기
                    data = response.body()!!.toMutableList()
                }
            }
            override fun onFailure(call: Call<List<Text>>, t: Throwable) {
                Log.d(TAG, "Fail : {$t}")
            }
        })
        return data
    }
}
