package com.example.threelines.Activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.threelines.Data.Result
import com.example.threelines.Network.RetrofitClient
import com.example.threelines.Network.RetrofitService
import com.example.threelines.R
import com.github.squti.androidwaverecorder.WaveRecorder
import com.tyorikan.voicerecordingvisualizer.RecordingSampler
import com.tyorikan.voicerecordingvisualizer.VisualizerView
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
class MicActivity : Fragment(R.layout.activity_mic) {
    private var TAG = "MIC"
    private lateinit var retrofit: Retrofit
    private lateinit var retrofitService: RetrofitService

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
            }
        }

        button_stop_recording.setOnClickListener{
            Log.d(TAG, "Record End")
            Toast.makeText(activity!!.applicationContext, "녹음 끝", Toast.LENGTH_SHORT).show()
            waveRecorder.stopRecording()
        }
/*
        //Pause
        waveRecorder.pauseRecording()

        //Resume
        waveRecorder.resumeRecording()
*/

        // Dialog builder
        val builder = AlertDialog.Builder(activity!!.applicationContext)
        val dialogView = layoutInflater.inflate(R.layout.dialog_item, null)

        val title_edit = dialogView.findViewById<EditText>(R.id.editText_title)
        val people_edit = dialogView.findViewById<EditText>(R.id.editText_people)
        val location_edit = dialogView.findViewById<EditText>(R.id.editText_location)

        // Dialog View
        button_send_audio.setOnClickListener{
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
}