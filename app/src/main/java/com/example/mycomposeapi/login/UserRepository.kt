package com.example.mycomposeapi.login

import com.example.mycomposeapi.server.ApiInterface
import retrofit2.Response

class UserRepository {

    suspend fun loginUser(loginRequest:LoginRequest): Response<LoginResponse>? {
        return  ApiInterface.getApi()?.loginUser(loginRequest.email,loginRequest.password,"user")
    }
}