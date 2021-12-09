package com.evgeniykim.callrecord.di

import com.evgeniykim.callrecord.data.CallHistoryService
import com.evgeniykim.callrecord.viewmodels.JournalViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val groupModule = module {
    viewModel { JournalViewModel(CallHistoryService(androidApplication().contentResolver, androidApplication().applicationContext)) }
}