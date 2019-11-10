package com.finitebits.doo.util

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.finitebits.ViewModelFactory
import com.finitebits.doo.DooApplication
import com.finitebits.doo.ui.todos.Todos


fun Todos.getViewModelFactory(context : Context): ViewModelFactory {
    val todoRepository = (context.applicationContext as DooApplication).todoRepository
    return ViewModelFactory(todoRepository)
}