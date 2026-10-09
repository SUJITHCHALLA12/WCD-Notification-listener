package com.pentester.wcd.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pentester.wcd.R
import com.pentester.wcd.model.ChatItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var messages: List<ChatItem> = emptyList()

    companion object {
        private const val VIEW_TYPE_INCOMING = 1
        private const val VIEW_TYPE_OUTGOING = 2
        private val TIME_FORMAT = SimpleDateFormat("hh:mm a", Locale.getDefault())
        private const val COLOR_NORMAL_TEXT = 0xFF000000.toInt()
    }

    inner class IncomingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textMessage: TextView = view.findViewById(R.id.textMessage)
        val textTime: TextView = view.findViewById(R.id.textTime)
        val bubble: View = view.findViewById(R.id.bubbleContainer)
    }

    inner class OutgoingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textMessage: TextView = view.findViewById(R.id.textMessage)
        val textTime: TextView = view.findViewById(R.id.textTime)
        val bubble: View = view.findViewById(R.id.bubbleContainer)
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].type == "outgoing") VIEW_TYPE_OUTGOING else VIEW_TYPE_INCOMING
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_OUTGOING) {
            val view = inflater.inflate(R.layout.item_message_sent, parent, false)
            OutgoingViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_message_received, parent, false)
            IncomingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = messages[position]
        val timeStr = TIME_FORMAT.format(Date(item.timestamp))

        when (holder) {
            is IncomingViewHolder -> {
                if (item.isDeleted) {
                    holder.textMessage.text = holder.itemView.context.getString(R.string.deleted_message_incoming)
                    holder.textMessage.setTextColor(
                        ContextCompat.getColor(holder.itemView.context, R.color.deleted_red)
                    )
                    holder.bubble.setBackgroundResource(R.drawable.bg_deleted)
                } else {
                    holder.textMessage.text = item.message
                    holder.textMessage.setTextColor(COLOR_NORMAL_TEXT)
                    holder.bubble.setBackgroundResource(R.drawable.bg_received)
                }
                holder.textTime.text = timeStr
            }
            is OutgoingViewHolder -> {
                if (item.isDeleted) {
                    holder.textMessage.text = holder.itemView.context.getString(R.string.deleted_message_outgoing)
                    holder.textMessage.setTextColor(
                        ContextCompat.getColor(holder.itemView.context, R.color.deleted_red)
                    )
                    holder.bubble.setBackgroundResource(R.drawable.bg_deleted)
                } else {
                    holder.textMessage.text = item.message
                    holder.textMessage.setTextColor(COLOR_NORMAL_TEXT)
                    holder.bubble.setBackgroundResource(R.drawable.bg_sent)
                }
                holder.textTime.text = timeStr
            }
        }
    }

    override fun getItemCount() = messages.size

    fun updateList(newList: List<ChatItem>) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() = messages.size
            override fun getNewListSize() = newList.size
            override fun areItemsTheSame(o: Int, n: Int) = messages[o].id == newList[n].id
            override fun areContentsTheSame(o: Int, n: Int) = messages[o] == newList[n]
        })
        messages = newList
        diff.dispatchUpdatesTo(this)
    }
}
