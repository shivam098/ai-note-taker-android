package com.ainotetaker.app.data

import android.content.Context
import com.ainotetaker.app.model.Snapshot
import com.ainotetaker.app.model.TaskItem
import com.ainotetaker.app.model.TaskList
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

/** Offline-first local store: one JSON file in the app's private storage. */
class TaskRepository(context: Context) {
    private val file = File(context.filesDir, "ainotetaker.json")
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    fun load(): Snapshot =
        try {
            if (file.exists()) json.decodeFromString(file.readText()) else seed()
        } catch (e: Exception) {
            seed()
        }

    fun save(snapshot: Snapshot) {
        try {
            file.writeText(json.encodeToString(snapshot))
        } catch (e: Exception) {
            // Best-effort local persistence; ignore write failures for the scaffold.
        }
    }

    private fun seed(): Snapshot {
        val lists = listOf(
            TaskList(id = "inbox", title = "Inbox", emoji = "\uD83D\uDCE5"),
            TaskList(id = "work", title = "Work", emoji = "\uD83D\uDCBC", colorArgb = 0xFF7C6FF0L),
            TaskList(id = "personal", title = "Personal", emoji = "\uD83C\uDF3F", colorArgb = 0xFF34C759L),
            TaskList(id = "groceries", title = "Groceries", emoji = "\uD83D\uDED2", colorArgb = 0xFFFF9F45L),
        )
        val tasks = listOf(
            TaskItem(id = "t1", listId = "inbox", title = "Try AI Note Taker"),
            TaskItem(id = "t2", listId = "work", title = "Ship launch plan", notes = "Coordinate design + marketing."),
            TaskItem(id = "t3", listId = "work", parentId = "t2", title = "Write release notes"),
            TaskItem(id = "t4", listId = "work", parentId = "t2", title = "Schedule announcement"),
            TaskItem(id = "t5", listId = "personal", title = "Morning workout"),
            TaskItem(id = "t6", listId = "groceries", title = "Oat milk"),
            TaskItem(id = "t7", listId = "groceries", title = "Coffee beans", done = true),
        )
        return Snapshot(lists, tasks)
    }
}
