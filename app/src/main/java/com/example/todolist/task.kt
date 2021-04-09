package com.example.todolist

import java.sql.Time
import java.util.*
import java.util.Date

data class Task(val id: UUID = UUID.randomUUID(),
                var title: String = "",
                var date: Date = Date(),
                var notes: String = "",
                var tags: List<String> = mutableListOf<String>(),
                var ETC: Int = 30, // in minutes
                var completed: Boolean = false)