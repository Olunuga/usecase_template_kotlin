package com.finitebits.doo.data.source.local

import androidx.room.*
import com.finitebits.doo.data.model.Todo


@Dao
interface TodoDao {

    /**
     * Select all todos from the todos table.
     *
     * @return all todos.
     */

    @Query("SELECT * FROM todos")
    suspend fun getTodos() : List<Todo>


    /**
     * Select a todoo by id.
     *
     * @param todoId the todoo id.
     * @return the todoo with todoId.
     */
    @Query("select * from Todos where entryId = :todoId")
    suspend fun getTodoById(todoId: String) : Todo?


    /**
     * Insert a todoo in the database. If the todoo already exists, replace it.
     *
     * @param todoo the todoo to be inserted.
     */
    @Insert( onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: Todo)


    /**
     * Update a todoo.
     *
     * @param todoo todoo to be updated
     * @return the number of todoos updated. This should always be 1.
     */
    @Update
    suspend fun updateTodo(todo: Todo): Int

    /**
     * Update the complete status of a todoo
     *
     * @param todooId    id of the todoo
     * @param completed status to be updated
     */
    @Query("Update todos set completed = :completed where entryId = :todoId ")
    suspend fun updateCompleted(todoId: String, completed: Boolean)


    /**
     * Delete a todoo by id.
     *
     * @return the number of todoos deleted. This should always be 1.
     */
    @Query("delete from todos where entryId = :todoId")
    suspend fun deleteTodoById(todoId: String): Int


    /**
     * Delete all todos.
     */
    @Query("delete from todos")
    suspend fun deleteTodos()


    /**
     * Delete all completed todos from the table.
     *
     * @return the number of todos deleted.
     */
    @Query("delete from todos where completed = 1")
    suspend fun deleteCompletedTodos() : Int

}