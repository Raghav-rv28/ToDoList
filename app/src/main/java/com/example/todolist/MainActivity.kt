package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import java.util.*
private const val TAG = "MainActivity"
private lateinit var ListViewButton: ImageView
private lateinit var CalendarViewButton: ImageView
class MainActivity : AppCompatActivity(), TaskListFragment.Listener {

    override fun onClickTask(id: UUID) {
        Log.i(TAG, "called onClickCrime id = $id")
        val fragment = TaskFragment.newInstance(id)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ListViewButton = findViewById(R.id.listView)
        CalendarViewButton = findViewById(R.id.CalendarView)
        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = TaskListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }
/**
    fun calendarFragment():
    {
        val fragment = CalendarViewFragment.newInstance()
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
    }
    */
}