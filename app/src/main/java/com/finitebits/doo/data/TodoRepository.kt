package com.finitebits.doo.data

import com.finitebits.doo.data.Result.Success
import com.finitebits.doo.data.Result.Error
import com.finitebits.doo.data.model.Todo
import com.finitebits.doo.data.dDataSourceInterface.TodoDataSource
import com.finitebits.doo.data.dRepositoryInterface.TodoRepositoryInt
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.Exception
import java.lang.IllegalStateException
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 *
 * Concrete Implementation of Todos Repository
* */

class TodoRepository(private val todoRemoteDataSource: TodoDataSource,
                     private val todoLocalDataSource : TodoDataSource,
                     private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): TodoRepositoryInt {

    private var cachedTodos : ConcurrentMap<String,Todo>? = null

    /********************* GET TODOS **************************************/

    override suspend fun getTodos(forceUpdate: Boolean): Result<List<Todo>> {
        return withContext(ioDispatcher){

            if(!forceUpdate){
                cachedTodos?.let {
                    cachedTodos -> return@withContext Success(cachedTodos.values.sortedBy { it.id })
                }
            }

            //If it gets here, that means cache is null, fetch from remote or local source
            val newTasksResult = fetchTasksFromRemoteOrLocal(forceUpdate)

            //Refresh the cache with new tasks
            (newTasksResult as? Success)?.let { refreshCache(it.data) }

            cachedTodos?.values?.let { todos ->
                return@withContext  Success(todos.sortedBy {it.id})
            }

            (newTasksResult as? Success)?.let {
                if(it.data.isEmpty()){
                    return@withContext Success(it.data)
                }
            }

            return@withContext Error(Exception("Illegal state"))
        }

    }

    //Get Todos Helpers
    private suspend fun fetchTasksFromRemoteOrLocal(forceUpdate: Boolean): Result<List<Todo>> {
        //Remote first
        when(val remoteTodosResult = todoRemoteDataSource.getTodos()){
            is Error -> Timber.w("Remote data source fetch failed")
            is Success -> {
                refreshLocalDataSource(remoteTodosResult.data)
                return remoteTodosResult
            }
            else -> throw IllegalStateException()

        }

        //Don't read from local data source if forced
        if(forceUpdate){
            return Error(Exception("Can't force refresh: remote dataSource is unavailable"))
        }

        //Local if remote fails
        val localTodosResult = todoLocalDataSource.getTodos()
        if(localTodosResult is Success) return  localTodosResult
        return Error(Exception("Error fetching from remote and local"))
    }

    private fun refreshCache(todos: List<Todo>){
        cachedTodos?.clear()
        todos.sortedBy { it.id }.forEach {
            cacheAndPerform(it) {}
        }
    }

    private suspend fun refreshLocalDataSource(todos: List<Todo>) {
        todoLocalDataSource.deleteAllTodo()
        for(todo in todos){
            todoLocalDataSource.saveTodo(todo)
        }
    }

    private inline fun cacheAndPerform(todo: Todo, perform: (Todo) -> Unit ){
        val cachedTodo = cacheTodo(todo)
        perform(cachedTodo)
    }

    private fun cacheTodo(todo: Todo): Todo{
        val cachedTodo = Todo(todo.id, todo.title)

        //create if it doesn't exists
        if(cachedTodos == null){
            cachedTodos = ConcurrentHashMap()
        }

        cachedTodos?.put(cachedTodo.id, cachedTodo)
        return cachedTodo
    }
    /*** END ***/


    /********************* GET TODOO **************************************/
    override suspend fun getTodo(todoId: String, forceUpdate: Boolean): Result<Todo> {
       return withContext(ioDispatcher){

           //respond immediately with cache if available
           if(!forceUpdate){
               getTaskWithId(todoId)?.let{
                   return@withContext Success(it)
               }
           }

           val newTodoResult = fetchTaskFromRemoteOrLocal(todoId,forceUpdate)

           //Refresh the cache with new task
           (newTodoResult as? Success)?.let {
               cacheTodo(it.data)
           }

           return@withContext newTodoResult
       }
    }

    private suspend fun fetchTaskFromRemoteOrLocal(todoId: String, forceUpdate: Boolean): Result<Todo> {
       //Remote first
        when(val remoteTodoResult = todoRemoteDataSource.getTodo(todoId)){
            is Error -> Timber.w("Remote data source fetch failed")
            is Success -> {
                refreshLocalDataSource(remoteTodoResult.data)
            }
            else -> throw IllegalStateException()
        }


        //Don't read from local if forced
        if(forceUpdate){
            return Error(Exception("Refresh failed"))
        }

        //Local if remote fails
        val localTodoResult = todoLocalDataSource.getTodo(todoId)
        if(localTodoResult is Success) return  localTodoResult
        return Error(Exception("Error fetching from remote and local"))

    }

    private suspend fun refreshLocalDataSource(todo: Todo) {
        todoLocalDataSource.saveTodo(todo)
    }


    private fun getTaskWithId(todoId: String)  = cachedTodos?.get(todoId)

    /*** END ***/



    override suspend fun saveTodo(todo: Todo) {
       cacheAndPerform(todo){
           coroutineScope {
              launch { todoRemoteDataSource.saveTodo(todo)}
               launch { todoLocalDataSource.saveTodo(todo)}
           }
       }
    }

    override suspend fun completeTodo(todo: Todo) {
        todo.isCompleted = true
        cacheAndPerform(todo){
            coroutineScope{
                launch { todoRemoteDataSource.completeTodo(it.id)}
                launch { todoLocalDataSource.completeTodo(it.id)}
            }
        }
    }

    override suspend fun completeTodo(todoId: String) {
        withContext(ioDispatcher){
            getTaskWithId(todoId)?.let {
                completeTodo(it)
            }
        }

    }

    override suspend fun activateTodo(todo: Todo) {
        todo.isCompleted = false
        cacheAndPerform(todo){
            coroutineScope{
                launch { todoRemoteDataSource.activateTodo(it.id)}
                launch { todoLocalDataSource.activateTodo(it.id)}
            }
        }

    }

    override suspend fun deleteTodo(todo: Todo) {
        coroutineScope{
            launch { todoRemoteDataSource.deleteTodo(todo.id)}
            launch { todoLocalDataSource.deleteTodo(todo.id)}
        }

        cachedTodos?.remove(todo.id)

    }
}