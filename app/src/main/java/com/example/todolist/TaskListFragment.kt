  package com.example.todolist

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

private const val TAG = "TASKLISTFRAGMENT"
class TaskListFragment:Fragment(), DatePickerFragment.Listener {

    interface Listener {
        fun onClickTask(id: UUID)
    }

    private lateinit var myDayButton: ImageView
    private lateinit var nextDayButton: ImageView
    private lateinit var anyDayButton: ImageView
    private lateinit var taskRecyclerView: RecyclerView
    private var listener: Listener? = null

    private var adapter: TaskAdapter? = TaskAdapter(emptyList())

    private val taskListViewModel: TaskListViewModel by lazy {
        ViewModelProviders.of(this).get(TaskListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)

        myDayButton = view.findViewById(R.id.myDay)
        nextDayButton = view.findViewById(R.id.nextDay)
        anyDayButton = view.findViewById(R.id.anyDay)
        taskRecyclerView = view.findViewById(R.id.task_recycler_view) as RecyclerView
        taskRecyclerView.layoutManager = LinearLayoutManager(context)
        taskRecyclerView.adapter = adapter
        return view
    }

    override fun onStart() {
        super.onStart()
        myDayButton.setOnClickListener{
            //populates with tasks due today
        }
        nextDayButton.setOnClickListener{
            //populates with tasks due tomorrow
        }
        anyDayButton.setOnClickListener{
            //choose the due date and tasks will be added
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskListViewModel.tasksListLiveData.observe(
                viewLifecycleOwner,
                Observer { tasks: List<Task> ->
                    tasks?.let {
                        Log.i(TAG, "Got Tasks ${tasks.size}")
                        setRecyclerViewAdapter(tasks)
                    }
                })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as Listener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_task_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_task -> {
                val task = Task()
                taskListViewModel.addTask(task)
                listener?.onClickTask(task.id)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

        //Sends the list of tasks to populate using adapter class
    private fun setRecyclerViewAdapter(tasks: List<Task>) {
        taskRecyclerView.adapter = TaskAdapter(tasks)
    }

    companion object {
        fun newInstance(): TaskListFragment {
            return TaskListFragment()
        }
    }


    private inner class TaskHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {

        override fun onClick(v: View?) {
            listener?.onClickTask(task.id)
        }

        private lateinit var task: Task

        private val titleTextView: TextView = itemView.findViewById(R.id.editTextTitle)
        private val dateTextView: TextView = itemView.findViewById(R.id.editTextDate)
        private val notesImgView: ImageView = itemView.findViewById(R.id.notesView)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(task: Task) {
            this.task = task
            titleTextView.text = this.task.title
            if (task.completed) {
                titleTextView.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
            }
                dateTextView.text = "${this.task.date.toString()}"
                notesImgView.visibility = if (!task.notes.equals("")) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        } //TASK HOLDER ENDS HERE

        private inner class TaskAdapter(var tasks: List<Task>)
            : RecyclerView.Adapter<TaskHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
                val view = layoutInflater.inflate(R.layout.list_item_task, parent, false)
                return TaskHolder(view)
            }

            override fun onBindViewHolder(holder: TaskHolder, position: Int) {
                val task = tasks[position]
                holder.bind(task)
            }

            override fun getItemCount() = tasks.size

            }    //ADAPTER ENDS HERE

            override fun onDateSelected(date: Date) {
                Log.i(TAG,"I WILL DO NOTHING FOR NOW")
            }
}//TASK LIST FRAGMENT ENDS HERE