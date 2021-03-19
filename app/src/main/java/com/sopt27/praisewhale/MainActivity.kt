package com.sopt27.praisewhale

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sopt27.praisewhale.collection.ui.CollectionFragment
import com.sopt27.praisewhale.databinding.ActivityMainBinding
import com.sopt27.praisewhale.fragment.PraiseLevelFragment
import com.sopt27.praisewhale.home.ui.HomeFragment
import com.sopt27.praisewhale.util.showToast


class MainActivity : AppCompatActivity() {

    private var _viewBinding: ActivityMainBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val homeFragment by lazy { HomeFragment() }
    private val collectionFragment by lazy { CollectionFragment() }
    private val praiseLevelFragment by lazy { PraiseLevelFragment() }

    private var backPressedTime: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewBinding()
        initBottomNav()
    }

    private fun setViewBinding() {
        _viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }

    private fun initBottomNav() {
        viewBinding.bottomNavigationView.apply {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    menu.getItem(0).itemId -> showFragmentHome()
                    menu.getItem(1).itemId -> showFragmentCollection()
                    menu.getItem(2).itemId -> showFragmentPraiseLevel()
                }
                true
            }
            selectedItemId = menu.getItem(0).itemId
        }
    }

    private fun showFragmentHome() {
        supportFragmentManager.apply {
            when (fragments.contains(homeFragment)) {
                true -> {
                    beginTransaction().show(homeFragment).commit()
                    beginTransaction().hide(collectionFragment).commit()
                    beginTransaction().hide(praiseLevelFragment).commit()
                }
                false -> beginTransaction().add(viewBinding.frameLayoutFragmentContainer.id, homeFragment).commit()
            }
        }
    }

    private fun showFragmentCollection() {
        supportFragmentManager.apply {
            when (fragments.contains(collectionFragment)) {
                true -> {
                    beginTransaction().hide(homeFragment).commit()
                    beginTransaction().show(collectionFragment).commit()
                    beginTransaction().hide(praiseLevelFragment).commit()
                }
                false -> beginTransaction().add(viewBinding.frameLayoutFragmentContainer.id, collectionFragment).commit()
            }
        }
    }

    private fun showFragmentPraiseLevel() {
        supportFragmentManager.apply {
            when (fragments.contains(praiseLevelFragment)) {
                true -> {
                    beginTransaction().hide(homeFragment).commit()
                    beginTransaction().hide(collectionFragment).commit()
                    beginTransaction().show(praiseLevelFragment).commit()
                }
                false -> beginTransaction().add(viewBinding.frameLayoutFragmentContainer.id, praiseLevelFragment).commit()
            }
        }
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(viewBinding.frameLayoutFragmentContainer.id, fragment).commit()
    }

    fun showFinishToast() {
        if (System.currentTimeMillis() - backPressedTime < 2000) {
            finish()
            return
        }
        showToast("'뒤로' 버튼을 한번 더 누르시면 앱이 종료됩니다.")
        backPressedTime = System.currentTimeMillis()
    }
}