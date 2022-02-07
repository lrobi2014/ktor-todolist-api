package com.todolist.services

import com.todolist.models.*
import org.jetbrains.exposed.sql.transactions.transaction

class TodoListItemService {

    fun createItem(todoListId: Int, todoListItem: TodoListItemSerializable): TodoListItemSerializable? {
        val todoList = transaction {
            TodoList.findById(todoListId)
        }

        if (todoList === null) {
            return null;
        }

        return transaction {
            val item = TodoListItem.new {
                name = todoListItem.name
                list =  todoList
                status = ItemStatus.valueOf(todoListItem.status.uppercase())
                dateCreated = System.currentTimeMillis()
            }

            item.toTodoListItemSerializable()
        }
    }

    fun getAllItemsForList(listId: Int): List<TodoListItemSerializable>? {
        return transaction {
           TodoList.findById(listId)?.items?.map {
               it.toTodoListItemSerializable()
           }
        }
    }
}