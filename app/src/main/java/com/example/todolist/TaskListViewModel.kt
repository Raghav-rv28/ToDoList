package com.example.todolist

import androidx.lifecycle.LiveData
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
    fun getTasksSortedDateAsc(): LiveData<List<Task>> {
        return taskRepository.getTasksSortedDateAsc()
    }

    fun getTasksSortedDateDesc(): LiveData<List<Task>> {
        return taskRepository.getTasksSortedDateDesc()
    }
    fun getTasksSortedLevelAsc(): LiveData<List<Task>> {
        return taskRepository.getTasksSortedLevelAsc()
    }
    fun getTasksSortedLevelDesc(): LiveData<List<Task>> {
        return taskRepository.getTasksSortedLevelDesc()
    }
    fun getTasksSortedEtcAsc(): LiveData<List<Task>> {
        return taskRepository.getTasksSortedEtcAsc()
    }
    fun getTasksSortedEtcDesc():LiveData<List<Task>> {
        return taskRepository.getTasksSortedEtcDesc()
    }
}