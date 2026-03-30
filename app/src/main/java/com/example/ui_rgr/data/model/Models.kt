package com.example.ui_rgr.data.model

data class BoardListRequest(
    val token: String,
    val email: String
)

data class BoardListResponse(
    val success: Boolean,
    val boards: List<Board>
)

data class Board(
    val id: Int,
    val name: String,
    val email: String,
    val content: String,
    val create_time: String,
    val upvote_count: Int,
    val downvote_count: Int,
    val is_user_upvoted: Boolean,
    val is_user_downvoted: Boolean
)

data class CreateBoardRequest(
    val content: String,
    val token: String,
    val email: String,
    val timestamp: String
)

data class VoteBoardRequest(
    val token: String,
    val email: String,
    val voteType: String
)

data class AuthRequest(
    val token: String,
    val email: String
)

data class CreateCommentRequest(
    val token: String,
    val email: String,
    val board_id: Int,
    val content: String,
    val timestamp: String
)

data class VoteCommentRequest(
    val token: String,
    val email: String,
    val vote_type: String
)

data class Comment(
    val id: Int,
    val board_id: Int,
    val name: String,
    val email: String,
    val content: String,
    val create_time: String,
    val upvote_count: Int,
    val downvote_count: Int,
    val is_user_upvoted: Boolean,
    val is_user_downvoted: Boolean
)

data class CommentListResponse(
    val success: Boolean,
    val comments: List<Comment>,
    val message: String? = null
)

data class BasicResponse(
    val success: Boolean,
    val message: String? = null
)

data class LoginRequest(val email: String, val password: String)

data class LoginResponse(
    val success: Boolean,
    val user: User?,
    val message: String? = null
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val gender: String,
    val birthdate: String
)

data class RegisterResponse(
    val success: Boolean,
    val message: String? = null
)

data class User(
    val id: Int,
    val email: String,
    val token: String,
    val name: String
)

data class UserProfile(
    val id: Int,
    val email: String,
    val name: String,
    val gender: String,
    val birthdate: String,
    val joined: String
)

data class ProfileResponse(
    val success: Boolean,
    val profile: UserProfile?,
    val message: String? = null
)