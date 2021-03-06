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
import com.example.threelines.Adapter.TextAdapter
import kotlinx.android.synthetic.main.activity_text.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
/*
* TextActivity
* retrofit으로 user_id, record_id에 해당하는 text list를
* recyclerView로 만든다
* RecyclerView -> text_item
* getIntent -> user_id, record_id
* setIntent -> record_id
 */
class TextActivity : AppCompatActivity() {
    private var TAG = "TEXT"
    private lateinit var retrofit : Retrofit
    private lateinit var retrofitService: RetrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text)

        // Retrofit
        initRetrofit()
    }

    override fun onResume() {
        super.onResume()
        // Intent get
        var intent : Intent = getIntent();
        var record_id : Int? = intent.getIntExtra("record_id", 0)
        Log.d(TAG, "record_id : $record_id")

        if (record_id != 0){
            getTextList(retrofitService, record_id!!)
        }

        btn_speaker_edit.setOnClickListener {
            val intent = Intent(applicationContext, SpeakerEditActivity::class.java)
            intent.putExtra("record_id", record_id)
            startActivity(intent)
        }
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getRetrofit()
        retrofitService = retrofit.create(RetrofitService::class.java)
    }

    // TextList 불러오기
    private fun getTextList(service : RetrofitService, record_id : Int){
        var data : MutableList<Text>?
        service.getText(record_id).enqueue(object : Callback<List<Text>> {
            override fun onResponse(call: Call<List<Text>>, response: Response<List<Text>>) {
                Log.d(TAG, "Access Success")
                if(response.body() == null){ // 데이터가 없을 시
                    Log.d(TAG, "No data")
                } else {
                    Log.d(TAG, "Data Access Success")
                    data = response.body()!!.toMutableList()

                    // RecyclerView
                    var adapter = TextAdapter()
                    adapter.listData = data!!
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                }
            }
            override fun onFailure(call: Call<List<Text>>, t: Throwable) {
                Log.d(TAG, "Fail : {$t}")
            }
        })
    }
}
