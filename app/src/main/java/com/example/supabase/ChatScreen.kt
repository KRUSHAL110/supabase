package com.example.supabase

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

// User Data Class
data class User(val name: String, val phone: String, val imageUri: Uri?)

val dummyUsers = mutableStateListOf(
    User("Krushal", "1234567890", null),
    User("Diya", "9876543210", null),
    User("Preet", "1112223333", null),
    User("Kush", "4445556666", null),
    User("Aditya", "7778889999", null)
)
val pinnedUsers = mutableStateListOf<User>()

@Composable
fun ChatListAndChatScreenController() {
    var selectedUser by remember { mutableStateOf<User?>(null) }

    if (selectedUser == null) {
        ChatListScreen(onUserClick = { selectedUser = it })
    } else {
        ChatScreenWithUser(user = selectedUser!!, onBack = { selectedUser = null })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(onUserClick: (User) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newPhone by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Select User to Chat")
                },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.user),
                            contentDescription = "Create User",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            val sortedUsers = remember {
                derivedStateOf {
                    pinnedUsers + dummyUsers.filterNot { it in pinnedUsers }
                }
            }

            sortedUsers.value.forEach { user ->
                var expanded by remember { mutableStateOf(false) }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onUserClick(user) }
                    ) {
                        if (user.imageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(user.imageUri),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )
                        } else {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = user.name.first().uppercase(),
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = user.name, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                            Text(text = user.phone, fontSize = 14.sp, color = Color.Gray)
                        }
                        if (user in pinnedUsers) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.pin),
                                contentDescription = "Pinned",
                                tint = Color.Red,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Box {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More Options")
                        }

                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            val isPinned = user in pinnedUsers
                            DropdownMenuItem(
                                text = { Text(if (isPinned) "Unpin Chat" else "Pin Chat") },
                                onClick = {
                                    expanded = false
                                    if (isPinned) pinnedUsers.remove(user)
                                    else pinnedUsers.add(user)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Delete Chat") },
                                onClick = {
                                    expanded = false
                                    dummyUsers.remove(user)
                                    pinnedUsers.remove(user)
                                }
                            )
                        }
                    }
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Create New User") },
                text = {
                    Column {
                        OutlinedTextField(value = newName, onValueChange = { newName = it }, label = { Text("Name") })
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = newPhone, onValueChange = { newPhone = it }, label = { Text("Phone Number") })
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                            Text("Choose Profile Image")
                        }
                        selectedImageUri?.let {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Image selected ✅", color = Color.Green)
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (newName.isNotBlank() && newPhone.isNotBlank()) {
                            dummyUsers.add(User(newName, newPhone, selectedImageUri))
                            newName = ""
                            newPhone = ""
                            selectedImageUri = null
                            showDialog = false
                        }
                    }) {
                        Text("Create")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialog = false
                        newName = ""
                        newPhone = ""
                        selectedImageUri = null
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreenWithUser(user: User, onBack: () -> Unit) {
    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage("Hey ${user.name}!", true),
                ChatMessage("Hi there!", false),
                ChatMessage("What's up?", true)
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = user.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(painter = painterResource(id = R.drawable.back), contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Call, contentDescription = "Call")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            ChatScreen(
                messages = messages,
                onSendMessage = { newMessage -> messages = messages + ChatMessage(newMessage, true) },
                onSendAudio = {},
                onSendFile = {}
            )
        }
    }
}

@Composable
fun ChatScreen(
    messages: List<ChatMessage>,
    onSendMessage: (String) -> Unit,
    onSendAudio: () -> Unit,
    onSendFile: (Uri) -> Unit
) {
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let(onSendFile) }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            reverseLayout = true
        ) {
            items(messages) { message ->
                ChatBubble(message)
            }
        }
        ChatInputBar(
            onSendMessage = onSendMessage,
            onSendAudio = onSendAudio,
            onPickFile = { filePickerLauncher.launch("*") }
        )
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val backgroundColor = if (message.isSentByUser) Color(0xFF1976D2) else Color(0xFFBDBDBD)
    val alignment = if (message.isSentByUser) Arrangement.End else Arrangement.Start

    Row(
        horizontalArrangement = alignment,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = message.content,
            modifier = Modifier
                .background(color = backgroundColor, shape = RoundedCornerShape(12.dp))
                .padding(12.dp),
            color = Color.White,
            fontSize = 16.sp
        )
    }
}
@Composable
fun ChatInputBar(
    onSendMessage: (String) -> Unit,
    onSendAudio: () -> Unit,
    onPickFile: () -> Unit
) {
    var messageText by remember { mutableStateOf(TextFieldValue("")) }

    Surface(
        tonalElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            IconButton(onClick = onSendAudio, modifier = Modifier.size(36.dp)) {
                Icon(painter = painterResource(id = R.drawable.microphone), contentDescription = "Mic")
            }
            Spacer(modifier = Modifier.width(6.dp))
            TextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                placeholder = { Text("Type a message...") },
                maxLines = 4,
                shape = RoundedCornerShape(24.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            IconButton(onClick = onPickFile, modifier = Modifier.size(36.dp)) {
                Icon(painter = painterResource(id = R.drawable.attachfile), contentDescription = "Attach File")
            }
            Spacer(modifier = Modifier.width(6.dp))
            IconButton(
                onClick = {
                    val text = messageText.text.trim()
                    if (text.isNotEmpty()) {
                        onSendMessage(text)
                        messageText = TextFieldValue("")
                    }
                },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send")
            }
        }
    }
}

data class ChatMessage(val content: String, val isSentByUser: Boolean)
