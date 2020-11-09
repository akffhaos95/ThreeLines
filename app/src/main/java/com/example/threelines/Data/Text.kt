package com.example.threelines.Data

import java.sql.Date

data class Text (
    val idx: Int,
    val content: String,
    val start_t: String,
    val end_t: String,
    val speaker_id: Int,
    val speaker_name: String
)