package com.example.threelines.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.threelines.Data.Speaker
import com.example.threelines.R
import kotlinx.android.synthetic.main.speaker_item.view.*

class SpeakerAdapter : RecyclerView.Adapter<SpeakerHolder>(){
    var listData = mutableListOf<Speaker>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeakerHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.speaker_item, parent, false)
        return SpeakerHolder(view)
    }

    override fun onBindViewHolder(holder: SpeakerHolder, position: Int) {
        val data = listData.get(position)
        holder.setListData(data)
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}

class SpeakerHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    fun setListData(listdata: Speaker){
        itemView.speaker_id.text = "${listdata.speaker_id}"
        itemView.speaker_name.setText(listdata.speaker_name)
    }
}