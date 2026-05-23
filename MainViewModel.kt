package com.pentester.wcd.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pentester.wcd.Graph
import com.pentester.wcd.data.local.NotificationEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val notificationRepo = Graph.notificationRepository
    private val messageRepo      = Graph.messageRepository

    // ── Notification threads (one row per sender, newest message) ──────────
    private val _threads = MutableStateFlow<List<NotificationEntity>>(emptyList())
    val threads: StateFlow<List<NotificationEntity>> = _threads.asStateFlow()

    // ── Search results across messages table ───────────────────────────────
    private val _searchResults = MutableStateFlow<List<com.pentester.wcd.data.local.MessageEntity>>(emptyList())
    val searchResults: StateFlow<List<com.pentester.wcd.data.local.MessageEntity>> =
        _searchResults.asStateFlow()

    init {
        observeThreads()
    }

    private fun observeThreads() {
        notificationRepo.latestPerSender
            .onEach { _threads.value = it }
            .catch { throwable -> /* log or expose error state */ }
            .launchIn(viewModelScope)
    }

    fun search(query: String) {
        messageRepo.search(query)
            .onEach { _searchResults.value = it }
            .catch { throwable -> }
            .launchIn(viewModelScope)
    }
}