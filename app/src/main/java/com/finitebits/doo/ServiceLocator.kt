package com.finitebits.doo

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.finitebits.doo.data.TodoRepository
import com.finitebits.doo.data.source.local.TodoDatabase
import com.finitebits.doo.data.source.local.TodoLocalDataSource
import com.finitebits.doo.data.source.remote.FakeTodoRemoteDataSource
import kotlinx.coroutines.runBlocking

object ServiceLocator {
    private val lock = Any()
    private var database : TodoDatabase ? = null

    @Volatile
    var todoRepository : TodoRepository ? = null


    @VisibleForTesting set
    fun provideTodoRepository(context: Context): TodoRepository {
        synchronized(this){
            return todoRepository?: todoRepository?: createTodoRepository(context)
        }
    }

    private fun createTodoRepository(context: Context): TodoRepository {
      return TodoRepository(FakeTodoRemoteDataSource,createTodoLocalDataSource(context))
    }

    private fun createTodoLocalDataSource(context: Context): TodoLocalDataSource {
        val database = database ?: createDataBase(context)
        return TodoLocalDataSource(database.todoDao())
    }

    private fun createDataBase(context: Context): TodoDatabase {
       val result = Room.databaseBuilder(context.applicationContext,
           TodoDatabase::class.java,
            "Todos.db"
           ).build()
        database = result
        return result
    }


    @VisibleForTesting
    fun resetRespository(){
        synchronized(lock){
            runBlocking {
                FakeTodoRemoteDataSource.deleteAllTodo()
            }

            // Clear all data to avoid test pollution.
            database?.apply {
                clearAllTables()
                close()
            }

            database = null
            todoRepository = null
        }
    }
}