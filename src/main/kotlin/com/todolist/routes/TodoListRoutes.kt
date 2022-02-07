package com.todolist.routes

import com.todolist.models.TodoListSerializable
import com.todolist.response.*
import com.todolist.services.TodoListService
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.todoListRouting() {
    val todoListService = TodoListService()
    route("/lists") {
        get {
            val lists = todoListService.getAllLists()

            call.respond(APIResponse(APIResult(lists), null))
        }

        get("{id}") {
            val id = call.parameters["id"]?.toInt()
            val list = id?.let { it1 -> todoListService.getSingleList(id = it1) }

            if (list === null) {
                throw NotFoundException()
            }

            call.respond(APISingleResponse(APISingleResult(list), null))
        }

        post {
            val list = call.receive<TodoListSerializable>()

            val createdList = todoListService.createList(list)

            call.response.header("Location", "/lists/${createdList.id}")
            call.respond(HttpStatusCode.Created, APISingleResponse(APISingleResult(createdList), null))
        }

        put {
            val list = call.receive<TodoListSerializable>()

            val updatedList = todoListService.updateList(list)

            if (updatedList === null) {
                throw NotFoundException()
            } else call.respond(APISingleResponse(APISingleResult(updatedList), null))
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw NotFoundException()

            todoListService.deleteList(id)

            call.respond(HttpStatusCode.OK, APIResponse(null, null))
        }
    }

}