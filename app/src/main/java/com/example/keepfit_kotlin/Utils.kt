package com.example.keepfit_kotlin

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.IndexOutOfBoundsException

object Utils {

    val MONTHS = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

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

    fun getFormattedDate(dateStr: String): String {
        return "${dateStr.substring(0, 2)} ${MONTHS[dateStr.substring(2, 4).toInt() - 1]} ${dateStr.substring(4, 8)}"
    }

    fun <T> LiveData<T>.observeOnceNoLC(observer: Observer<T>) {
        observeForever(object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

    fun Spinner.selected(action: (position:Int) -> Unit) {
        this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                action(position)
            }
        }
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