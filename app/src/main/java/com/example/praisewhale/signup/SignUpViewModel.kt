package com.example.praisewhale.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class SignUpViewModel : ViewModel() {

    val userName = MutableLiveData<String>("")
    val whaleName = MutableLiveData<String>("")

    private val _fcmToken = MutableLiveData<String>()
    val fcmToken : LiveData<String>
        get() = _fcmToken

    private val _isValid = MutableLiveData<Boolean>(true)
    val isValid : LiveData<Boolean>
        get() = _isValid

    private val _isAgree = MutableLiveData<Boolean>(false)
    val isAgree : LiveData<Boolean>
        get() = _isAgree

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

    fun allAgreeCheck() {
        _isAgree.value = !_isAgree.value!!
    }

    fun fcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("fcm", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            token?.let {
                _fcmToken.value = token
            }
        })
    }
}