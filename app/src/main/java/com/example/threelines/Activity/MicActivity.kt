package com.example.threelines.Activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.threelines.Data.Result
import com.example.threelines.Network.RetrofitClient
import com.example.threelines.Network.RetrofitService
import com.example.threelines.R
import com.github.squti.androidwaverecorder.WaveRecorder
import kotlinx.android.synthetic.main.activity_mic.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*

/*
* MicActivity
* 회의 녹음 -> 서버로 전송
* 회의 녹음 : MediaRecorder
*   녹음 [시작, 중지, 재개, 정지]
*   파일 경로는 ~~~로
* 서버 전송 : Retrofit
* */
class MicActivity : AppCompatActivity() {
    private var TAG = "MIC"
    private lateinit var retrofit: Retrofit
    private lateinit var retrofitService: RetrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mic)


        // Intent get
        var intent: Intent = intent;
        var user_id: String? = intent.getStringExtra("user_id")
        Log.d(TAG, "user_id : $user_id")

        // retrofit
        initRetrofit()

        var AUDIO_FILE_PATH : String = externalCacheDir?.absolutePath + "/"+ Date().time.toString()+".wav"
        val waveRecorder = WaveRecorder(filePath = AUDIO_FILE_PATH)

        button_start_recording.setOnClickListener{
            Log.d(TAG, AUDIO_FILE_PATH)
            Toast.makeText(this, "녹음시작", Toast.LENGTH_SHORT).show()
            waveRecorder.startRecording()
        }

        button_stop_recording.setOnClickListener{
            Toast.makeText(this, "녹음끝", Toast.LENGTH_SHORT).show()
            waveRecorder.stopRecording()
        }
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getRetrofit()
        retrofitService = retrofit.create(RetrofitService::class.java)
    }

    private fun postMic(service: RetrofitService, user_id: String, file: MultipartBody.Part): Boolean {
        var result: Boolean = false
        service.mic(user_id, file).enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                Log.d(TAG, "Success")
                Log.d(TAG, response.body().toString())
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                Log.d(TAG, "Fail : {$t}")
            }
        })
        return result
    }
}