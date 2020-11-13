package com.example.threelines.Activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.threelines.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_main)

        val recordActivity = RecordActivity()
        val micActivity = MicActivity()
        //val settingActivity = SettingFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, micActivity)
            commit()
        }

        btn_record_fragment.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment, recordActivity)
                commit()
            }
        }

        btn_mic_fragment.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment, micActivity)
                commit()
            }
        }

        btn_setting_fragment.setOnClickListener {

        }
    }
}
