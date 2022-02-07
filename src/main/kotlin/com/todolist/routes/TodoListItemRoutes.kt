package com.todolist.routes

import com.todolist.models.TodoListSerializable
import com.todolist.response.APIResponse
import com.todolist.response.APIResult
import com.todolist.response.APISingleResponse
import com.todolist.response.APISingleResult
import com.todolist.services.TodoListItemService
import com.todolist.services.TodoListService
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.todoItemRouting() {
    val todoListItemService= TodoListItemService()

    route("/lists/{listId}/items") {
        get {
            val listId = call.parameters["listId"]?.toInt()

            if (listId === null) {
                throw NotFoundException()
            }

            val items = todoListItemService.getAllItemsForList(listId)

            call.respond(APIResponse(APIResult(items), null))
        }

    }
//        get("{id}") {
//            val id = call.parameters["id"]?.toInt()
//            val list = id?.let { it1 -> todoListItemService.getSingleList(id = it1) }
//
//            if (list === null) {
//                throw NotFoundException()
//            }
//
//            call.respond(APISingleResponse(APISingleResult(list), null))
//        }
//
//        post {
//            val list = call.receive<TodoListSerializable>()
//
//            val createdList = todoListService.createList(list)
//
//            call.response.header("Location", "/lists/${createdList.id}")
//            call.respond(HttpStatusCode.Created, APISingleResponse(APISingleResult(createdList), null))
//        }
//
//        put {
//            val list = call.receive<TodoListSerializable>()
//
//            val updatedList = todoListService.updateList(list)
//
//            if (updatedList === null) {
//                throw NotFoundException()
//            } else call.respond(APISingleResponse(APISingleResult(updatedList), null))
//        }
//
//        delete("{id}") {
//            val id = call.parameters["id"]?.toInt() ?: throw NotFoundException()
//
//            todoListService.deleteList(id)
//
//            call.respond(HttpStatusCode.OK, APIResponse(null, null))
//        }
//    }

}