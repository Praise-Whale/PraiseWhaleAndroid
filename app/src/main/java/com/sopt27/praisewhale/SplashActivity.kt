package com.sopt27.praisewhale

import android.animation.Animator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.sopt27.praisewhale.data.RequestSignIn
import com.sopt27.praisewhale.data.ResponseToken
import com.sopt27.praisewhale.databinding.ActivitySplashBinding
import com.sopt27.praisewhale.onboarding.OnBoardingActivity
import com.sopt27.praisewhale.signup.SignUpActivity
import com.sopt27.praisewhale.util.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySplashBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_splash)

        setStatusBarColor(this@SplashActivity)
        startAnimation(binding)
        setLottieListener(binding)
    }

    private fun startAnimation(binding: ActivitySplashBinding) {
        binding.lottieSplash.playAnimation()
    }

    private fun setLottieListener(binding: ActivitySplashBinding) {
        binding.lottieSplash.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {
            }

            override fun onAnimationEnd(p0: Animator?) {
                if (!MyApplication.mySharedPreferences.getBooleanValue("onBoarding", false)) {
                    toOnBoarding()
                } else {
                    val nickname = MyApplication.mySharedPreferences.getValue("nickName", "")
                    if (nickname == "") {
                        toSignUp()
                    } else {
                        signIn(nickname)
                    }
                }
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {
            }
        })
    }

    private fun setStatusBarColor(context: Activity) {
        val window: Window = context.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(context, R.color.white)
    }

    private fun toOnBoarding() {
        startActivity(Intent(this@SplashActivity, OnBoardingActivity::class.java))
        finish()
    }

    private fun toSignUp() {
        startActivity(Intent(this@SplashActivity, SignUpActivity::class.java))
        finish()
    }

    private fun signIn(nickname: String) {
        val body = RequestSignIn(nickName = nickname)
        val call: Call<ResponseToken> = CollectionImpl.service.signIn(body)
        call.enqueue(object : Callback<ResponseToken> {
            override fun onFailure(call: Call<ResponseToken>, t: Throwable) {
                Log.d("response", t.localizedMessage!!)
            }

            override fun onResponse(
                call: Call<ResponseToken>,
                response: Response<ResponseToken>
            ) {
                Log.d("response", response.body().toString())
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let {
                        MyApplication.mySharedPreferences.setValue("token", it.data.accessToken)
                        MyApplication.mySharedPreferences.setValue(
                            "refreshToken",
                            it.data.refreshToken
                        )
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish()
                    } ?: Toast.makeText(this@SplashActivity, "error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}