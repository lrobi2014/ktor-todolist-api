package com.todolist.routes

import com.todolist.models.*
import io.kotest.assertions.json.shouldContainJsonKey
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.assertions.json.shouldEqualSpecifiedJson
import io.kotest.assertions.ktor.shouldHaveStatus
import io.kotest.matchers.string.shouldNotBeEmpty
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.math.absoluteValue

class TodoListRoutesTest: BaseTodoRoutesTest() {

    @Test
    fun testCreate_withItems() {
        with(engine) {
            handleRequest(HttpMethod.Post, "/lists") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(
                    Json.encodeToString(TodoListSerializable(
                        name = "Task List 1",
                        user = "testuser",
                        items = listOf(
                            TodoListItemSerializable(
                                name = "Item 1",
                                status = ItemStatus.ACTIVE.name.lowercase()
                            )
                        )
                    )))
            }.apply {
                response.shouldHaveStatus(201)
                response.content?.shouldContainJsonKey("$.data.entry.id")
                response.content?.shouldContainJsonKey("$.data.entry.items[:1].id")
            }
        }
    }

    @Test
    fun testCreate_withoutItems() {
        with(engine) {
            handleRequest(HttpMethod.Post, "/lists") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(
                    Json.encodeToString(
                        TodoListSerializable(
                            name = "Task List 1",
                            user = "testuser",
                            items = listOf()
                        )
                    )
                )
            }.apply {
                response.shouldHaveStatus(201)
                response.content?.shouldContainJsonKey("$.data.entry.id")
            }
        }
    }

    @Test
    fun testGetAll() {
        val list1 = addTodoList(TodoListSerializable(name = "Test List", archived = false, user = "testuser"))
        val list2 = addTodoList(TodoListSerializable(name = "Test List 2", archived = false, user = "testuser"))

        addTodoListItem(
            list1.id,
            TodoListItemSerializable(
                name = "Item 1",
                status = ItemStatus.ACTIVE.toString()
            )
        )

        addTodoListItem(
            list2.id,
            TodoListItemSerializable(
                name = "Item 1",
                status = ItemStatus.ACTIVE.toString()
            )
        )

        with(engine) {
            handleRequest(HttpMethod.Get, "/lists").apply {
                response.shouldHaveStatus(HttpStatusCode.OK)
                response.content.shouldNotBeEmpty()
                response.content.shouldContainJsonKey("$.data.entries")
                response.content.shouldContainJsonKey("$.data.entries[:1].name")
                response.content.shouldContainJsonKey("$.data.entries[:1].items[:1].name")
                response.content.shouldContainJsonKey("$.data.entries[:2].name")
                response.content.shouldContainJsonKey("$.data.entries[:2].items[:1].name")
            }
        }
    }

    @Test
    fun testGetOne() {
        val todoList = addTodoList(
            TodoListSerializable(
                name = "Test List",
                archived = false,
                user = "testuser"
            )
        )

        val todoList2 = addTodoList(
            TodoListSerializable(
                name = "Test List 2",
                archived = false,
                user = "testuser"
            )
        )

        addTodoListItem(
            todoList.id,
            TodoListItemSerializable(
                name = "Item 1",
                listId = todoList.id,
                status = ItemStatus.ACTIVE.toString()
            )
        )

        with(engine) {
            val expectedResponse = """
                {
                   "data":{
                      "entry":{
                         "id":${todoList2.id},
                         "name":"Grocery List 2022-02",
                         "archived":false,
                         "user":"testuser",
                         "items":[],
                         "dateCreated": ${todoList2.dateCreated.absoluteValue}
                      }
                   },
                   "errors":null
                }
            """.trimIndent()

            handleRequest(HttpMethod.Get, "/lists/${todoList2.id}").apply {
                response.shouldHaveStatus(HttpStatusCode.OK)
                response.content.shouldNotBeEmpty()
            }
        }
    }

    @Test
    fun testGetOne_withUnknownId() {
        val expectedResponse = """
            {
                "data": null,
                "errors":  [
                    {
                        "type": "Not Found",
                        "statusCode": 404,
                        "message": "The requested resource was not found"
                    }
                ]
            }
        """.trimIndent()
        with(engine) {
            handleRequest(HttpMethod.Get, "/lists/50").apply {
                response.shouldHaveStatus(404)
                response.content?.shouldEqualJson(expectedResponse)
            }
        }
    }

    @Test
    fun testUpdateList_withoutItems(){
        val todoList = addTodoList(
            TodoListSerializable(
                name = "Task List 1",
                archived = false,
                user = "testuser"
            )
        )

        val expectedResponse = """
                {
                   "data":{
                      "entry":{
                         "id":${todoList.id},
                         "name":"Task List Updated",
                         "archived":true,
                         "user":"testuser",
                         "items":[],
                         "dateCreated": ${todoList.dateCreated}
                      }
                   },
                   "errors":null
                }
            """
        with(engine) {
            handleRequest(HttpMethod.Put, "/lists") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(
                    Json.encodeToString(TodoListSerializable(
                        id = todoList.id,
                        name = "Task List Updated",
                        archived = true,
                        user = "testuser"
                    )))
            }.apply {
                response.shouldHaveStatus(200)
                response.content?.shouldEqualSpecifiedJson(expectedResponse)

            }
        }
    }

    @Test
    fun testUpdateList_withUnknownId() {
        val expectedResponse = """
            {
                "data": null,
                "errors":  [
                    {
                        "type": "Not Found",
                        "statusCode": 404,
                        "message": "The requested resource was not found"
                    }
                ]
            }
        """.trimIndent()
        with(engine) {
            handleRequest(HttpMethod.Put, "/lists") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(
                    Json.encodeToString(TodoListSerializable(
                        id = 1001,
                        name = "Task List 1",
                        archived = true,
                        user = "testuser",
                    )))
            }.apply {
                response.shouldHaveStatus(404)
                response.content?.shouldEqualJson(expectedResponse)
            }
        }
    }

    @Test
    fun testDeleteList_withoutId() {
        val expectedResponse = """
            {
                "data": null,
                "errors":  [
                    {
                        "type": "Not Found",
                        "statusCode": 404,
                        "message": "The requested resource was not found"
                    }
                ]
            }
        """.trimIndent()
        with(engine) {
            handleRequest(HttpMethod.Delete, "/lists").apply {
                response.shouldHaveStatus(404)
                response.content?.shouldEqualJson(expectedResponse)
            }
        }
    }

    @Test
    fun testDeleteList() {
        val todoList = addTodoList(TodoListSerializable(
            name = "Task List 1",
            archived = false,
            user = "testuser"
        ))

        val expectedResponse = """
           {
                "data": null,
                "errors": null
           }
        """.trimIndent()

        with(engine) {
            handleRequest(HttpMethod.Delete, "/lists/${todoList.id}").apply {
                response.shouldHaveStatus(200)
                response.content?.shouldEqualJson(expectedResponse)
            }
        }
    }
}