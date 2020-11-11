package com.example.threelines.Adapter

import android.content.Intent
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
            Log.d("ADAPTER", "Clicked ${holder.itemView?.record_id.text}")
            val intent = Intent(holder.itemView?.context, TextActivity::class.java)
            intent.putExtra("record_id", holder.itemView?.record_id.text.toString().toInt())
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
        itemView.uploaded_date.text = listdata.uploaded_date
        itemView.people.text = "${listdata.people}"
        itemView.location.text = listdata.location
    }
}