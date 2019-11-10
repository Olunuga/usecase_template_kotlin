package com.finitebits.doo.ui.todos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finitebits.doo.data.Result.Success
import com.finitebits.doo.data.model.Todo
import com.finitebits.doo.domain.DeleteTodoUseCase
import com.finitebits.doo.domain.GetTodosUseCase
import com.finitebits.doo.domain.SaveTodoUseCase
import kotlinx.coroutines.launch

class TodosViewModel(
    private val getTodosUseCase: GetTodosUseCase,
    private val saveTodoUseCase: SaveTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase
) : ViewModel() {

    private val _todos = MutableLiveData<List<Todo>>().apply { value = emptyList() }
    val todos : LiveData<List<Todo>> = _todos

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading = _dataLoading


    init {
        loadTodo(true)
    }

    fun loadTodo(forceUpdate: Boolean) {
      _dataLoading.value = true
        viewModelScope.launch {
            val todoResults = getTodosUseCase(true)

            if(todoResults is Success){
                _todos.value = todoResults.data
            }else{
                //Todo: handle error case here to show proper error view.
                _todos.value = emptyList()
            }
            _dataLoading.value = false
        }
    }

    fun saveTodo(todo: Todo){
        viewModelScope.launch {
            saveTodoUseCase(todo)
            loadTodo(true)
        }
    }

    fun deleteTodo(todo: Todo){
        viewModelScope.launch {
            deleteTodoUseCase(todo)
            loadTodo(true)
        }
    }
}