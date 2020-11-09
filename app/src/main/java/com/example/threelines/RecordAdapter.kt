package com.example.threelines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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