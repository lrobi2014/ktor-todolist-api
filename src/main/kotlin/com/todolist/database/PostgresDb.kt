package com.todolist.database

import com.todolist.DbConfig
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

object PostgresDb {

    fun connect(dbConfig: DbConfig) {
        val conn = hikari(dbConfig)
        val dataSource = HikariDataSource(conn)

        // Establish db connection
        Database.connect(conn)

        // Create schema and seed tables
        val flyway = Flyway.configure().baselineOnMigrate(true).dataSource(dataSource).load()
        flyway.migrate()
    }

    private fun hikari(dbConfig: DbConfig): HikariDataSource {
        val config = HikariConfig()

        config.driverClassName = dbConfig.driverClass
        config.jdbcUrl = dbConfig.url
        config.username = dbConfig.user
        config.password = dbConfig.password
        config.maximumPoolSize = dbConfig.maxPoolSize
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"

        config.validate()
        return HikariDataSource(config)
    }
}