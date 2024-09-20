package com.example.mycomposeapi.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mycomposeapi.util.BaseResponse
import kotlinx.coroutines.launch

class CategoriesViewModel(application: Application) : AndroidViewModel(application) {

    private val categoriesRepository = CategoriesRepository()
    private val _categoriesResult = MutableLiveData<BaseResponse<CategoriesResponse>>()
    val categoriesResult: LiveData<BaseResponse<CategoriesResponse>> get() = _categoriesResult

    fun fetchCategories(token: String) {
        _categoriesResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val categoriesRequest = CategoriesRequest("Bearer $token")
                val response = categoriesRepository.categoriesData(categoriesRequest)
                if (response?.code() == 200) {
                    _categoriesResult.value = BaseResponse.Success(response.body())
                } else {
                    _categoriesResult.value = BaseResponse.Error(response?.message() ?: "Unknown error")
                }
            } catch (ex: Exception) {
                _categoriesResult.value = BaseResponse.Error(ex.message ?: "Unknown error")
            }
        }
    }
}
