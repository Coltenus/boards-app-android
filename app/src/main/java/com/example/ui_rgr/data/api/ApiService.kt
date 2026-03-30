package com.example.ui_rgr.data.api

import com.example.ui_rgr.data.model.*
import retrofit2.http.Body
import retrofit2.http.HTTP
import retrofit2.http.Path
import retrofit2.http.POST

interface ApiService {

    @POST("/api/user/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
    
    @POST("/api/user/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("/api/board/list")
    suspend fun getBoards(@Body request: BoardListRequest): BoardListResponse

    @POST("/api/board/create")
    suspend fun createBoard(@Body request: CreateBoardRequest): BasicResponse

    @POST("/api/board/vote/{id}")
    suspend fun voteBoard(
        @Path("id") id: Int,
        @Body request: VoteBoardRequest
    ): BasicResponse

    @HTTP(method = "DELETE", path = "/api/board/delete/{id}", hasBody = true)
    suspend fun deleteBoard(
        @Path("id") id: Int,
        @Body request: AuthRequest
    ): BasicResponse

    @POST("/api/comment/{id}")
    suspend fun getComments(
        @Path("id") id: Int,
        @Body request: AuthRequest
    ): CommentListResponse

    @POST("/api/comment/create")
    suspend fun createComment(@Body request: CreateCommentRequest): BasicResponse

    @POST("/api/comment/vote/{board_id}/{comment_id}")
    suspend fun voteComment(
        @Path("board_id") boardId: Int,
        @Path("comment_id") commentId: Int,
        @Body request: VoteCommentRequest
    ): BasicResponse

    @POST("/api/user/profile/{email}")
    suspend fun getProfile(
        @Path("email") email: String,
        @Body request: AuthRequest
    ): ProfileResponse
}