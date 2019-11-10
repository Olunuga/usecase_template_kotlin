package com.finitebits.doo.data.dRepositoryInterface

import com.finitebits.doo.data.Result
import com.finitebits.doo.data.model.Todo


/**
 * Interface to the data layer
 * The repository knows which data source to get the data from
 * */

interface TodoRepositoryInt {

    suspend fun getTodos(forceUpdate: Boolean = false): Result<List<Todo>>

    suspend fun getTodo(todoId: String, forceUpdate: Boolean = false): Result<Todo>

    suspend fun saveTodo(todo: Todo)

    suspend fun completeTodo(todoId: String)

    suspend fun completeTodo(todo: Todo)

    suspend fun activateTodo(todo: Todo)

    suspend fun deleteTodo(todo: Todo)
}