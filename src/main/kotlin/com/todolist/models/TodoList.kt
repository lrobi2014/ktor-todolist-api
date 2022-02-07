package com.todolist.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object TodoLists: IntIdTable("lists") {
    val name: Column<String> = varchar("name", 255)
    val archived: Column<Boolean> = bool("archived")
    val user: Column<String> = varchar("user_id", 255)
    val dateCreated: Column<Long> = long("date_created")
}

class TodoList(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<TodoList>(TodoLists)

    var name by TodoLists.name
    var archived by TodoLists.archived
    var user by TodoLists.user
    val items by TodoListItem referrersOn TodoListItems.list
    var dateCreated by TodoLists.dateCreated

    fun toTodoListSerializable(serializableItems: List<TodoListItemSerializable>) = TodoListSerializable(id.value, name, archived, user, serializableItems, dateCreated)
}

@Serializable
data class TodoListSerializable(
    val id: Int = 0,
    val name: String,
    val archived: Boolean = false,
    val user: String,
    val items: List<TodoListItemSerializable?> = listOf(),
    val dateCreated: Long = 0): SerializableEntity
