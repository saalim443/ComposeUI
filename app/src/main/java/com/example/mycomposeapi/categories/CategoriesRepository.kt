package com.example.mycomposeapi.categories

import com.example.mycomposeapi.login.LoginRequest
import com.example.mycomposeapi.login.LoginResponse
import com.example.mycomposeapi.server.ApiInterface
import retrofit2.Response

class CategoriesRepository {
    suspend fun categoriesData(categoriesRequest: CategoriesRequest): Response<CategoriesResponse>? {
        return  ApiInterface.getApi()?.categoriesDataAPI(categoriesRequest.authorization)
    }
}