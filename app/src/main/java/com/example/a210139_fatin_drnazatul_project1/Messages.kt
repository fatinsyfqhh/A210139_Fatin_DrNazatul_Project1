package com.example.a210139_fatin_drnazatul_project1

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun MessagesScreen(navController: NavHostController) {
    val colorScheme = MaterialTheme.colorScheme
    var searchQuery by remember { mutableStateOf("") }

    val chats = listOf(
        ChatPreview("NearBakery", "Your croissants are ready for pickup!", "10:30 AM", isUnread = true),
        ChatPreview("Mira", "Is the tuna still available?", "Yesterday"),
        ChatPreview("Amar", "Thanks for the bread!", "Monday")
    )

    Scaffold(
        topBar = {
            Surface(
                color = colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp)
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = colorScheme.tertiary)
                    }
                    Text(
                        text = "MESSAGES",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = colorScheme.tertiary
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorScheme.background)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = { Text("Search conversations...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = colorScheme.outlineVariant,
                    focusedContainerColor = colorScheme.surface
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(chats) { chat ->
                    ChatItem(chat, colorScheme)
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatItem(chat: ChatPreview, colorScheme: ColorScheme) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable {}.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // online indicator
        Box {
            Box(
                modifier = Modifier.size(52.dp).clip(CircleShape).background(colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(chat.name.take(1), fontWeight = FontWeight.Bold, color = colorScheme.onPrimaryContainer)
            }
            // online dot
            Box(
                modifier = Modifier.size(12.dp).background(Color.Green, CircleShape)
                    .border(2.dp, colorScheme.background, CircleShape)
                    .align(Alignment.BottomEnd)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = chat.name,
                fontWeight = if (chat.isUnread) FontWeight.ExtraBold else FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = chat.lastMsg,
                fontSize = 14.sp,
                color = if (chat.isUnread) colorScheme.onSurface else colorScheme.onSurfaceVariant,
                maxLines = 1,
                fontWeight = if (chat.isUnread) FontWeight.Medium else FontWeight.Normal
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = chat.time,
                fontSize = 12.sp,
                color = if (chat.isUnread) colorScheme.primary else colorScheme.outline
            )
            // unread badge
            if (chat.isUnread) {
                Box(
                    modifier = Modifier.padding(top = 4.dp).size(18.dp)
                        .background(colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("1", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}