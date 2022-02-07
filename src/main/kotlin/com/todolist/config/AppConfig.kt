package com.todolist.config

import io.ktor.application.*


class AppConfig {
//    lateinit var db: DbConfig
//    lateinit var serverConfig: ServerConfig
}

// What is setupConfig really?
//fun Application.setupConfig(appConfig: AppConfig) {
//    val db = environment.config.config("ktor.database")
//    val driverClass = db.property("driverClass").getString()
//    val url = db.property("url").getString()
//    val user = db.property("user").getString()
//    val password = db.property("password").getString()
//    val maxPoolSize = db.property("maxPoolSize").getString().toInt()
//
//    appConfig.db = DbConfig(driverClass, url, user, password, maxPoolSize)
//}

//data class DbConfig(val driverClass: String, val url: String, val user: String, val password: String, val maxPoolSize: Int)