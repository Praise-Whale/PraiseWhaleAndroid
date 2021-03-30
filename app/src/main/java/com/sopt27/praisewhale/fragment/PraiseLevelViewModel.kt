package com.sopt27.praisewhale.fragment

import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sopt27.praisewhale.CollectionImpl
import com.sopt27.praisewhale.R
import com.sopt27.praisewhale.ResponselevelData
import com.sopt27.praisewhale.util.MyApplication
import kotlinx.android.synthetic.main.fragment_praise_level.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PraiseLevelViewModel :ViewModel() {


    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    private val _userNickName = MutableLiveData<String>()
    val userNickName: LiveData<String> =_userNickName

    private val _whaleName = MutableLiveData<String>()
    val whaleName: LiveData<String> =_whaleName

    private val _levelDescription = MutableLiveData<String>()
    val levelDescription: LiveData<String> =_levelDescription


    private val _userToken = MutableLiveData<String>()
    val userToken: LiveData<String> =_userToken


    private val _praiseCount = MutableLiveData<String>()
    val praiseCount: LiveData<String> =_praiseCount

    private val _praiseNeedCount = MutableLiveData<String>()
    val praiseNeedCount: LiveData<String> =_praiseNeedCount

    private val _userGage = MutableLiveData<Int>()
    val userGage: LiveData<Int> =_userGage

    private val _userLevelCount = MutableLiveData<Int>()
    val userLevelCount: LiveData<Int> =_userLevelCount

    fun requestPraiseLevelData(){

        _userToken.value = MyApplication.mySharedPreferences.getValue("token", "")

        val call: Call<ResponselevelData> = CollectionImpl.service.getlevelcount(_userToken.value.toString())
        call.enqueue(object : Callback<ResponselevelData> {
            override fun onFailure(call: Call<ResponselevelData>, t: Throwable) {
                Log.d("tag", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<ResponselevelData>,
                response: Response<ResponselevelData>
            ) {

                response.takeIf { it.isSuccessful }

                    ?.body()
                    ?.let {

                            it ->

                       _praiseCount.value=it.data.praiseCount.toString()
                        _praiseNeedCount.value = it.data.levelUpNeedCount.toString() +"번"
                        _userLevelCount.value=it.data.userLevel
                        _whaleName.value=it.data.whaleName
                        _userNickName.value=it.data.nickName+"님의"

                    }
            }
        })
    }







}