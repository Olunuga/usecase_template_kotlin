package com.finitebits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.finitebits.doo.data.TodoRepository
import com.finitebits.doo.domain.DeleteTodoUseCase
import com.finitebits.doo.domain.GetTodosUseCase
import com.finitebits.doo.domain.SaveTodoUseCase
import com.finitebits.doo.ui.todos.TodosViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(private val todoRepository: TodoRepository) :
ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T  = with(modelClass){
        when{
            isAssignableFrom(TodosViewModel::class.java) -> TodosViewModel(
                GetTodosUseCase(todoRepository),
                SaveTodoUseCase(todoRepository),
                DeleteTodoUseCase(todoRepository)
            )
            else -> throw IllegalArgumentException("Unknown ViewModel")
        }
    } as T
}