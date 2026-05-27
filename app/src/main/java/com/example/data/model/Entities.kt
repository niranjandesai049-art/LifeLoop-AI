package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val details: String,
    val dueDate: Long,
    val priority: String = "MEDIUM", // HIGH, MEDIUM, LOW
    val category: String = "Life",  // Life, Work, Bills, Health, Study, Unsorted
    val isCompleted: Boolean = false,
    val isRecurring: Boolean = false
)

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val timestamp: Long,
    val isAutoDetected: Boolean = false,
    val urgency: String = "NORMAL", // URGENT, NORMAL, LOW
    val status: String = "ACTIVE"   // ACTIVE, SNOOZED, COMPLETED
)

@Entity(tableName = "memories")
data class Memory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val textContent: String,
    val category: String = "General", // Personal, Knowledge, Professional, Social
    val sourceMimeType: String? = null, // "image/jpeg", "text/plain", "audio/wav"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "documents")
data class Document(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val extractedText: String,
    val deadline: Long? = null,
    val category: String = "Unsorted",
    val imageUri: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val role: String, // "user", "model"
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)
