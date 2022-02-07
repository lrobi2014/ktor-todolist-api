package com.todolist.response

import com.todolist.models.SerializableEntity
import com.todolist.models.TodoListSerializable
import kotlinx.serialization.Serializable

@Serializable
data class APIResponse(val data: APIResult?, val errors: List<APIError>?)

@Serializable
data class APISingleResponse(val data: APISingleResult?, val errors: List<APIError>?)

@Serializable
data class APIResult(val entries: List<SerializableEntity>?)

@Serializable
data class APISingleResult(val entry: SerializableEntity?)


/**
 * {
 *      data: {
 *          entries: [
 *              {},
 *              {}
 *          ]
 *      }
 *      errors: [
 *          {
 *              type: String
 *              statusCode: HttpStatusCode
 *              message: String
 *          }
 *      ]
 * }
 *
 */


@Serializable
data class APIError (val type: String, val statusCode: Int, val message: String)
