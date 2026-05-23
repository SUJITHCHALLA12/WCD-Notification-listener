package com.pentester.wcd.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pentester.wcd.R
import com.pentester.wcd.model.ChatThread
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatThreadAdapter(
    private var chatList: List<ChatThread>,
    private val onClick: (ChatThread) -> Unit
) : RecyclerView.Adapter<ChatThreadAdapter.ChatViewHolder>() {

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
        holder.name.text = item.sender
        holder.message.text = item.lastMessage
        holder.time.text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(item.timestamp))

        holder.itemView.setOnClickListener { onClick(item) }
    }

    fun updateList(newList: List<ChatThread>) {
        val diffResult = DiffUtil.calculateDiff(ChatThreadDiffCallback(chatList, newList))
        chatList = newList
        diffResult.dispatchUpdatesTo(this)
    }
}

class ChatThreadDiffCallback(
    private val oldList: List<ChatThread>,
    private val newList: List<ChatThread>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].sender == newList[newItemPosition].sender
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
