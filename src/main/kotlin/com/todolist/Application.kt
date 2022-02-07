package com.todolist

import com.todolist.database.PostgresDb
import io.ktor.application.*
import com.todolist.plugins.*
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.core.context.startKoin

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@ExperimentalSerializationApi
@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureRouting()
    configureSerialization()


    val db = environment.config.config("ktor.database")
    val driverClass = db.property("driverClass").getString()
    val url = db.property("url").getString()
    val user = db.property("user").getString()
    val password = db.property("password").getString()
    val maxPoolSize = db.property("maxPoolSize").getString().toInt()

    PostgresDb.connect(DbConfig(driverClass, url, user, password, maxPoolSize))
}

data class DbConfig(val driverClass: String, val url: String, val user: String, val password: String, val maxPoolSize: Int)
