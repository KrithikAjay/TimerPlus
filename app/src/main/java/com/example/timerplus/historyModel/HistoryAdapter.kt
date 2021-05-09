package com.example.timerplus.historyModel

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.timerplus.R
import com.example.timerplus.convertDurationToFormatted
import com.example.timerplus.databinding.HistoryListItemBinding
import com.example.timerplus.database.History
import com.example.timerplus.formatHistory
import java.text.Bidi

//import com.example.timerplus.formatTimer

class HistoryAdapter : ListAdapter<History,HistoryAdapter.HistoryViewHolder>(HistoryDiffUtil()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

    }

    class HistoryViewHolder(val binding:  HistoryListItemBinding) : RecyclerView.ViewHolder(binding.root){

        companion object {
            fun from(parent: ViewGroup): HistoryViewHolder {
                val inflator = LayoutInflater.from(parent.context)
                val binding = HistoryListItemBinding.inflate(inflator, parent, false)
                return HistoryViewHolder(binding)
            }
        }
        fun bind(item : History) {
            binding.history = item
            binding.executePendingBindings()
        }


    }





}
class HistoryDiffUtil : DiffUtil.ItemCallback<History>(){
    override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
        return oldItem.usedId == newItem.usedId
    }

    override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
       return oldItem == newItem
    }

}

