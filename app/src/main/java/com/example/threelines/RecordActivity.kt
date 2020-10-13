package com.example.threelines

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import java.io.IOException
import java.lang.IllegalStateException

class RecordActivity : AppCompatActivity() {

    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var state: Boolean = false
    private var recordingStopped: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        /* mediaRecorder */
        output = Environment.DIRECTORY_DOWNLOADS + "/recording.mp3"
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder?.setOutputFile(output)

        /* record_start */
        button_start_recording.setOnClickListener {
            /* permission check */
            if (ContextCompat.checkSelfPermission(this, // 오디오 녹음에 대한 권한
                    Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, // 저장소에 대한 권한
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                val permissions = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(this, permissions,0)
            } else {
                startRecording()
                Log.d("FILE", ""+output)
            }
        }

        /* record_stop */
        button_stop_recording.setOnClickListener{
           stopRecording()
        }

        /* record_pause */
        button_pause_recording.setOnClickListener {
            pauseRecording()
        }

        /* retrofit */
        var retrofit = Retrofit.Builder()
            .baseUrl("http://localhost:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var fileUploadService: FileUploadService = retrofit.create(FileUploadService::class.java)

        button_send_audio.setOnClickListener{
            Toast.makeText(this, "파일 전송", Toast.LENGTH_SHORT).show()

            var file = File(output);
            var requestBody : RequestBody = RequestBody.create(MediaType.parse("multipart/form-data/"),file)
            var body : MultipartBody.Part = MultipartBody.Part.createFormData("uploaded_file","test",requestBody)

            fileUploadService.uploadFile("tes", body).enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("FILE", t.message!!)
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                   if (response?.isSuccessful){
                       Toast.makeText(applicationContext, "File Upload Successfully...", Toast.LENGTH_LONG).show();
                       Log.d("FILE", "" + response?.body().toString());
                   } else {
                       Toast.makeText(applicationContext, "Some error occurred...", Toast.LENGTH_LONG).show();
                   }
                }
            })
        }
    }

    private fun startRecording() {
        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            state = true
            Toast.makeText(this, "녹음 시작", Toast.LENGTH_SHORT).show()
        } catch (e: IllegalStateException){
            e.printStackTrace()
        } catch (e: IOException){
            e.printStackTrace()
        }
    }

    private fun stopRecording() {
        if (state){
            mediaRecorder?.stop()
            mediaRecorder?.release()
            state = false
        } else{
            Toast.makeText(this, "녹음 끝", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("RestrictedApi", "SetTextI18n")
    @TargetApi(Build.VERSION_CODES.N)
    private fun pauseRecording(){
        if (state){
            Toast.makeText(this, "녹음 중지", Toast.LENGTH_SHORT).show()
            mediaRecorder?.pause()
            recordingStopped = true
            button_pause_recording.text = "다시 시작"
        } else {
            resumeRecording()
        }
    }

    @SuppressLint("RestrictedApi", "SetTextI18n")
    @TargetApi(Build.VERSION_CODES.N)
    private fun resumeRecording(){
        Toast.makeText(this, "다시 시작", Toast.LENGTH_SHORT).show()
        mediaRecorder?.resume()
        button_pause_recording.text = "중지"
        recordingStopped = false
    }
}