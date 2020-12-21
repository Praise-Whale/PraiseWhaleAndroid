package com.example.praisewhale.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {

    val userName = MutableLiveData<String>("")
    val whaleName = MutableLiveData<String>("")

    private val _isValid = MutableLiveData<Boolean>(true)
    val isValid : LiveData<Boolean>
        get() = _isValid

    fun resetUserName() {
        userName.value = ""
    }

    fun resetWhaleName() {
        whaleName.value = ""
    }

    fun nameFail() {
        _isValid.value = false
    }

    fun validReset() {
        _isValid.value = true
    }
}