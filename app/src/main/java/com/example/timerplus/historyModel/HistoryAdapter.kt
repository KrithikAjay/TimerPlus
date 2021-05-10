package com.example.timerplus.historyModel

import android.util.Log
import android.view.LayoutInflater
import android.view.View
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.Bidi


//paraeters for notify the positions of viewHolders
 private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class HistoryAdapter : ListAdapter<DataItem,RecyclerView.ViewHolder>(HistoryDiffUtil()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> HistoryViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       when(holder){
           is HistoryViewHolder ->{
               val item = getItem(position) as DataItem.HistoryItem
               holder.bind(item.history)

           }
       }

    }

    override fun getItemViewType(position: Int): Int {
        return  when(getItem(position)){
           is  DataItem.Header -> ITEM_VIEW_TYPE_HEADER
           is  DataItem.HistoryItem -> ITEM_VIEW_TYPE_ITEM

        }
    }

    //Override the submit list functions for our Modified list
    private val adapterScope = CoroutineScope(Dispatchers.Default)
    fun addHistoryOrHeader(list : List<History>){
        adapterScope.launch {
           val items =  when(list){
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map{ DataItem.HistoryItem(it)}
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }

    }


//Viewholder For History List
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

    //ViewHolder for Header
    class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return TextViewHolder(view)
            }
        }
    }





}

//Diffutil Class for Find the Chances in the database
class HistoryDiffUtil : DiffUtil.ItemCallback<DataItem>(){
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
       return oldItem == newItem
    }

}


//Sealed class DataItem for Separating our history items and header

/*
why sealed class : because all the dataItems in this sealed class must br defined in this file
As the result number of subclass are known to the compiler , it is not possible to add new
data item to your sealed hence your adapter won't break

 */
sealed class DataItem{
    class HistoryItem ( val history : History) : DataItem() {
        override val id: Long = history.usedId

    }

    object Header : DataItem() {
        override val id: Long
            get() = Long.MIN_VALUE
    }

    //id for  notify the diffUtil class what changed in the History Item
    abstract val id : Long
}

