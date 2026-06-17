package com.ainotetaker.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DeleteOutline
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ainotetaker.app.model.Snapshot
import com.ainotetaker.app.ui.AppViewModel
import com.ainotetaker.app.ui.RoundCheck
import com.ainotetaker.app.ui.theme.TextMuted
import com.ainotetaker.app.ui.theme.TextPrimary
import com.ainotetaker.app.ui.theme.TextSecondary

@Composable
fun TaskScreen(
    snapshot: Snapshot,
    taskId: String,
    onBack: () -> Unit,
    onOpenSubtask: (String) -> Unit,
    vm: AppViewModel,
) {
    val task = snapshot.tasks.find { it.id == taskId }
    if (task == null) {
        Column(
            modifier = Modifier.fillMaxSize().systemBarsPadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("This task no longer exists.", color = TextSecondary, fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))
            Text("Go back", color = TextPrimary, modifier = Modifier.clickable { onBack() })
        }
        return
    }

    val subs = snapshot.tasks.filter { it.parentId == taskId }
    var newSub by remember { mutableStateOf("") }

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
            Spacer(Modifier.weight(1f))
            IconButton(onClick = {
                vm.deleteTask(taskId)
                onBack()
            }) {
                Icon(Icons.Filled.DeleteOutline, contentDescription = "Delete", tint = TextSecondary)
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RoundCheck(done = task.done, onClick = { vm.toggleTask(taskId) }, size = 28)
                OutlinedTextField(
                    value = task.title,
                    onValueChange = { vm.updateTask(taskId, title = it) },
                    placeholder = { Text("Task name") },
                    modifier = Modifier.weight(1f).padding(start = 10.dp),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = if (task.done) TextDecoration.LineThrough else TextDecoration.None,
                    ),
                )
            }

            Text(
                "NOTES",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
            )
            OutlinedTextField(
                value = task.notes,
                onValueChange = { vm.updateTask(taskId, notes = it) },
                placeholder = { Text("Add notes, links, or details\u2026") },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().height(140.dp),
            )

            Text(
                "SUBTASKS  ${subs.count { it.done }}/${subs.size}",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 26.dp, bottom = 6.dp),
            )

            subs.forEach { sub ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenSubtask(sub.id) }
                        .padding(vertical = 10.dp),
                ) {
                    RoundCheck(done = sub.done, onClick = { vm.toggleTask(sub.id) }, size = 22)
                    Text(
                        text = sub.title,
                        color = if (sub.done) TextMuted else TextPrimary,
                        fontSize = 16.sp,
                        textDecoration = if (sub.done) TextDecoration.LineThrough else TextDecoration.None,
                        modifier = Modifier.weight(1f).padding(start = 12.dp),
                    )
                    Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = TextMuted)
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 24.dp),
            ) {
                OutlinedTextField(
                    value = newSub,
                    onValueChange = { newSub = it },
                    placeholder = { Text("Add a subtask\u2026") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = {
                    vm.addTask(task.listId, newSub, parentId = taskId)
                    newSub = ""
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add subtask", tint = TextPrimary)
                }
            }
        }
    }
}
