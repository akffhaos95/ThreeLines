package com.example.threelines.Activity

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

class RecordActivity : AppCompatActivity() {
    private var TAG = "RECORD"
    private lateinit var retrofit : Retrofit
    private lateinit var retrofitService: RetrofitService
    private var data : MutableList<Record>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        // Retrofit
        initRetrofit()

        // RecyclerView
        var adapter = RecordAdapter()
        adapter.listData = getRecordList(retrofitService, "")!!
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getRetrofit()
        retrofitService = retrofit.create(RetrofitService::class.java)
    }

    private fun getRecordList(service : RetrofitService, user_id : String) : MutableList<Record>?{
        var data : MutableList<Record>? = null
        service.getRecord(user_id).enqueue(object : Callback<List<Record>>{
            override fun onResponse(call: Call<List<Record>>, response: Response<List<Record>>) {
                Log.d(TAG, "Success")
                if(response.body()!!.size == 0){ // 데이터가 없을 시
                    Log.d(TAG, "No data")
                } else {
                    // 데이터 받고 로그로 확인하기
                    data = response.body()!!.toMutableList()
                }
            }
            override fun onFailure(call: Call<List<Record>>, t: Throwable) {
                Log.d(TAG, "Fail : {$t}")
            }
        })
        return data
    }
}