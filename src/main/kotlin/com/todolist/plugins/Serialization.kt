package com.todolist.plugins

import com.todolist.models.SerializableEntity
import com.todolist.models.TodoListItemSerializable
import com.todolist.models.TodoListSerializable
import io.ktor.serialization.*
import io.ktor.features.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import kotlinx.serialization.ContextualSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@ExperimentalSerializationApi
fun Application.configureSerialization() {
    val module = SerializersModule {
        polymorphic(SerializableEntity::class) {
            subclass(TodoListSerializable::class, TodoListSerializable.serializer())
            subclass(TodoListItemSerializable::class, TodoListItemSerializable.serializer())
        }
    }

    install(ContentNegotiation) {
        json(
            Json {
                serializersModule = module
                encodeDefaults = true
            }
        )
    }

    routing {
        get("/json/kotlinx-serialization") {
                call.respond(mapOf("hello" to "world"))
            }
    }
}
