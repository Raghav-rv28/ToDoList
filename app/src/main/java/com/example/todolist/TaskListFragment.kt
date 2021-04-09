package com.example.todolist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "TASKLISTFRAGMENT"
class TaskListFragment:Fragment() {

    private lateinit var taskRecyclerView: RecyclerView
    private var adapter: TaskAdapter? = null
    private val taskListViewModel: TaskListViewModel by lazy {
        ViewModelProviders.of(this).get(TaskListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total crimes: ${taskListViewModel.tasks.size}")
    }

    companion object{
        fun newinstance(): TaskListFragment{
            return TaskListFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)

        taskRecyclerView =
            view.findViewById(R.id.task_recycler_view) as RecyclerView
        taskRecyclerView.layoutManager = LinearLayoutManager(context)

        updateUI()
        return view
    }

    private fun updateUI(){
        val tasks=taskListViewModel.tasks
        adapter = TaskAdapter(tasks)
        taskRecyclerView.adapter = adapter
    }


    private inner class TaskHolder(view : View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var task: Task
        private val titleTextView: TextView = itemView.findViewById(R.id.editTextTitle)
        private val dateTextView: TextView = itemView.findViewById(R.id.editTextDate)

        init{
            itemView.setOnClickListener(this)
        }

        fun bind(task: Task){
            this.task= task
            titleTextView.text= this.task.title
            dateTextView.text= this.task.date.toString()
        }

        override fun onClick(v: View?) {
            Toast.makeText(context, "${task.title} pressed!", Toast.LENGTH_SHORT).show()
        }
    }

    private inner class TaskAdapter(var tasks: List<Task>)
        : RecyclerView.Adapter<TaskHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
            val view = layoutInflater.inflate(R.layout.list_item_task, parent,false)
            return TaskHolder(view)
        }

        override fun onBindViewHolder(holder: TaskHolder, position: Int) {
            val task = tasks[position]
            holder.bind(task)
        }

        override fun getItemCount() = tasks.size

    }
}