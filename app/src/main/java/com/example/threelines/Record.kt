package com.example.threelines

import java.sql.Date

data class Record (
    val record_id: Int,
    val audio_file: String,
    val title: String,
    val uploaded_date: String,
    val people: Int,
    val location: String,
    val user_id: String,
    val deleted: Boolean
)