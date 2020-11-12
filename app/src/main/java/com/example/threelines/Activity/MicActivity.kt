package com.example.threelines.Activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.threelines.Data.Result
import com.example.threelines.Network.RetrofitClient
import com.example.threelines.Network.RetrofitService
import com.example.threelines.R
import com.github.squti.androidwaverecorder.WaveRecorder
import kotlinx.android.synthetic.main.activity_mic.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
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

        var AUDIO_FILE_PATH : String = externalCacheDir?.absolutePath + "/"+ user_id + "." + Date().time.toString()+".wav"
        // https://github.com/squti/Android-Wave-Recorder
        val waveRecorder = WaveRecorder(filePath = AUDIO_FILE_PATH)

        button_start_recording.setOnClickListener {
            Log.d(TAG, AUDIO_FILE_PATH)
            if (ContextCompat.checkSelfPermission(this, // 오디오 녹음에 대한 권한
                    Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, // 저장소에 대한 권한
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                val permissions = arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                Log.d(TAG, "Permission Error")
                Toast.makeText(this, "권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(this, permissions, 0)
            } else {
                Log.d(TAG, "Record Start")
                Toast.makeText(this, "녹음 시작", Toast.LENGTH_SHORT).show()
                waveRecorder.startRecording()
            }
        }

        button_stop_recording.setOnClickListener{
            Log.d(TAG, "Record End")
            Toast.makeText(this, "녹음 끝", Toast.LENGTH_SHORT).show()
            waveRecorder.stopRecording()
        }
/*
        //Pause
        waveRecorder.pauseRecording()

        //Resume
        waveRecorder.resumeRecording()
*/
        button_send_audio.setOnClickListener{
            Log.d(TAG, "Send Audio")
            Toast.makeText(this, "오디오를 전송합니다.", Toast.LENGTH_SHORT).show()
            var file : File = File(AUDIO_FILE_PATH)
            var requestFile : RequestBody = RequestBody.create(MediaType.parse("audio/*"), file);
            var uploadFile : MultipartBody.Part = MultipartBody.Part.createFormData("file", AUDIO_FILE_PATH, requestFile)

            postMic(retrofitService, user_id!!, uploadFile)
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