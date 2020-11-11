package com.example.threelines.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.threelines.Data.Record
import com.example.threelines.Network.RetrofitClient
import com.example.threelines.Network.RetrofitService
import com.example.threelines.R
import com.example.threelines.Adapter.RecordAdapter
import kotlinx.android.synthetic.main.activity_record.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
/*
* RecordActivity(user_id)
* RecyclerView -> record_item
* item 클릭 -> TextActivity(record_id)
* Retrofit -> getRecordList
*/
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
        Log.d(TAG, "user_id : " + user_id)

        if (user_id != null) {
            getRecordList(retrofitService, user_id)
        }

        refresh_layout.setOnRefreshListener {
            getRecordList(retrofitService, user_id!!)
            refresh_layout.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getRetrofit()
        retrofitService = retrofit.create(RetrofitService::class.java)
    }

    // RecordList 불러오기
    private fun getRecordList(service: RetrofitService, user_id: String) {
        var data : MutableList<Record>?
        service.getRecord(user_id).enqueue(object : Callback<List<Record>> {
            override fun onResponse(call: Call<List<Record>>, response: Response<List<Record>>) {
                Log.d(TAG, "Access Success")
                if (response.body() == null) { // 데이터가 없을 시
                    Log.d(TAG, "No data")
                } else {
                    Log.d(TAG, "Get Data Success")
                    data = response.body()!!.toMutableList()

                    // RecyclerView
                    var adapter = RecordAdapter()
                    adapter.listData = data!!
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