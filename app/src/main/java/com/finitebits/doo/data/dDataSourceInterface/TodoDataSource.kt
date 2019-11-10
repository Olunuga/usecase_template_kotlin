package com.finitebits.doo.data.dDataSourceInterface
import com.finitebits.doo.data.Result
import com.finitebits.doo.data.model.Todo

/**
* Main entry point for accessing task data
* */

interface TodoDataSource {

    suspend fun getTodos(): Result<List<Todo>>

    suspend fun getTodo(todoId: String): Result<Todo>

    suspend fun saveTodo(todo: Todo)

    suspend fun completeTodo(todoId: String)

    suspend fun completeTodo(todo: Todo)

    suspend fun activateTodo(todoId: String)

    suspend fun activateTodo(todo: Todo)

    suspend fun deleteTodo(todoId: String)

    suspend fun deleteAllTodo()
}