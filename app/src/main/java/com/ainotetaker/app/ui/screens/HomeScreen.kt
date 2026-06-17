package com.ainotetaker.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ainotetaker.app.model.Snapshot
import com.ainotetaker.app.model.TaskList
import com.ainotetaker.app.ui.AppViewModel
import com.ainotetaker.app.ui.theme.Primary
import com.ainotetaker.app.ui.theme.SurfaceDark
import com.ainotetaker.app.ui.theme.TextMuted
import com.ainotetaker.app.ui.theme.TextPrimary
import com.ainotetaker.app.ui.theme.TextSecondary

@Composable
fun HomeScreen(snapshot: Snapshot, onOpenList: (String) -> Unit, vm: AppViewModel) {
    var newList by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding()
            .padding(horizontal = 20.dp),
    ) {
        Spacer(Modifier.height(16.dp))
        Text("AI Note Taker", color = TextPrimary, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text(
            text = "YOUR LISTS",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 20.dp, bottom = 10.dp),
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(snapshot.lists, key = { it.id }) { list ->
                val openCount = snapshot.tasks.count {
                    it.listId == list.id && it.parentId == null && !it.done
                }
                ListRow(list = list, openCount = openCount, onClick = { onOpenList(list.id) })
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp),
        ) {
            OutlinedTextField(
                value = newList,
                onValueChange = { newList = it },
                placeholder = { Text("New list") },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.weight(1f),
            )
            IconButton(onClick = {
                vm.addList(newList)
                newList = ""
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add list", tint = Primary)
            }
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun ListRow(list: TaskList, openCount: Int, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceDark)
            .clickable { onClick() }
            .padding(14.dp),
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(list.colorArgb).copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center,
        ) {
            Text(list.emoji, fontSize = 20.sp)
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(list.title, color = TextPrimary, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
            Text(
                text = if (openCount == 0) "All done" else "$openCount open",
                color = TextMuted,
                fontSize = 13.sp,
            )
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = TextMuted)
    }
}
