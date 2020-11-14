package com.example.threelines.Activity

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.threelines.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recordActivity = RecordActivity()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, recordActivity)
            commit()
        }

        val bottomNavigationView = findViewById<View>(R.id.navigation_view) as BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.btn_record_fragment -> {
                val recordActivity = RecordActivity()
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment, recordActivity)
                    commit()
                }
            }

            R.id.btn_mic_fragment -> {
                val micActivity = MicActivity()
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment, micActivity)
                    commit()
                }
            }

            R.id.btn_setting_fragment -> {
//                val settingActivity = SettingActivity()
//                supportFragmentManager.beginTransaction().apply {
//                    replace(R.id.fragment, settingActivity)
//                    commit()
//                }
            }
        }
        return true
    }
}
