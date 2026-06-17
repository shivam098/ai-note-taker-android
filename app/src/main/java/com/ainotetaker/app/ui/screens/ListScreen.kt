package com.ainotetaker.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ainotetaker.app.model.Snapshot
import com.ainotetaker.app.model.TaskItem
import com.ainotetaker.app.ui.AppViewModel
import com.ainotetaker.app.ui.RoundCheck
import com.ainotetaker.app.ui.theme.Primary
import com.ainotetaker.app.ui.theme.TextMuted
import com.ainotetaker.app.ui.theme.TextPrimary
import com.ainotetaker.app.ui.theme.TextSecondary

@Composable
fun ListScreen(
    snapshot: Snapshot,
    listId: String,
    onBack: () -> Unit,
    onOpenTask: (String) -> Unit,
    vm: AppViewModel,
) {
    val list = snapshot.lists.find { it.id == listId }
    val title = list?.title ?: "List"
    val emoji = list?.emoji ?: "\uD83D\uDCCB"
    val top = snapshot.tasks.filter { it.listId == listId && it.parentId == null }
    val active = top.filter { !it.done }
    val done = top.filter { it.done }
    var newTask by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 6.dp),
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
            }
            Text(
                text = "$emoji  $title",
                color = TextPrimary,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            items(active, key = { it.id }) { task ->
                TaskRow(task, snapshot, onToggle = { vm.toggleTask(task.id) }, onClick = { onOpenTask(task.id) })
            }
            if (done.isNotEmpty()) {
                item {
                    Text(
                        text = "COMPLETED  ${done.size}",
                        color = TextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 18.dp, bottom = 6.dp),
                    )
                }
                items(done, key = { it.id }) { task ->
                    TaskRow(task, snapshot, onToggle = { vm.toggleTask(task.id) }, onClick = { onOpenTask(task.id) })
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            OutlinedTextField(
                value = newTask,
                onValueChange = { newTask = it },
                placeholder = { Text("Add a task\u2026") },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.weight(1f),
            )
            IconButton(onClick = {
                vm.addTask(listId, newTask)
                newTask = ""
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add task", tint = Primary)
            }
        }
    }
}

@Composable
private fun TaskRow(
    task: TaskItem,
    snapshot: Snapshot,
    onToggle: () -> Unit,
    onClick: () -> Unit,
) {
    val subs = snapshot.tasks.filter { it.parentId == task.id }
    val subDone = subs.count { it.done }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 6.dp),
    ) {
        RoundCheck(done = task.done, onClick = onToggle)
        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
            Text(
                text = task.title,
                color = if (task.done) TextMuted else TextPrimary,
                fontSize = 16.sp,
                textDecoration = if (task.done) TextDecoration.LineThrough else TextDecoration.None,
            )
            if (subs.isNotEmpty() || task.notes.isNotBlank()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (subs.isNotEmpty()) {
                        Text("$subDone/${subs.size} subtasks", color = TextMuted, fontSize = 12.sp)
                    }
                    if (task.notes.isNotBlank()) {
                        Icon(
                            Icons.Filled.Notes,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.padding(start = 8.dp).size(14.dp),
                        )
                    }
                }
            }
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = TextMuted)
    }
}
