package com.finitebits.doo.util

import androidx.fragment.app.Fragment
import com.finitebits.ViewModelFactory
import com.finitebits.doo.DooApplication

fun Fragment.getViewModelFactory(): ViewModelFactory {
    val todoRepository = (requireContext().applicationContext as DooApplication).todoRepository
    return ViewModelFactory(todoRepository)
}