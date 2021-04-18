package com.example.todolist

import android.text.Editable

import android.text.TextWatcher


abstract class TextChangeListener<T>(private val target: T) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        this.onTextChanged(target, s)
    }
    override fun afterTextChanged(s: Editable) {
        this.afterTextChanged(target, s);
    }

    abstract fun onTextChanged(target: T, s: CharSequence)
    abstract fun afterTextChanged(target: T,s: Editable)
}