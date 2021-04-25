  package com.example.todolist

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.util.*


  private const val TAG = "TASKLISTFRAGMENT"
  private const val DIALOG_DATE = "dialog_date"
  private const val REQUEST_DATE = 0

class TaskListFragment:Fragment(), DatePickerFragment.Listener {

    interface Listener {
        fun onClickTask(id: UUID)
    }

    private lateinit var myDayButton: ImageView
    private lateinit var nextDayButton: ImageView
    private lateinit var anyDayButton: ImageView
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var sortingSpinner: Spinner
    private var listener: Listener? = null

    private var adapter: TaskAdapter? = TaskAdapter(emptyList())

    private val taskListViewModel: TaskListViewModel by lazy {
        ViewModelProviders.of(this).get(TaskListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun addItemsOnSpinner(context: Context) {

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            context,
            R.array.Sort,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            sortingSpinner.adapter = adapter
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)
        sortingSpinner = view.findViewById(R.id.spinner1)
        myDayButton = view.findViewById(R.id.myDay)
        nextDayButton = view.findViewById(R.id.nextDay)
        anyDayButton = view.findViewById(R.id.anyDay)
        taskRecyclerView = view.findViewById(R.id.task_recycler_view) as RecyclerView
        taskRecyclerView.layoutManager = LinearLayoutManager(context)
        taskRecyclerView.adapter = adapter

        myDayButton.setOnClickListener {myDayTasks()}
        nextDayButton.setOnClickListener{nextDayTasks()}
        val customDay = LocalDate()
        anyDayButton.setOnClickListener {
            DatePickerFragment.newInstance(customDay).apply {
                setTargetFragment(this@TaskListFragment, REQUEST_DATE)
                show(this@TaskListFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }

        context?.let { addItemsOnSpinner(it) };
        sortingSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                sorting(sortingSpinner.selectedItemPosition)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Do Nothing
            }
        }

        return view

    }


    fun sorting(x: Int){
        Log.i(TAG, "this is position: $x")
        when (x) {
            0 -> populatingTasks(taskListViewModel.getTasksSortedLevelAsc())
            1 -> populatingTasks(taskListViewModel.getTasksSortedLevelDesc())
            2 -> populatingTasks(taskListViewModel.getTasksSortedEtcAsc())
            3 -> populatingTasks(taskListViewModel.getTasksSortedEtcDesc())
            4 -> populatingTasks(taskListViewModel.getTasksSortedDateAsc())
            5 -> populatingTasks(taskListViewModel.getTasksSortedDateDesc())
            6 -> populatingTasks(taskListViewModel.tasksListLiveData)
        }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populatingTasks(taskListViewModel.tasksListLiveData)
    }

    fun myDayTasks(){
        var dateToday = LocalDate()
        val tasksWithDueDateToday: LiveData<List<Task>> = taskListViewModel.findTaskByDate(dateToday)
        populatingTasks(tasksWithDueDateToday)
    }

    fun nextDayTasks(){
        var dateTomorrow = LocalDate()
        dateTomorrow = dateTomorrow.plusDays(1)
        val tasksWithDueDateTomorrow: LiveData<List<Task>> = taskListViewModel.findTaskByDate(
            dateTomorrow
        )
        populatingTasks(tasksWithDueDateTomorrow)
    }

    fun populatingTasks(tasks: LiveData<List<Task>>){
        tasks.observe(
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
        private val etcIndicator: TextView = itemView.findViewById(R.id.ETCindicator)
        private val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.constraintLayoutList)

        init {
            itemView.setOnClickListener(this)
        }

        fun background(x: Int) {
            when(x){
                1->  backgroundGradient(Color.parseColor("#e64847"))
                2->  backgroundGradient(Color.parseColor("#fca041"))
                3->  backgroundGradient(Color.parseColor("#59eb00"))
                4->  backgroundGradient(Color.parseColor("#02e3ea"))
                else ->  backgroundGradient(Color.parseColor("#ffffff"))
            }
        }

        fun backgroundGradient(x: Int) {
            val customBar = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(x, x))
            customBar.cornerRadius = 15f
            customBar.setStroke(2, Color.parseColor("#000000"))
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                customBar.setPadding(0, 0, 0, 10)
            }
            constraintLayout.background = customBar
        }

        fun bind(task: Task) {
            this.task = task
            titleTextView.text = this.task.title
            var date = this.task.date
            dateTextView.text = date.toString("EEE, dd MMM")
            etcIndicator.text = this.task.ETC.toString()
            task.ECL?.let { background(it) }
            notesImgView.visibility = if (!task.notes.equals("")) { View.VISIBLE } else { View.GONE }
            if (task.completed) { titleTextView.apply { paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG } }
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

            override fun onDateSelected(date: LocalDate) {
                val chosenDay = date
                populatingTasks(taskListViewModel.findTaskByDate(chosenDay))
            }
}//TASK LIST FRAGMENT ENDS HERE