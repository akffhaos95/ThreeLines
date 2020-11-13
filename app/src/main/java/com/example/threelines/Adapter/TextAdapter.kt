package com.example.threelines.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.threelines.Data.Text
import com.example.threelines.R
import kotlinx.android.synthetic.main.text_item.view.*
import android.util.Log.d as d1

class TextAdapter : RecyclerView.Adapter<TextHolder>(){
    var listData = mutableListOf<Text>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.text_item, parent, false)
        return TextHolder(view)
    }

    override fun onBindViewHolder(holder: TextHolder, position: Int) {
        val data = listData.get(position)
        holder.setListData(data)

        holder.itemView.setOnClickListener{
            // 수정 페이지 (dialog)
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}

class TextHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    fun setListData(listdata: Text){
        itemView.idx.text = "${listdata.idx}"
        itemView.content.text = listdata.content

        var start_t = listdata.start_t
        var end_t = listdata.end_t

        var time : Int = end_t.substring(0,1).toInt() - start_t.substring(0,1).toInt()
        var min : Int = end_t.substring(2,4).toInt() - start_t.substring(2,4).toInt()
        var sec : Int = end_t.substring(5).toInt() - start_t.substring(5).toInt()

        if (min < 0) {
            min += 60
            time -= 1
        }
        if (sec < 0) {
            sec += 1000
            min -= 1
        }
        itemView.time.text = "${time.toString()}:${min.toString()}.${sec.toString()}"

        if(listdata.speaker_name == null){
            itemView.speaker_name.text = listdata.speaker_id.toString()
        } else {
            itemView.speaker_name.text = listdata.speaker_name
        }
    }
}