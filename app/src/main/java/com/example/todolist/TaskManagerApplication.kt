package com.example.todolist

import android.app.Application

class TaskManagerApplication: Application() {
    override fun onCreate(){
        super.onCreate()
        TaskRepository.initialize(this)
    }
}