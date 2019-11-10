package com.finitebits.doo.data.model
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


/**
 * Immutable model class for a Task.
 *
 * @param title       title of the task
 * @param description description of the task
 * @param isCompleted whether or not this task is completed
 * @param id          id of the task
 */

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey @ColumnInfo(name = "entryId") var id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name="title") var title: String = "",
    @ColumnInfo(name="completed") var isCompleted: Boolean = false
){
    val isActive
     get() = !isCompleted

    val isEmpty
        get() = title.isEmpty()
}