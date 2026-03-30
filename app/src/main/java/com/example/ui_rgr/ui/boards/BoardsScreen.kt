package com.example.ui_rgr.ui.boards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.UnfoldLess
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.ui_rgr.data.model.Board
import com.example.ui_rgr.viewmodel.MainViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun BoardsScreen(vm: MainViewModel) {

    var text by remember { mutableStateOf("") }
    val commentInputs = remember { mutableStateMapOf<Int, String>() }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Boards", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Community feed",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        val error = vm.errorMessage.value

        if (error != null) {
            Text(
                error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
        }

        if (vm.isLoggedIn.value) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("Create a new board post") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        enabled = !vm.isLoading.value,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            if (text.isNotBlank()) {
                                focusManager.clearFocus()
                                vm.createBoard(text)
                                text = ""
                            }
                        })
                    )

                    Button(
                        onClick = {
                            if (text.isNotEmpty()) {
                                focusManager.clearFocus()
                                vm.createBoard(text)
                                text = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !vm.isLoading.value && text.isNotEmpty()
                    ) {
                        if (vm.isLoading.value) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Post")
                        }
                    }
                }
            }
        }

        if (vm.boards.value.isEmpty() && !vm.isLoading.value) {
            Text(
                "No boards yet. Be the first to post!",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        LazyColumn {
            items(vm.boards.value) { board ->
                BoardCard(
                    board = board,
                    vm = vm,
                    commentInput = commentInputs[board.id] ?: "",
                    onCommentInputChange = { commentInputs[board.id] = it }
                )
            }
        }
    }
}

@Composable
private fun BoardCard(
    board: Board,
    vm: MainViewModel,
    commentInput: String,
    onCommentInputChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val comments = vm.commentsByBoard[board.id] ?: emptyList()
    val commentsExpanded = vm.expandedBoardId.value == board.id
    var showDeleteConfirm by remember { mutableStateOf(false) }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete post") },
            text = { Text("Are you sure you want to delete this post?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirm = false
                        vm.deleteBoard(board.id)
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    board.content,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 12.dp)
                )

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "By: ${board.name}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "At: ${formatDateTime(board.create_time)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (vm.isLoggedIn.value && board.email == vm.getUserEmail()) {
                        FilledTonalButton(
                            onClick = { showDeleteConfirm = true },
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Delete,
                                contentDescription = "Delete board"
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    VoteButton(
                        onClick = { if (vm.isLoggedIn.value) vm.voteBoard(board.id, "upvote") },
                        enabled = vm.isLoggedIn.value,
                        isUpvote = true,
                        isActive = board.is_user_upvoted,
                        count = board.upvote_count
                    )
                    VoteButton(
                        onClick = { if (vm.isLoggedIn.value) vm.voteBoard(board.id, "downvote") },
                        enabled = vm.isLoggedIn.value,
                        isUpvote = false,
                        isActive = board.is_user_downvoted,
                        count = board.downvote_count
                    )
                }

                FilledTonalButton(
                    onClick = { vm.toggleComments(board.id) },
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.45f))
                ) {
                    Icon(
                        imageVector = if (commentsExpanded) Icons.Rounded.UnfoldLess else Icons.Rounded.ChatBubbleOutline,
                        contentDescription = if (commentsExpanded) "Hide comments" else "Show comments"
                    )
                }
            }

            if (commentsExpanded) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                if (vm.isLoggedIn.value) {
                    TextField(
                        value = commentInput,
                        onValueChange = onCommentInputChange,
                        label = { Text("Write a comment") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            if (commentInput.trim().isNotEmpty()) {
                                focusManager.clearFocus()
                                vm.createComment(board.id, commentInput)
                                onCommentInputChange("")
                            }
                        })
                    )
                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            vm.createComment(board.id, commentInput)
                            onCommentInputChange("")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        enabled = commentInput.trim().isNotEmpty()
                    ) {
                        Text("Send")
                    }
                }

                if (comments.isEmpty()) {
                    Text(
                        "No comments yet",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                comments.forEach { comment ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(comment.content)
                            Text(
                                "By: ${comment.name} at ${formatDateTime(comment.create_time)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            Row(
                                modifier = Modifier.padding(top = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                VoteButton(
                                    onClick = {
                                        if (vm.isLoggedIn.value) {
                                            vm.voteComment(board.id, comment.id, "upvote")
                                        }
                                    },
                                    enabled = vm.isLoggedIn.value,
                                    isUpvote = true,
                                    isActive = comment.is_user_upvoted,
                                    count = comment.upvote_count
                                )
                                VoteButton(
                                    onClick = {
                                        if (vm.isLoggedIn.value) {
                                            vm.voteComment(board.id, comment.id, "downvote")
                                        }
                                    },
                                    enabled = vm.isLoggedIn.value,
                                    isUpvote = false,
                                    isActive = comment.is_user_downvoted,
                                    count = comment.downvote_count
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatDateTime(raw: String): String {
    val value = raw.trim()
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")

    if (value.equals("{timestamp}", ignoreCase = true) || value.equals("timestamp", ignoreCase = true)) {
        return LocalDateTime.now().format(outputFormatter)
    }

    runCatching {
        if (value.all { it.isDigit() }) {
            val epoch = value.toLong()
            val instant = if (value.length <= 10) {
                Instant.ofEpochSecond(epoch)
            } else {
                Instant.ofEpochMilli(epoch)
            }
            return instant.atZone(ZoneId.systemDefault()).format(outputFormatter)
        }
    }

    runCatching {
        val instant = Instant.parse(value)
        return instant.atZone(ZoneId.systemDefault()).format(outputFormatter)
    }

    runCatching {
        val offset = OffsetDateTime.parse(value)
        return offset.atZoneSameInstant(ZoneId.systemDefault()).format(outputFormatter)
    }

    runCatching {
        val local = LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        return local.format(outputFormatter)
    }

    runCatching {
        val local = LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
        return local.format(outputFormatter)
    }

    runCatching {
        val local = LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
        return local.format(outputFormatter)
    }

    runCatching {
        val local = LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))
        return local.format(outputFormatter)
    }

    return value
}

@Composable
private fun VoteButton(
    onClick: () -> Unit,
    enabled: Boolean,
    isUpvote: Boolean,
    isActive: Boolean,
    count: Int
) {
    val activeContainer = if (isUpvote) Color(0xFF15803D) else Color(0xFFB91C1C)
    val activeContent = Color.White
    val inactiveContainer = MaterialTheme.colorScheme.secondaryContainer
    val inactiveContent = MaterialTheme.colorScheme.onSecondaryContainer

    val loggedOutContainer = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.9f)
    val loggedOutContent = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.9f)

    val containerColor = when {
        !enabled -> loggedOutContainer
        isActive -> activeContainer
        else -> inactiveContainer
    }

    val contentColor = when {
        !enabled -> loggedOutContent
        isActive -> activeContent
        else -> inactiveContent
    }

    val borderColor = when {
        !enabled -> MaterialTheme.colorScheme.outline.copy(alpha = 0.55f)
        isActive -> Color.Transparent
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)
    }

    Button(
        onClick = onClick,
        enabled = enabled,
        shape = if (isUpvote) RoundedCornerShape(999.dp) else CutCornerShape(10.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = loggedOutContainer,
            disabledContentColor = loggedOutContent
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isUpvote) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                contentDescription = if (isUpvote) "Upvote" else "Downvote"
            )
            Text(count.toString())
        }
    }
}