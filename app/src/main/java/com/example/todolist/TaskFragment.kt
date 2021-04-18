package com.example.todolist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import java.util.*

private const val TAG = "TaskFragment"
private const val TASK_ID_KEY = "task_id_key"
private const val DIALOG_DATE = "dialog_date"
private const val REQUEST_DATE = 0
class TaskFragment: Fragment(), DatePickerFragment.Listener {
    private lateinit var task: Task

    private lateinit var tasktitle: EditText
    private lateinit var taskNotes: EditText
    private lateinit var taskLevel: EditText
    private lateinit var dateButton: Button
    private lateinit var myDayButton: Button
    private lateinit var completedCheckBox: CheckBox


    private val taskDetailViewModel: TaskDetailViewModel by lazy {
        ViewModelProviders.of(this).get(TaskDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        task = Task()
        val taskId = arguments?.getSerializable(TASK_ID_KEY) as UUID
        Log.d(TAG, "Retrieved task id in onCreate: $taskId")
        taskDetailViewModel.loadTask(taskId)
    }

    override fun onDateSelected(date: Date) {
        task.date = date
        updateUI()
    }

    private fun updateUI() {
        tasktitle.setText(task.title)
        dateButton.text = task.date.toString()
        completedCheckBox.apply {
            isChecked = task.completed
            jumpDrawablesToCurrentState()
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstance: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_task, container, false)

        tasktitle = view.findViewById(R.id.editTextTitle) as EditText
        dateButton = view.findViewById(R.id.button) as Button
        completedCheckBox = view.findViewById(R.id.checkBox) as CheckBox
        taskNotes = view.findViewById(R.id.editTextTime) as EditText
        taskLevel = view.findViewById(R.id.eisenhowerNumber) as EditText
        myDayButton = view.findViewById(R.id.mydaybutton) as Button
        dateButton.apply {
            text = task.date.toString()
            isEnabled = true
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(tag, "called onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        taskDetailViewModel.taskLiveData.observe(
                viewLifecycleOwner,
                androidx.lifecycle.Observer { task ->
                    task?.let {
                        this.task = task
                        updateUI()
                    }
                }
        )
    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(sequence: Editable?) {}
            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                task.title = sequence.toString()
            }
        }

        val noteWatcher = object : TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(sequence: Editable?) {}
            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                task.notes = sequence.toString()
            }
        }

        val levelWatcher = object : TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(sequence: Editable?) {}
            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                val temp = sequence.toString()
                task.ECL = Integer.parseInt(temp)
            }
        }

        tasktitle.addTextChangedListener(titleWatcher)
        taskNotes.addTextChangedListener(noteWatcher)
        taskLevel.addTextChangedListener(levelWatcher)

        completedCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked ->
                task.completed = isChecked
                Log.i(TAG, "Task Completed: $task.completed")
            }
        }

            myDayButton.setOnClickListener {
                if(myDayButton.text.equals("Add to My Day"))
                {
                    task.date = Date()
                    myDayButton.text = "Added to My Day"
                    dateButton.text = task.date.toString()
                }
                 else if(myDayButton.text.equals("Added to My Day")){
                     task.date = Date(0, 0, 0)
                    myDayButton.text = "Add to My Day"
                    dateButton.text = "Add Due Date"
                }
            }

            dateButton.setOnClickListener {
                DatePickerFragment.newInstance(task.date).apply {
                    setTargetFragment(this@TaskFragment, REQUEST_DATE)
                    show(this@TaskFragment.requireFragmentManager(), DIALOG_DATE)
                }
            }
    }
        override fun onStop() {
            super.onStop()
            taskDetailViewModel.saveTask(task)
        }

        companion object {
            fun newInstance(crimeId: UUID): TaskFragment {
                return TaskFragment().apply {
                    arguments = newArguments(crimeId)
                }
            }

            @VisibleForTesting
            fun newArguments(crimeId: UUID) = Bundle().apply {
                putSerializable(TASK_ID_KEY, crimeId)
            }
        }

    }