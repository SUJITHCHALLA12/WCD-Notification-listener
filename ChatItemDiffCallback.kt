package com.pentester.wcd.ui.chat

import androidx.recyclerview.widget.DiffUtil
import com.pentester.wcd.model.ChatItem

class ChatItemDiffCallback(private val oldList: List<ChatItem>, private val newList: List<ChatItem>) :
    DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
