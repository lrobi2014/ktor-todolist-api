package com.todolist.routes

import com.todolist.models.TodoListItemSerializable
import com.todolist.models.TodoListItems
import com.todolist.models.TodoListSerializable
import com.todolist.models.TodoLists
import com.todolist.services.TodoListItemService
import com.todolist.services.TodoListService
import com.typesafe.config.ConfigFactory
import io.ktor.config.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.junit.BeforeClass
import kotlin.test.BeforeTest

open class BaseTodoRoutesTest {
    protected val todoListService = TodoListService()
    protected val todoListItemService = TodoListItemService()

    companion object {
        val engine = TestApplicationEngine(createTestEnvironment {
            config = HoconApplicationConfig(ConfigFactory.load("application.conf"))
        })

        @BeforeClass
        @JvmStatic fun setup() {
            engine.start(wait = false)
        }
    }

    @BeforeTest
    fun before() = runBlocking {
        newSuspendedTransaction {
            TodoLists.deleteAll()
            TodoListItems.deleteAll()
            Unit
        }
    }

    protected fun addTodoList(todoListSerializable: TodoListSerializable): TodoListSerializable {
        return todoListService.createList(todoListSerializable)
    }

    protected fun addTodoListItem(id: Int, todoListItemSerializable: TodoListItemSerializable): TodoListItemSerializable? {
        return todoListItemService.createItem(id, todoListItemSerializable)
    }
}