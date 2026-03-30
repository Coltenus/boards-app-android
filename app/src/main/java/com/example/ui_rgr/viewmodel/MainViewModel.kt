package com.example.ui_rgr.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ui_rgr.data.model.*
import com.example.ui_rgr.data.repository.MainRepository
import com.example.ui_rgr.utils.SessionManager
import kotlinx.coroutines.launch

sealed class Screen {
    data object Home : Screen()
    data object Login : Screen()
    data object Register : Screen()
    data object Boards : Screen()
    data object Profile : Screen()
}

class MainViewModel(
    private val repo: MainRepository,
    private val session: SessionManager
) : ViewModel() {

    var currentScreen = mutableStateOf<Screen>(Screen.Home)
    var boards = mutableStateOf<List<Board>>(emptyList())
    var commentsByBoard = mutableStateMapOf<Int, List<Comment>>()
    var expandedBoardId = mutableStateOf<Int?>(null)
    var profile = mutableStateOf<UserProfile?>(null)
    var isLoggedIn = mutableStateOf(false)
    var isDarkMode = mutableStateOf(session.isDarkMode())
    var errorMessage = mutableStateOf<String?>(null)
    var isLoading = mutableStateOf(false)

    fun toggleDarkMode() {
        val next = !isDarkMode.value
        isDarkMode.value = next
        session.setDarkMode(next)
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            errorMessage.value = "Email and password are required"
            return
        }
        
        isLoading.value = true
        viewModelScope.launch {
            try {
                val res = repo.login(email, password)
                if (res.success && res.user != null) {
                    session.saveUser(res.user)
                    isLoggedIn.value = true
                    errorMessage.value = null
                    currentScreen.value = Screen.Boards
                    loadBoards()
                } else {
                    errorMessage.value = res.message ?: "Login failed"
                }
            } catch (e: Exception) {
                errorMessage.value = "Error: ${e.message ?: "Unknown error"}"
            } finally {
                isLoading.value = false
            }
        }
    }
    
    fun register(name: String, email: String, password: String, confirmPassword: String, gender: String, birthdate: String) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || gender.isEmpty() || birthdate.isEmpty()) {
            errorMessage.value = "All fields are required"
            return
        }
        if (password != confirmPassword) {
            errorMessage.value = "Passwords do not match"
            return
        }
        
        isLoading.value = true
        viewModelScope.launch {
            try {
                val res = repo.register(RegisterRequest(name, email, password, gender, birthdate))
                if (res.success) {
                    errorMessage.value = null
                    currentScreen.value = Screen.Login
                } else {
                    errorMessage.value = res.message ?: "Registration failed"
                }
            } catch (e: Exception) {
                errorMessage.value = "Error: ${e.message ?: "Unknown error"}"
            } finally {
                isLoading.value = false
            }
        }
    }
    
    fun logout() {
        session.clear()
        isLoggedIn.value = false
        errorMessage.value = null
        boards.value = emptyList()
        commentsByBoard.clear()
        expandedBoardId.value = null
        profile.value = null
        currentScreen.value = Screen.Home
    }
    
    fun navigateTo(screen: Screen) {
        if (screen == Screen.Profile && !isLoggedIn.value) {
            errorMessage.value = "Login required to open profile"
            currentScreen.value = Screen.Login
            return
        }
        currentScreen.value = screen
    }

    fun getUserEmail(): String = session.getEmail()

    fun loadBoards() {
        viewModelScope.launch {
            try {
                val res = repo.getBoards(
                    session.getToken(),
                    session.getEmail()
                )

                if (res.success) {
                    boards.value = res.boards
                }

            } catch (e: Exception) {
                errorMessage.value = "No internet connection"
            }
        }
    }

    fun createBoard(content: String) {
        if (!isLoggedIn.value) {
            errorMessage.value = "Login required to create board"
            currentScreen.value = Screen.Login
            return
        }
        val normalized = content.trim()
        if (normalized.isEmpty()) {
            errorMessage.value = "Board content cannot be empty"
            return
        }
        viewModelScope.launch {
            try {
                val res = repo.createBoard(
                    CreateBoardRequest(
                        normalized,
                        session.getToken(),
                        session.getEmail(),
                        java.time.Instant.now().toString()
                    )
                )
                if (res.success) {
                    errorMessage.value = null
                    loadBoards()
                } else {
                    errorMessage.value = res.message ?: "Failed to create board"
                }
            } catch (e: Exception) {
                errorMessage.value = "Error: ${e.message ?: "Unknown error"}"
            }
        }
    }

    fun voteBoard(boardId: Int, voteType: String) {
        if (!isLoggedIn.value) {
            errorMessage.value = "Login required to vote"
            return
        }
        viewModelScope.launch {
            try {
                val res = repo.voteBoard(
                    boardId,
                    VoteBoardRequest(session.getToken(), session.getEmail(), voteType)
                )
                if (res.success) {
                    loadBoards()
                } else {
                    errorMessage.value = res.message ?: "Failed to vote"
                }
            } catch (e: Exception) {
                errorMessage.value = "Error: ${e.message ?: "Unknown error"}"
            }
        }
    }

    fun deleteBoard(boardId: Int) {
        if (!isLoggedIn.value) {
            errorMessage.value = "Login required to delete board"
            return
        }
        viewModelScope.launch {
            try {
                val res = repo.deleteBoard(
                    boardId,
                    AuthRequest(session.getToken(), session.getEmail())
                )
                if (res.success) {
                    commentsByBoard.remove(boardId)
                    if (expandedBoardId.value == boardId) {
                        expandedBoardId.value = null
                    }
                    loadBoards()
                } else {
                    errorMessage.value = res.message ?: "Failed to delete board"
                }
            } catch (e: Exception) {
                errorMessage.value = "Error: ${e.message ?: "Unknown error"}"
            }
        }
    }

    fun toggleComments(boardId: Int) {
        if (expandedBoardId.value == boardId) {
            expandedBoardId.value = null
            return
        }
        expandedBoardId.value = boardId
        loadComments(boardId)
    }

    fun loadComments(boardId: Int) {
        viewModelScope.launch {
            try {
                val res = repo.getComments(
                    boardId,
                    AuthRequest(session.getToken(), session.getEmail())
                )
                if (res.success) {
                    commentsByBoard[boardId] = res.comments
                } else {
                    errorMessage.value = res.message ?: "Failed to load comments"
                }
            } catch (e: Exception) {
                errorMessage.value = "Error: ${e.message ?: "Unknown error"}"
            }
        }
    }

    fun createComment(boardId: Int, content: String) {
        if (!isLoggedIn.value) {
            errorMessage.value = "Login required to comment"
            return
        }
        val normalized = content.trim()
        if (normalized.isEmpty()) {
            errorMessage.value = "Comment cannot be empty"
            return
        }
        viewModelScope.launch {
            try {
                val res = repo.createComment(
                    CreateCommentRequest(
                        token = session.getToken(),
                        email = session.getEmail(),
                        board_id = boardId,
                        content = normalized,
                        timestamp = java.time.Instant.now().toString()
                    )
                )
                if (res.success) {
                    loadComments(boardId)
                    loadBoards()
                } else {
                    errorMessage.value = res.message ?: "Failed to create comment"
                }
            } catch (e: Exception) {
                errorMessage.value = "Error: ${e.message ?: "Unknown error"}"
            }
        }
    }

    fun voteComment(boardId: Int, commentId: Int, voteType: String) {
        if (!isLoggedIn.value) {
            errorMessage.value = "Login required to vote"
            return
        }
        viewModelScope.launch {
            try {
                val res = repo.voteComment(
                    boardId,
                    commentId,
                    VoteCommentRequest(
                        token = session.getToken(),
                        email = session.getEmail(),
                        vote_type = voteType
                    )
                )
                if (res.success) {
                    loadComments(boardId)
                } else {
                    errorMessage.value = res.message ?: "Failed to vote comment"
                }
            } catch (e: Exception) {
                errorMessage.value = "Error: ${e.message ?: "Unknown error"}"
            }
        }
    }

    fun loadProfile() {
        if (!isLoggedIn.value) {
            return
        }
        viewModelScope.launch {
            try {
                val email = session.getEmail()
                val res = repo.getProfile(
                    email,
                    AuthRequest(session.getToken(), email)
                )
                if (res.success && res.profile != null) {
                    profile.value = res.profile
                } else {
                    errorMessage.value = res.message ?: "Failed to load profile"
                }
            } catch (e: Exception) {
                errorMessage.value = "Error: ${e.message ?: "Unknown error"}"
            }
        }
    }
}