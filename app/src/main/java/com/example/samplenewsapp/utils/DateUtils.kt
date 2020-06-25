package com.example.samplenewsapp.utils

import java.text.SimpleDateFormat
import java.util.*

val locale = Locale("in", "ID")

fun String.removeInstant(): String {
    return this.replace("T", " ").replace("Z","")
}

fun String.toDate(format: String): Date {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.parse(this)!!
}

fun Date.toString(format: String): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}