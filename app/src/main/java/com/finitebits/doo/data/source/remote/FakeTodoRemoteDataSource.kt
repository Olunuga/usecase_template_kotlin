package com.finitebits.doo.data.source.remote

import com.finitebits.doo.data.Result
import com.finitebits.doo.data.Result.Success
import com.finitebits.doo.data.Result.Error
import com.finitebits.doo.data.dDataSourceInterface.TodoDataSource
import com.finitebits.doo.data.model.Todo
import java.lang.Exception
import java.util.LinkedHashMap

object FakeTodoRemoteDataSource : TodoDataSource {


    private var TASKS_SERVICE_DATA: LinkedHashMap<String, Todo> = LinkedHashMap()

    override suspend fun getTodos(): Result<List<Todo>> {
        return Success(TASKS_SERVICE_DATA.values.toList())
    }

    override suspend fun getTodo(todoId: String): Result<Todo> {
        TASKS_SERVICE_DATA[todoId]?.let {
            return Success(it)
        }
        return Error(Exception("Could not get todo"))
         }

    override suspend fun saveTodo(todo: Todo) {
        TASKS_SERVICE_DATA[todo.id] = todo
           }

    override suspend fun completeTodo(todoId: String) {
        // Not required for the remote data source.
         }

    override suspend fun completeTodo(todo: Todo) {
        val completedTodo = Todo(todo.id, title = todo.title, isCompleted = true)
        TASKS_SERVICE_DATA[todo.id] = completedTodo
        }

    override suspend fun activateTodo(todo: Todo) {
        val completedTodo = Todo(todo.id, title = todo.title, isCompleted = false)
        TASKS_SERVICE_DATA[todo.id] = completedTodo
        }

    override suspend fun activateTodo(todoId: String) {
        // Not required for the remote data source.
        }

    override suspend fun deleteTodo(todoId: String) {
            TASKS_SERVICE_DATA.remove(todoId)
        }

    override suspend fun deleteAllTodo() {
        TASKS_SERVICE_DATA.clear()
    }
}