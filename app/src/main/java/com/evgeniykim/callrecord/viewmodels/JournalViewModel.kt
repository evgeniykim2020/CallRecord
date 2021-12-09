package com.evgeniykim.callrecord.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.evgeniykim.callrecord.data.CallHistoryService
import com.evgeniykim.callrecord.data.JournalDataSource
import com.evgeniykim.callrecord.models.JournalModels
import kotlinx.coroutines.flow.Flow

class JournalViewModel(private val callHistoryService: CallHistoryService) : ViewModel() {

    fun loadCallsHistory(): Flow<PagingData<JournalModels>> =
        Pager(config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { JournalDataSource(callHistoryService) })
            .flow.cachedIn(viewModelScope)
}