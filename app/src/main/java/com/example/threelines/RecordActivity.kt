package com.example.threelines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_record.*

class RecordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        var data:MutableList<Record> = setData()
        var adapter = RecordAdapter()
        adapter.listData = data
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    fun setData(): MutableList<Record> {
        var data:MutableList<Record> = mutableListOf()
        for(num in 1..10) {
            var record_id = "${num}번째 타이틀"
            var listdata = Record(num, record_id, "", "",2,"","",false)
            data.add(listdata)
        }
        return data
    }
}