package com.example.keepfit_kotlin

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.IndexOutOfBoundsException

object Utils {

    fun safeInt(text: String, fallback: Int): Int {
        return text.toIntOrNull() ?: fallback
    }

    fun getDateString(year: Int, month: Int, day: Int): String {
        return if (month < 9 && day <= 9)
            "0${day}0${month + 1}$year"
        else if (month < 9)
            "${day}0${month + 1}$year"
        else if (day <= 9)
            "0${day}${month + 1}$year"
        else
            "${day}${month + 1}$year"
    }

    fun <T> LiveData<T>.observeForeverOnce(observer: Observer<T>) {
        observeForever(object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }
}

class LinearLayoutManagerWrapper(context: Context): LinearLayoutManager(context) {

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch(e: IndexOutOfBoundsException) {
            return
        }
    }
}