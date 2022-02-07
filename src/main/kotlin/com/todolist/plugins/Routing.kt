package com.todolist.plugins

import com.todolist.routes.todoListRouting
import com.todolist.response.APIError
import com.todolist.response.APIResponse
import com.todolist.routes.todoItemRouting
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*

fun Application.configureRouting() {
    install(StatusPages) {
        status(HttpStatusCode.NotFound) {
            val errors = listOf(APIError(it.description, it.value, "The requested resource was not found"))
            call.respond(HttpStatusCode.NotFound, APIResponse(null, errors))
        }

        exception<BadRequestException> { cause ->
            val errors = listOf(APIError(HttpStatusCode.BadRequest.description, HttpStatusCode.BadRequest.value, cause.message ?: "Invalid request"))
            call.respond(HttpStatusCode.BadRequest, APIResponse(null, errors))
        }
    }

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }

    routing {
        todoListRouting()
        todoItemRouting()
    }
}
