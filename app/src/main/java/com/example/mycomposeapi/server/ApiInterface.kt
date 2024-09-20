package com.example.mycomposeapi.server

import com.example.mycomposeapi.categories.CategoriesResponse
import com.example.mycomposeapi.login.LoginResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {



    @POST("api/users/login")
    suspend fun loginUser(@Query("email") email: String , @Query("password") password: String,@Query("role") role: String): Response<LoginResponse>


    @GET("api/vehicles/categories")
    suspend fun categoriesDataAPI(@Header("Authorization") token: String ): Response<CategoriesResponse>
    companion object {
        fun getApi(): ApiInterface? {
            return ApiClient.client?.create(ApiInterface::class.java)
        }
    }
}