package com.ainotetaker.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ainotetaker.app.ui.theme.Primary
import com.ainotetaker.app.ui.theme.TextMuted

/** Round, tappable completion check used in lists and task detail. */
@Composable
fun RoundCheck(done: Boolean, onClick: () -> Unit, size: Int = 26) {
    val color = if (done) Primary else TextMuted
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(if (done) Primary else Color.Transparent)
            .border(2.dp, color, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        if (done) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size((size * 0.62f).dp),
            )
        }
    }
}
