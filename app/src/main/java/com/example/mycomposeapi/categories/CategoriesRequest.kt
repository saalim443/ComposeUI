package com.example.mycomposeapi.categories

import com.google.gson.annotations.SerializedName

data class CategoriesRequest (
     @SerializedName("Authorization")
     var authorization: String
)
