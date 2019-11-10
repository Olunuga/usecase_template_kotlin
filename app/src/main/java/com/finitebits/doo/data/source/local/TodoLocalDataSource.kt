package com.finitebits.doo.data.source.local

import com.finitebits.doo.data.Result
import com.finitebits.doo.data.Result.Success
import com.finitebits.doo.data.Result.Error
import com.finitebits.doo.data.dDataSourceInterface.TodoDataSource
import com.finitebits.doo.data.model.Todo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class TodoLocalDataSource internal constructor(
    private val todoDao: TodoDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
)
    : TodoDataSource {


    override suspend fun getTodos(): Result<List<Todo>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(todoDao.getTodos())
        }catch (e: Exception){
            Error(e)
        }
    }

    override suspend fun getTodo(todoId: String): Result<Todo>  = withContext(ioDispatcher){
        return@withContext try{
            val todo = todoDao.getTodoById(todoId)
            if(todo != null){
                return@withContext Success(todo)
            }else{
                return@withContext Error(Exception("Task not found"))
            }

        }catch (e : Exception){
            Error(e)
        }
    }

    override suspend fun saveTodo(todo: Todo) = withContext(ioDispatcher){
        todoDao.insertTodo(todo)
    }

    override suspend fun completeTodo(todoId: String) = withContext(ioDispatcher){
        todoDao.updateCompleted(todoId,true)
    }

    override suspend fun completeTodo(todo: Todo) {
        todoDao.updateCompleted(todo.id, completed = true)
       }

    override suspend fun activateTodo(todo: Todo) {
              todoDao.updateCompleted(todo.id, completed = false)
         }

    override suspend fun activateTodo(todoId: String) = withContext(ioDispatcher){
        todoDao.updateCompleted(todoId, false)
    }

    override suspend fun deleteTodo(todoId: String) = withContext<Unit>(ioDispatcher){
        todoDao.deleteTodoById(todoId)
    }

    override suspend fun deleteAllTodo() = withContext(ioDispatcher){
        todoDao.deleteTodos()
    }
}