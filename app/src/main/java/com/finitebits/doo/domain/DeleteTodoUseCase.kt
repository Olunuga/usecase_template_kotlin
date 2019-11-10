package com.finitebits.doo.domain

import com.finitebits.doo.data.TodoRepository
import com.finitebits.doo.data.model.Todo

class DeleteTodoUseCase(private val todoRepository: TodoRepository) {
    suspend operator fun invoke(todo: Todo){
        return todoRepository.deleteTodo(todo)
    }
}