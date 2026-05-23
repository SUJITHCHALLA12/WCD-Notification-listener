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

class ChatAdapter(
    list: List<ChatItem>,
    private val onClick: (ChatItem) -> Unit
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private var chatList: List<ChatItem> = list

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.nameText)
        val message: TextView = view.findViewById(R.id.messagePreview)
        val time: TextView = view.findViewById(R.id.timeText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun getItemCount() = chatList.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val item = chatList[position]
        holder.name.text = item.groupName ?: item.sender
        holder.time.text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(item.timestamp))

        holder.itemView.setOnClickListener { onClick(item) }

        if (item.isDeleted) {
            holder.message.text = holder.itemView.context.getString(R.string.deleted_message_incoming)
            holder.message.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.deleted_red)
            )
        } else {
            holder.message.text = item.message
            holder.message.setTextColor(0xFF777777.toInt())
        }
    }

    fun updateList(newList: List<ChatItem>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() = chatList.size
            override fun getNewListSize() = newList.size
            override fun areItemsTheSame(o: Int, n: Int) = chatList[o].id == newList[n].id
            override fun areContentsTheSame(o: Int, n: Int) = chatList[o] == newList[n]
        })
        chatList = newList
        diffResult.dispatchUpdatesTo(this)
    }
}
