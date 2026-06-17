package com.ainotetaker.app.model

import kotlinx.serialization.Serializable

@Serializable
data class TaskList(
    val id: String,
    val title: String,
    val emoji: String = "\uD83D\uDCCB",
    val colorArgb: Long = 0xFF7C6FF0L,
)

@Serializable
data class TaskItem(
    val id: String,
    val listId: String,
    val parentId: String? = null,
    val title: String,
    val notes: String = "",
    val done: Boolean = false,
    val dueDate: Long? = null,
)

/** The whole local dataset, serialized to disk as one JSON document. */
@Serializable
data class Snapshot(
    val lists: List<TaskList> = emptyList(),
    val tasks: List<TaskItem> = emptyList(),
)
