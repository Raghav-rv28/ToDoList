package com.example.todolist

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import org.joda.time.LocalDate
import java.util.*

private const val ARG_DATE = "arg_date"

class DatePickerFragment: DialogFragment() {
    interface Listener {
        fun onDateSelected(date: LocalDate)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dateListener = DatePickerDialog.OnDateSetListener {
            _, year, month, dayOfMonth ->
            val resultDate = LocalDate(year, month, dayOfMonth)
            targetFragment?.let { (it as Listener).onDateSelected(resultDate)  }
        }

        var calendar = arguments?.getSerializable(ARG_DATE) as LocalDate
        return DatePickerDialog(
                requireContext(),
                dateListener,
                calendar.year,
                calendar.monthOfYear,
                calendar.dayOfMonth
        )
    }

    companion object {
        fun newInstance(date: LocalDate): DatePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_DATE, date)
            }
            return DatePickerFragment().apply { arguments = args }
        }
    }

}