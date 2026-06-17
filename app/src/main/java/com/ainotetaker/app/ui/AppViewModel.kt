package com.ainotetaker.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ainotetaker.app.data.TaskRepository
import com.ainotetaker.app.model.Snapshot
import com.ainotetaker.app.model.TaskItem
import com.ainotetaker.app.model.TaskList
import com.ainotetaker.app.util.newId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = TaskRepository(app)
    private val _state = MutableStateFlow(repo.load())
    val state: StateFlow<Snapshot> = _state.asStateFlow()

    private fun mutate(block: (Snapshot) -> Snapshot) {
        val next = block(_state.value)
        _state.value = next
        repo.save(next)
    }

    fun addList(title: String, emoji: String = "\uD83D\uDCCB") {
        val t = title.trim().ifEmpty { return }
        mutate { it.copy(lists = it.lists + TaskList(id = newId("list"), title = t, emoji = emoji)) }
    }

    fun addTask(listId: String, title: String, parentId: String? = null) {
        val t = title.trim().ifEmpty { return }
        mutate { it.copy(tasks = it.tasks + TaskItem(id = newId("task"), listId = listId, parentId = parentId, title = t)) }
    }

    fun toggleTask(id: String) =
        mutate { s -> s.copy(tasks = s.tasks.map { if (it.id == id) it.copy(done = !it.done) else it }) }

    fun updateTask(id: String, title: String? = null, notes: String? = null) =
        mutate { s ->
            s.copy(tasks = s.tasks.map {
                if (it.id == id) it.copy(title = title ?: it.title, notes = notes ?: it.notes) else it
            })
        }

    fun deleteTask(id: String) =
        mutate { s ->
            val remove = mutableSetOf(id)
            var changed = true
            while (changed) {
                changed = false
                for (t in s.tasks) {
                    if (t.parentId != null && t.parentId in remove && t.id !in remove) {
                        remove.add(t.id)
                        changed = true
                    }
                }
            }
            s.copy(tasks = s.tasks.filterNot { it.id in remove })
        }
}
