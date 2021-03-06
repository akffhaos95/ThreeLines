package com.example.threelines.Activity

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.threelines.Data.Result
import com.example.threelines.Network.RetrofitClient
import com.example.threelines.Network.RetrofitService
import com.example.threelines.R
import com.github.squti.androidwaverecorder.WaveRecorder
import kotlinx.android.synthetic.main.activity_mic.*
import kotlinx.android.synthetic.main.activity_mic.view.*
import kotlinx.android.synthetic.main.text_item.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.util.*
import kotlin.concurrent.timer


/*
* MicActivity
* 회의 녹음 -> 서버로 전송
* 회의 녹음 : MediaRecorder
*   녹음 [시작, 중지, 재개, 정지]
*   파일 경로는 ~~~로
* 서버 전송 : Retrofit
* */
class MicActivity : Fragment(R.layout.activity_mic) {
    private var TAG = "MIC"
    private lateinit var retrofit: Retrofit
    private lateinit var retrofitService: RetrofitService
    private lateinit var timerTask : Timer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Intent get
        var intent: Intent? = activity!!.intent
        var user_id: String? = intent!!.getStringExtra("user_id")
        Log.d(TAG, "user_id : $user_id")

        // retrofit
        initRetrofit()

        var AUDIO_FILE_PATH : String = activity!!.externalCacheDir?.absolutePath + "/"+ user_id + "." + Date().time.toString()+".wav"

        // https://github.com/squti/Android-Wave-Recorder
        val waveRecorder = WaveRecorder(filePath = AUDIO_FILE_PATH)

        button_start_recording.setOnClickListener {
            Log.d(TAG, AUDIO_FILE_PATH)
            if (ContextCompat.checkSelfPermission(
                    activity!!.applicationContext, // 오디오 녹음에 대한 권한
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    activity!!.applicationContext, // 저장소에 대한 권한
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {
                val permissions = arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                Log.d(TAG, "Permission Error")
                Toast.makeText(activity!!.applicationContext, "권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(activity!!, permissions, 0)
            } else {
                Log.d(TAG, "Record Start")
                Toast.makeText(activity!!.applicationContext, "녹음 시작", Toast.LENGTH_SHORT).show()
                waveRecorder.startRecording()

                button_start_recording.visibility = View.GONE
                button_stop_recording.visibility = View.VISIBLE
                button_send_audio.visibility = View.GONE

                startTimer()
            }
        }

        button_stop_recording.setOnClickListener{
            Log.d(TAG, "Record End")
            Toast.makeText(activity!!.applicationContext, "녹음 끝", Toast.LENGTH_SHORT).show()
            waveRecorder.stopRecording()

            button_start_recording.visibility = View.VISIBLE
            button_stop_recording.visibility = View.GONE
            button_send_audio.visibility = View.VISIBLE

            stopTimer()
        }
/*
        //Pause
        waveRecorder.pauseRecording()

        //Resume
        waveRecorder.resumeRecording()
*/

        // Dialog builder
        val builder = AlertDialog.Builder(activity!!)
        val dialogView = layoutInflater.inflate(R.layout.dialog_item, null)

        val title_edit = dialogView.findViewById<EditText>(R.id.editText_title)
        val people_edit = dialogView.findViewById<EditText>(R.id.editText_people)
        val location_edit = dialogView.findViewById<EditText>(R.id.editText_location)

        // Dialog View
        button_send_audio.setOnClickListener{
            Log.d(TAG, "Btn Send Audio")
            if (dialogView.parent != null) (dialogView.parent as ViewGroup).removeView(
                dialogView
            )
            builder.setView(dialogView)
                .setPositiveButton("확인") { _, _ ->
                    Log.d(TAG, "Send Audio")
                    Toast.makeText(activity!!.applicationContext, "오디오를 전송합니다.", Toast.LENGTH_SHORT).show()

                    // 제목, 인원, 장소
                    val title = title_edit.text.toString()
                    val people = people_edit.text.toString()
                    val location = location_edit.text.toString()

                    // 녹음된 파일
                    var file : File = File(AUDIO_FILE_PATH)
                    var requestFile : RequestBody = RequestBody.create(
                        MediaType.parse("audio/*"),
                        file
                    );
                    var uploadFile : MultipartBody.Part = MultipartBody.Part.createFormData(
                        "file",
                        AUDIO_FILE_PATH,
                        requestFile
                    )

                    // Retrofit 전송
                    postMic(
                        retrofitService,
                        title,
                        user_id!!,
                        people.toInt(),
                        location,
                        uploadFile
                    )
                }
                .setNegativeButton("취소") { _, _ ->

                }
                .show()
        }
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getRetrofit()
        retrofitService = retrofit.create(RetrofitService::class.java)
    }

    private fun postMic(
        service: RetrofitService,
        title: String,
        user_id: String,
        people: Int,
        location: String,
        file: MultipartBody.Part
    ): Boolean {
        var result: Boolean = false
        service.mic(title, user_id, people, location, file).enqueue(object : Callback<Result> {
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

    private fun startTimer() {
        var tim = 0
        var min = 0
        var sec = 0

        timerTask = timer(period = 1000) {
            sec++
            if (sec == 60) {
                min++
                sec = 0
            }
            if (min == 60) {
                tim++
                min = 0
            }
            activity!!.runOnUiThread {
                text_tim.text = "$tim"
                if(sec == 0){
                    text_sec.text = "00"
                } else if(sec < 10) {
                    text_sec.text = "0$sec"
                } else {
                    text_sec.text = "$sec"
                }
                if(min == 0){
                    text_min.text = "00"
                } else if(min < 10) {
                    text_min.text = "0$min"
                } else {
                    text_min.text = "$min"
                }
            }
        }
    }

    private fun stopTimer() {
        timerTask.cancel()
    }
}