package com.example.supabase

data class Message(
    val id: String = "",
    val content: Boolean,
    val isSentByUser: Boolean = false,
    val type: MessageType = MessageType.TEXT
)

enum class MessageType {
    TEXT, AUDIO, FILE
}
