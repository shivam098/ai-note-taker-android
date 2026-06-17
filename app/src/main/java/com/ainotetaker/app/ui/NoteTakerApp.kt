package com.ainotetaker.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import com.ainotetaker.app.ui.screens.HomeScreen
import com.ainotetaker.app.ui.screens.ListScreen
import com.ainotetaker.app.ui.screens.TaskScreen

/**
 * Minimal state-based navigation (no nav library) — keeps the scaffold light.
 * Home -> List -> Task. Nested subtasks open another Task screen.
 */
@Composable
fun NoteTakerApp(vm: AppViewModel) {
    val snapshot by vm.state.collectAsState()
    var openListId by rememberSaveable { mutableStateOf<String?>(null) }
    var openTaskId by rememberSaveable { mutableStateOf<String?>(null) }

    when {
        openTaskId != null -> {
            BackHandler { openTaskId = null }
            TaskScreen(
                snapshot = snapshot,
                taskId = openTaskId!!,
                onBack = { openTaskId = null },
                onOpenSubtask = { openTaskId = it },
                vm = vm,
            )
        }

        openListId != null -> {
            BackHandler { openListId = null }
            ListScreen(
                snapshot = snapshot,
                listId = openListId!!,
                onBack = { openListId = null },
                onOpenTask = { openTaskId = it },
                vm = vm,
            )
        }

        else -> HomeScreen(
            snapshot = snapshot,
            onOpenList = { openListId = it },
            vm = vm,
        )
    }
}
