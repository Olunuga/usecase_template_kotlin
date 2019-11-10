package com.finitebits.doo.domain

import com.finitebits.doo.data.TodoRepository
import com.finitebits.doo.data.model.Todo

class CompleteTaskUseCase(private val todoRepository: TodoRepository) {
    suspend operator fun invoke(todo: Todo){
        return todoRepository.completeTodo(todo)
    }
}