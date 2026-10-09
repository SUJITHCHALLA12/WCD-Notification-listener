package com.pentester.wcd.ui.main

import android.Manifest
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pentester.wcd.Graph
import com.pentester.wcd.R
import com.pentester.wcd.databinding.ActivityHomeBinding
import com.pentester.wcd.model.ChatItem
import com.pentester.wcd.ui.chat.ChatActivity
import com.pentester.wcd.ui.chat.ChatAdapter
import com.pentester.wcd.ui.developer.DeveloperInfoActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: ChatAdapter
    private var allThreads = listOf<ChatItem>()
    private var currentFilter = "NOTIFICATIONS"
    private var currentCategory = "chats"
    private val repository by lazy { Graph.notificationRepository }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logoWcd.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.neon_pulse)
        )

        requestNotificationPermissionIfNeeded()

        setupTabs()
        binding.profileButton.setOnClickListener {
            startActivity(Intent(this, DeveloperInfoActivity::class.java))
        }

        binding.chatRecycler.layoutManager = LinearLayoutManager(this)

        adapter = ChatAdapter(emptyList()) { chatItem: ChatItem ->
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("sender", chatItem.sender)
            intent.putExtra("groupName", chatItem.groupName)
            startActivity(intent)
        }

        binding.chatRecycler.adapter = adapter
        binding.bottomNav.selectedItemId = R.id.menu_chats
        binding.bottomNav.setOnItemSelectedListener { item ->
            currentCategory = when (item.itemId) {
                R.id.menu_channels -> "channels"
                R.id.menu_communities -> "groups"
                R.id.menu_calls -> "calls"
                else -> "chats"
            }
            updateListBasedOnFilter()
            true
        }

        lifecycleScope.launch {
            repository.allNotifications.collect { notifications ->

                // Group by sender (title), show latest message per conversation
                val threads = notifications
                    .groupBy { it.title }
                    .mapNotNull { (sender, messages) ->
                        val latest = messages.maxByOrNull { it.timestamp } ?: return@mapNotNull null
                        ChatItem(
                            id = latest.id.toString(),
                            sender = sender,
                            message = latest.content,
                            timestamp = latest.timestamp,
                            type = latest.appName,
                            category = latest.category,
                            isDeleted = latest.isDeleted,
                            isGroup = latest.isGroup,
                            groupName = latest.groupName
                        )
                    }
                    .sortedByDescending { it.timestamp }

                allThreads = threads
                updateListBasedOnFilter()
                updateChatBadge(threads.count { it.category == "chats" })
            }
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU) return
        val granted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        if (!granted) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }
    }

    private fun setupTabs() {
        binding.notificationsCard.setOnClickListener {
            currentFilter = "NOTIFICATIONS"
            setTabStyles(isNotificationsSelected = true)
            updateListBasedOnFilter()
        }

        binding.attachmentsCard.setOnClickListener {
            currentFilter = "ATTACHMENTS"
            setTabStyles(isNotificationsSelected = false)
            updateListBasedOnFilter()
        }
        setTabStyles(isNotificationsSelected = true)
    }

    private fun updateListBasedOnFilter() {
        val categoryFiltered = allThreads.filter { it.category == currentCategory }
        val filteredList = if (currentFilter == "ATTACHMENTS") {
            categoryFiltered.filter { it.type != "TEXT" && it.type != "incoming" && it.type != "outgoing" }
        } else {
            categoryFiltered
        }

        adapter.updateList(filteredList)
        binding.emptyView.visibility =
            if (filteredList.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun setTabStyles(isNotificationsSelected: Boolean) {
        if (isNotificationsSelected) {
            binding.notificationsText.setTypeface(null, Typeface.BOLD)
            binding.attachmentsText.setTypeface(null, Typeface.NORMAL)
            binding.notificationsCard.strokeColor = getColor(R.color.home_primary)
            binding.attachmentsCard.strokeColor = getColor(R.color.home_outline)
        } else {
            binding.notificationsText.setTypeface(null, Typeface.NORMAL)
            binding.attachmentsText.setTypeface(null, Typeface.BOLD)
            binding.notificationsCard.strokeColor = getColor(R.color.home_outline)
            binding.attachmentsCard.strokeColor = getColor(R.color.home_primary)
        }
    }

    private fun updateChatBadge(count: Int) {
        if (count <= 0) {
            binding.bottomNav.removeBadge(R.id.menu_chats)
            return
        }
        val badge = binding.bottomNav.getOrCreateBadge(R.id.menu_chats)
        badge.number = count
        badge.isVisible = true
    }
}
