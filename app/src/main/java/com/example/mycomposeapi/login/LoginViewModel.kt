package com.example.mycomposeapi.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mycomposeapi.util.BaseResponse
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepo = UserRepository()
    val loginResult: LiveData<BaseResponse<LoginResponse>> = MutableLiveData()

    fun loginUser(email: String, pwd: String) {
        (loginResult as MutableLiveData).value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(password = pwd, email = email)
                val response = userRepo.loginUser(loginRequest = loginRequest)
                if (response?.code() == 200) {
                    (loginResult as MutableLiveData).value = BaseResponse.Success(response.body())
                } else {
                    (loginResult as MutableLiveData).value = BaseResponse.Error(response?.message())
                }
            } catch (ex: Exception) {
                (loginResult as MutableLiveData).value = BaseResponse.Error(ex.message)
            }
        }
    }
}
