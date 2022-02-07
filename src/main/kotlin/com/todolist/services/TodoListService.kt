package com.todolist.services

import com.todolist.models.*
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.transactions.transaction

class TodoListService {
    fun getAllLists(): List<TodoListSerializable> {
        return transaction {
            val ls = TodoList.all().with(TodoList::items, TodoListItem::list).sortedByDescending { it.dateCreated }

            ls.map {
                it.toTodoListSerializable(
                    it.items.map {
                        item -> item.toTodoListItemSerializable()
                    }
                )
            }
        }

    }

    fun getSingleList(id: Int): TodoListSerializable? {
        return transaction {
            val ls = TodoList.findById(id)?.load(TodoList::items, TodoListItem::list)

            if (ls === null) {
                return@transaction null
            }

            val serializableItems = ls.items.map { it.toTodoListItemSerializable() }
            ls.toTodoListSerializable(serializableItems)
        }
    }

    fun createList(todoList: TodoListSerializable): TodoListSerializable {
        val itemService = TodoListItemService()

        val list = transaction {
            TodoList.new {
                name = todoList.name
                archived = todoList.archived
                user = todoList.user
                dateCreated = System.currentTimeMillis()
            }
        }

       val listItems = mutableListOf<TodoListItemSerializable>()

        // List<TodoListItemSerializable>
       todoList.items.map {
           if (it != null) {
               listItems.add(itemService.createItem(list.id.value, it)!!)
           }
       }

        transaction { list.refresh(true) }
        return list.toTodoListSerializable(listItems)
    }

    fun updateList(todoList: TodoListSerializable): TodoListSerializable? {
        return transaction {
            val ls = TodoList.findById(todoList.id)?.load(TodoList::items)
            if (ls === null) {
                return@transaction null
            }

            ls.name = todoList.name
            ls.archived = todoList.archived

            val serializableItems = ls.items.map { it.toTodoListItemSerializable() }
            ls.toTodoListSerializable(serializableItems)
        }
    }

    fun deleteList(listID: Int) {
        return transaction {
            TodoList.findById(listID)?.delete()
        }
    }
}