package com.android.example.searchwithdebounce.data.repository

import kotlinx.coroutines.delay

class QuoteRepository {
    private val quotes = listOf(
        "Believe in yourself",
        "Stay positive, work hard, make it happen",
        "Dream big and dare to fail",
        "Every moment is a fresh beginning",
        "The harder you work, the luckier you get",
        "Turn your wounds into wisdom",
        "Action is the foundational key to all success",
        "The only limit to our realization of tomorrow is doubts of today"
    )

    suspend fun searchQuotes(query: String): List<String> {
        delay(300)
        if (query.isBlank()) return quotes
        return quotes.filter { it.contains(query, ignoreCase = true) }
    }
}