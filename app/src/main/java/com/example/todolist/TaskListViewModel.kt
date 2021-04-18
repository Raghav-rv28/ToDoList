package com.example.todolist

import androidx.lifecycle.ViewModel

class TaskListViewModel: ViewModel(){


    val taskRepository =  TaskRepository.get()
    val tasksListLiveData = taskRepository.getTasks()

    fun addTask(task: Task) {
        taskRepository.addTask(task)
    }

    fun deleteTask(task: Task){
        taskRepository.deleteTask(task)
    }
}