package com.evgeniykim.callrecord.data

import com.evgeniykim.callrecord.models.JournalModels

data class PagedResponse(
    val pageInfo: PageInfo,
    val results: List<JournalModels> = listOf()
)

data class PageInfo(
    val count: Int,
    val pages: Int,
    val next: String,
    val prev: String?
)
