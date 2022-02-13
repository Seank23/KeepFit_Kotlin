package com.example.keepfit_kotlin

object Utils {

    fun safeInt(text: String, fallback: Int): Int {
        return text.toIntOrNull() ?: fallback
    }
}