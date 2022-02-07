package com.todolist.models

import com.todolist.database.PGEnum
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object TodoListItems: IntIdTable("list_items") {
    val name: Column<String> = varchar("name", 255)
    val list: Column<EntityID<Int>> = reference("list_id", TodoLists)
    val status: Column<ItemStatus> = customEnumeration("status", "ItemStatus", { value -> ItemStatus.valueOf(value as String) }, { PGEnum("ItemStatus", it) })
    val dateCreated: Column<Long> = long("date_created")
}

class TodoListItem(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<TodoListItem>(TodoListItems)

    var name by TodoListItems.name
    var list by TodoList referencedOn TodoListItems.list
    var status by TodoListItems.status
    var dateCreated by TodoListItems.dateCreated

    fun toTodoListItemSerializable() = TodoListItemSerializable(id.value, name, list.id.value, status.name.lowercase(), dateCreated)
}

@Serializable
data class TodoListItemSerializable(
    val id: Int = 0,
    val name: String,
    val listId: Int = 0,
    var status: String = ItemStatus.ACTIVE.name.lowercase(),
    val dateCreated: Long = 0
): SerializableEntity


enum class ItemStatus {
    ACTIVE,
    DONE
}