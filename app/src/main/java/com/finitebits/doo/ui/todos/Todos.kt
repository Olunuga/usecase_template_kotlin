package com.finitebits.doo.ui.todos

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.finitebits.doo.R
import com.finitebits.doo.data.model.Todo
import com.finitebits.doo.util.getViewModelFactory
import timber.log.Timber


class Todos : AppCompatActivity() {
    private var  viewModel : TodosViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       viewModel =  getViewModelFactory(this).create(TodosViewModel::class.java)
        viewModel?.saveTodo(Todo(title = "from me"))
        viewModel?.loadTodo(true)
    }


    override fun onStart() {
        super.onStart()
        val textView : TextView = findViewById(R.id.tvText)
        val textView2 : TextView = findViewById(R.id.tvText2)

        viewModel?.dataLoading?.observe(this, Observer {
            Timber.d("Value of data loading $it")
            textView.text = if(it) "loading..." else ""
        })

        viewModel?.todos?.observe(this, Observer {
            Timber.d("Value of todos ${it?.toString()}")
            textView2.text = if (it != null && it.isEmpty()) "" else  composeTodos(it)
        })
    }

    private fun composeTodos(todoList : List<Todo>): String? {
            var todos = ""
            for (todo in todoList){
                todos += "${todo.title}\n"
            }
        return todos
    }
}
