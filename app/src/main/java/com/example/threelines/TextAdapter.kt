package com.example.threelines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.threelines.Data.Text
import kotlinx.android.synthetic.main.text_item.view.*

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
        itemView.speaker_name.text = listdata.speaker_name
        itemView.start_t.text = listdata.start_t
        itemView.end_t.text = listdata.end_t
    }
}