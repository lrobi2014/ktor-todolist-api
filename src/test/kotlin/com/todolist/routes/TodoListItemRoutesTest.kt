package com.todolist.routes

import com.todolist.models.ItemStatus
import com.todolist.models.TodoListItemSerializable
import com.todolist.models.TodoListSerializable
import io.kotest.assertions.json.shouldContainJsonKey
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.assertions.ktor.shouldHaveStatus
import io.kotest.matchers.string.shouldNotBeEmpty
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test

class TodoListItemRoutesTest: BaseTodoRoutesTest() {
    @Test
    fun testGetAll() {
        val list = addTodoList(TodoListSerializable(name = "Test List", archived = false, user = "testuser"))

        addTodoListItem(
            list.id,
            TodoListItemSerializable(
                name = "Item 1",
                status = ItemStatus.ACTIVE.toString()
            )
        )

        addTodoListItem(
            list.id,
            TodoListItemSerializable(
                name = "Item 1",
                status = ItemStatus.ACTIVE.toString()
            )
        )

        with(engine) {
            handleRequest(HttpMethod.Get, "/lists/${list.id}/items").apply {
                response.shouldHaveStatus(HttpStatusCode.OK)
                response.content.shouldNotBeEmpty()
                response.content.shouldContainJsonKey("$.data.entries")
                response.content.shouldContainJsonKey("$.data.entries[:1].name")
                response.content.shouldContainJsonKey("$.data.entries[:2].name")
            }
        }
    }

    @Test
    fun testGetAll_withUnknownList() {
        val expectedResponse = """
            {
                "data": {
                    "entries":null
                },
                "errors": null
            }
        """.trimIndent()

        with(engine) {
            handleRequest(HttpMethod.Get, "/lists/3/items").apply {
                response.shouldHaveStatus(HttpStatusCode.OK)
                response.content?.shouldEqualJson(expectedResponse)
            }
        }
    }
}