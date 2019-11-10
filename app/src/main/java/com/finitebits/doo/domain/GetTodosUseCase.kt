package com.finitebits.doo.domain

import com.finitebits.doo.data.TodoRepository
import com.finitebits.doo.data.model.Todo
import com.finitebits.doo.data.Result
import com.finitebits.doo.data.Result.Success
import com.finitebits.doo.util.enums.TodoFilteringType
import com.finitebits.doo.util.enums.TodoFilteringType.ALL_TODOS
import com.finitebits.doo.util.enums.TodoFilteringType.ACTIVE_TODOS
import com.finitebits.doo.util.enums.TodoFilteringType.COMPLETED_TODOS


class GetTodosUseCase (private val todoRepository: TodoRepository){
    suspend operator fun invoke(
        forceUpdate: Boolean = false,
        currentFiltering : TodoFilteringType = ALL_TODOS
    ): Result<List<Todo>> {
        val todoResults = todoRepository.getTodos(forceUpdate)

        //filter task
        if(todoResults is Success && currentFiltering != ALL_TODOS){
            val todos = todoResults.data
            val todosToShow = mutableListOf<Todo>()

            for(todo in todos){
                when(currentFiltering){
                    ACTIVE_TODOS -> if(todo.isActive){
                        todosToShow.add(todo)
                    }

                    COMPLETED_TODOS -> if(todo.isCompleted){
                        todosToShow.add(todo)
                    }
                    else -> NotImplementedError()
                }
            }
            return Success(todosToShow)
        }
        return todoResults
    }
}