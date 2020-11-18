package com.example.threelines.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.threelines.Adapter.SpeakerAdapter
import com.example.threelines.Data.Speaker
import com.example.threelines.Network.RetrofitClient
import com.example.threelines.Network.RetrofitService
import com.example.threelines.R
import kotlinx.android.synthetic.main.activity_text.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class SpeakerEditActivity : AppCompatActivity() {
    private var TAG = "SPEAKER_EDIT"
    private lateinit var retrofit : Retrofit
    private lateinit var retrofitService: RetrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speaker_edit)

        // Retrofit
        initRetrofit()

        // Intent get
        var intent : Intent = getIntent();
        var record_id : Int? = intent.getIntExtra("record_id", 0)
        Log.d(TAG, "record_id : $record_id")

        if (record_id != 0){
            getSpeakerList(retrofitService, record_id!!)
        }
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getRetrofit()
        retrofitService = retrofit.create(RetrofitService::class.java)
    }

    // TextList 불러오기
    private fun getSpeakerList(service : RetrofitService, record_id : Int){
        var data : MutableList<Speaker>?
        service.get_edit_speaker(record_id).enqueue(object : Callback<List<Speaker>> {
            override fun onResponse(call: Call<List<Speaker>>, response: Response<List<Speaker>>) {
                Log.d(TAG, "Access Success")
                if(response.body() == null){ // 데이터가 없을 시
                    Log.d(TAG, "No data")
                } else {
                    Log.d(TAG, "Data Access Success")
                    data = response.body()!!.toMutableList()

                    // RecyclerView
                    var adapter = SpeakerAdapter(record_id)
                    adapter.listData = data!!
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                }
            }
            override fun onFailure(call: Call<List<Speaker>>, t: Throwable) {
                Log.d(TAG, "Fail : {$t}")
            }
        })
    }
}