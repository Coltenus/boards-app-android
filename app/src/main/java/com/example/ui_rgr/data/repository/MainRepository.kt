package com.example.ui_rgr.data.repository

import com.example.ui_rgr.data.api.RetrofitClient
import com.example.ui_rgr.data.model.*

class MainRepository {

    suspend fun login(email: String, password: String) =
        RetrofitClient.api.login(LoginRequest(email, password))
    
    suspend fun register(req: RegisterRequest) =
        RetrofitClient.api.register(req)

    suspend fun getBoards(token: String, email: String) =
        RetrofitClient.api.getBoards(BoardListRequest(token, email))

    suspend fun createBoard(req: CreateBoardRequest) =
        RetrofitClient.api.createBoard(req)

    suspend fun voteBoard(id: Int, req: VoteBoardRequest) =
        RetrofitClient.api.voteBoard(id, req)

    suspend fun deleteBoard(id: Int, req: AuthRequest) =
        RetrofitClient.api.deleteBoard(id, req)

    suspend fun getComments(boardId: Int, req: AuthRequest) =
        RetrofitClient.api.getComments(boardId, req)

    suspend fun createComment(req: CreateCommentRequest) =
        RetrofitClient.api.createComment(req)

    suspend fun voteComment(boardId: Int, commentId: Int, req: VoteCommentRequest) =
        RetrofitClient.api.voteComment(boardId, commentId, req)

    suspend fun getProfile(email: String, req: AuthRequest) =
        RetrofitClient.api.getProfile(email, req)
}