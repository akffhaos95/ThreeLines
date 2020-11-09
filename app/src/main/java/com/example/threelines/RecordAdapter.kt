package com.example.threelines

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.threelines.Activity.TextActivity
import com.example.threelines.Data.Record
import kotlinx.android.synthetic.main.record_item.view.*

class RecordAdapter : RecyclerView.Adapter<Holder>(){
    var listData = mutableListOf<Record>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.record_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = listData.get(position)
        holder.setListData(data)

        holder.itemView.setOnClickListener{
            Log.d("ADAPTER", "Clicked ${holder.itemView?.record_id.text}")
            val intent = Intent(holder.itemView?.context, TextActivity::class.java)
            intent.putExtra("record_id", holder.itemView?.record_id.text)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}
/*
* data class Record (
    val record_id: Int,
    val audio_file: String,
    val title: String,
    val uploaded_date: Date,
    val people: Int,
    val location: String,
    val user_id: String,
    val deleted: Boolean
    )
* */
class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
    fun setListData(listdata: Record){
        itemView.record_id.text = "${listdata.record_id}"
        itemView.audio_file.text = listdata.audio_file
    }
}