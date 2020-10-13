package com.example.threelines

import android.Manifest
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_record.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


class RecordActivity : AppCompatActivity() {

    var resp: resp? = null

    private var mediaRecorder: MediaRecorder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)


        var retrofit = Retrofit.Builder()
            .baseUrl("http://localhost:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var uploadFileService: UploadFileService = retrofit.create(UploadFileService::class.java)



        var state = false
        var recordingStopped = false

        val output = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}" + "/test.mp3"

        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        ActivityCompat.requestPermissions(this, permissions, 0)

        button_start_recording.setOnClickListener {
            mediaRecorder = MediaRecorder()

            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mediaRecorder?.setOutputFile(output)

            mediaRecorder?.prepare()
            mediaRecorder?.start()

            state = true

            Toast.makeText(this, "녹음 시작", Toast.LENGTH_SHORT).show()
        }

        button_stop_recording.setOnClickListener{
            if(state){
                mediaRecorder?.stop()
                mediaRecorder?.release()
                state = false
                Toast.makeText(this, "녹음 중지", Toast.LENGTH_SHORT).show()
            }
        }

        button_pause_recording.setOnClickListener {
            if (state) {
                if (!recordingStopped) {
                    Toast.makeText(this, "녹음 정지", Toast.LENGTH_SHORT).show()
                    mediaRecorder?.pause()
                    recordingStopped = true
                    button_pause_recording.text = "다시 시작"
                } else {
                    Toast.makeText(this, "다시 시작", Toast.LENGTH_SHORT).show()
                    mediaRecorder?.resume()
                    recordingStopped = false
                    button_pause_recording.text = "정지"
                }
            }
        }

        button_send_audio.setOnClickListener{
            Toast.makeText(this, "파일 전송", Toast.LENGTH_SHORT).show()
            var file = File(output);
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val body = MultipartBody.Part.createFormData("audio", file.name, requestFile)

            uploadFileService.uploadFile(body).enqueue(object : Callback<resp> {
                override fun onFailure(call: Call<resp>, t: Throwable) {
                    Log.e("FILE", t.message!!)
                }

                override fun onResponse(call: Call<resp>, resp: Response<resp>) {
                    this@RecordActivity.resp = resp.body()
                    Log.d("FILE", "msg : " + this@RecordActivity.resp?.msg)
                    Log.d("FILE", "code : " + this@RecordActivity.resp?.code)
                }
            })
        }
    }
}