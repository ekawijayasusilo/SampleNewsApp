package com.example.samplenewsapp.utils

sealed class PageState<out T> {
    object Loading : PageState<Nothing>()
    class Render<T>(val renderState: T) : PageState<T>()
}