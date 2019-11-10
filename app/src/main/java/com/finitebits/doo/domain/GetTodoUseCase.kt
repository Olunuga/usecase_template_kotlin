package com.finitebits.doo.domain

import com.finitebits.doo.data.TodoRepository
import com.finitebits.doo.data.model.Todo
import com.finitebits.doo.data.Result

class GetTodoUseCase(private val todoRepository: TodoRepository) {
    suspend operator fun invoke(todoId: String, forceUpdate: Boolean): Result<Todo>{
        return todoRepository.getTodo(todoId,forceUpdate)
    }
}