package com.pentester.wcd.ui.chat

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pentester.wcd.Graph
import com.pentester.wcd.R
import com.pentester.wcd.databinding.ActivityChatBinding
import com.pentester.wcd.model.ChatItem
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: MessageAdapter
    private val repository by lazy { Graph.notificationRepository }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (binding.chatRoot.background as? AnimationDrawable)?.apply {
            setEnterFadeDuration(1200)
            setExitFadeDuration(1200)
            start()
        }

        val senderName = intent.getStringExtra("sender")
        val groupName = intent.getStringExtra("groupName")

        if (senderName == null) {
            finish()
            return
        }

        // Set name in toolbar
        binding.textUserName.text = groupName ?: senderName

        adapter = MessageAdapter()
        binding.recyclerViewMessages.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        binding.recyclerViewMessages.adapter = adapter

        lifecycleScope.launch {
            repository.getMessagesBySender(senderName).collect { notifications ->
                val chatItems = notifications.map { entity ->
                    // "You" means outgoing, anything else is incoming
                    val direction = if (entity.title == "You") "outgoing" else "incoming"
                    ChatItem(
                        id = entity.id.toString(),
                        sender = entity.title,
                        message = entity.content,
                        timestamp = entity.timestamp,
                        type = direction,
                        category = entity.category,
                        isDeleted = entity.isDeleted,
                        isGroup = entity.isGroup,
                        groupName = entity.groupName
                    )
                }

                adapter.updateList(chatItems)

                if (chatItems.isNotEmpty()) {
                    binding.recyclerViewMessages.scrollToPosition(chatItems.size - 1)
                }
            }
        }

    }
}
