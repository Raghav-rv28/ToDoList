package com.example.todolist

import android.provider.CalendarContract
import java.util.*
import java.util.Date

data class Task(val id: UUID = UUID.randomUUID(),
                var title: String = "",
                var date: Date = Date(),
                var notes: String = "",
                var completed: Boolean = false)