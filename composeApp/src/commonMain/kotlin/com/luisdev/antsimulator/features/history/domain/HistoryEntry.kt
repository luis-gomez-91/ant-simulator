package com.luisdev.antsimulator.features.history.domain

data class HistoryEntry(
    val id: Long,
    val date: Long, // Timestamp
    val score: Int,
    val totalQuestions: Int
)