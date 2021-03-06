package com.example.threelines.Adapter

import android.content.Intent
import android.graphics.Color
import android.text.TextUtils.substring
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.threelines.Activity.TextActivity
import com.example.threelines.Data.Record
import com.example.threelines.R
import kotlinx.android.synthetic.main.record_item.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class RecordAdapter : RecyclerView.Adapter<RecordHolder>(){
    var listData = mutableListOf<Record>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.record_item, parent, false)
        return RecordHolder(view)
    }

    override fun onBindViewHolder(holder: RecordHolder, position: Int) {
        val data = listData.get(position)
        holder.setListData(data)

        holder.itemView.setOnClickListener{
            Log.d("ADAPTER", "Clicked ${holder.itemView.record_id.text}")
            val intent = Intent(holder.itemView.context, TextActivity::class.java)
            intent.putExtra("record_id", holder.itemView.record_id.text.toString().toInt())
            startActivity(holder.itemView.context, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}

class RecordHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    fun setListData(listdata: Record){
        itemView.record_id.text = "${listdata.record_id}"
        itemView.title.text = listdata.title
        itemView.people.text = "${listdata.people}인"
        itemView.location.text = listdata.location
        val uploaded_date = listdata.uploaded_date.split(".")[0].split("T")
        val date = uploaded_date[0]
        val time = uploaded_date[1]
        itemView.uploaded_date.text = "$date $time"

        val random = Random()
        var color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
        itemView.icon.text = substring(listdata.title, 0, 1)
        itemView.icon.setBackgroundColor(color)
    }
}