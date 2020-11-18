package com.example.threelines.Adapter

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.threelines.Data.LoginPreferences
import com.example.threelines.Data.Result
import com.example.threelines.Data.Speaker
import com.example.threelines.Network.RetrofitClient
import com.example.threelines.Network.RetrofitService
import com.example.threelines.R
import kotlinx.android.synthetic.main.speaker_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class SpeakerAdapter(record_id: Int) : RecyclerView.Adapter<SpeakerHolder>(){
    var listData = mutableListOf<Speaker>()
    var record_id = record_id

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeakerHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.speaker_item, parent, false)
        return SpeakerHolder(view)
    }

    override fun onBindViewHolder(holder: SpeakerHolder, position: Int) {
        val data = listData.get(position)
        val record_id = record_id
        holder.setListData(data, record_id)
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}

class SpeakerHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    private lateinit var retrofit : Retrofit
    private lateinit var retrofitService: RetrofitService

    fun setListData(listdata: Speaker, record_id: Int){
        itemView.speaker_id.text = "${listdata.speaker_id}"
        itemView.speaker_name.setText(listdata.speaker_name)

        initRetrofit()

        itemView.btn_edit_speaker.setOnClickListener {
            val speaker_name = itemView.speaker_name.text.toString()
            edit_speaker_app(retrofitService, record_id, listdata.speaker_id, speaker_name)
        }
    }
    private fun initRetrofit() {
        retrofit = RetrofitClient.getRetrofit()
        retrofitService = retrofit.create(RetrofitService::class.java)
    }

    private fun edit_speaker_app(service: RetrofitService, record_id: Int, speaker_id: Int, speaker_name: String) {
        service.post_edit_speaker(record_id, speaker_id, speaker_name).enqueue(object : Callback<Result> {
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