package com.example.praisewhale.collection.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.praisewhale.MainActivity
import com.example.praisewhale.R
import com.example.praisewhale.collection.adapter.CollectionViewPager2Adapter
import com.example.praisewhale.databinding.FragmentCollectionBinding
import com.example.praisewhale.util.MyApplication
import com.example.praisewhale.util.setContextCompatTextColor


class CollectionFragment : Fragment() {

    private var _viewBinding: FragmentCollectionBinding? = null
    private val viewBinding get() = _viewBinding!!
    private lateinit var onBackPressedCallback: OnBackPressedCallback


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentCollectionBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUserInfo()
        setListeners()
        setViewPager2()
        setOnBackPressedCallBack()
    }

    override fun onStart() {
        super.onStart()
        initTabButton()
        checkPreviousView()
    }

    override fun onResume() {
        super.onResume()
        if (!onBackPressedCallback.isEnabled) {
            onBackPressedCallback.isEnabled = true
        }
    }

    override fun onPause() {
        super.onPause()
        getSwitchHeight()
        onBackPressedCallback.isEnabled = false
    }

    private fun setUserInfo() {
        val userName = MyApplication.mySharedPreferences.getValue("nickName", "")
        viewBinding.tvCardTitleName.text = userName + "님의"
    }

    private fun setListeners() {
        viewBinding.apply {
            tabLeft.setOnClickListener(fragmentClickListener)
            tabRight.setOnClickListener(fragmentClickListener)
        }
    }

    private fun setViewPager2() {
        viewBinding.viewPager2Card.apply {
            adapter = CollectionViewPager2Adapter(this@CollectionFragment)
            isSaveEnabled = false
            isUserInputEnabled = false
        }
    }

    private fun setOnBackPressedCallBack() {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as MainActivity).showFinishToast()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    private fun initTabButton() {
        when (viewBinding.viewPager2Card.currentItem) {
            0 -> activateLeftTabButton()
            1 -> activateRightTabButton()
        }
    }

    private fun checkPreviousView() {
        viewBinding.viewPager2Card.apply {
            when (IS_FROM_PRAISE_RANKING_CARD_VIEW) {
                true -> {
                    activateRightTabButton()
                    setCurrentItem(1, false)
                    IS_FROM_PRAISE_RANKING_CARD_VIEW = false
                }
                false -> activateLeftTabButton()
            }
        }
    }

    private fun getSwitchHeight() {
        SWITCH_HEIGHT = viewBinding.switchTrack.height
    }

    private val fragmentClickListener = View.OnClickListener {
        viewBinding.apply {
            when (it.id) {
                tabLeft.id -> {
                    activateLeftTabButton()
                    viewPager2Card.setCurrentItem(0, true)
                }
                tabRight.id -> {
                    activateRightTabButton()
                    viewPager2Card.setCurrentItem(1, true)
                }
            }
        }
    }

    private fun activateLeftTabButton() {
        viewBinding.apply {
            tabLeft.isSelected = true
            tvTabLeft.setTextColor(Color.BLACK)
            tabRight.isSelected = false
            tvTabRight.setContextCompatTextColor(R.color.brown_grey)
        }
    }

    private fun activateRightTabButton() {
        viewBinding.apply {
            tabLeft.isSelected = false
            tvTabLeft.setContextCompatTextColor(R.color.brown_grey)
            tabRight.isSelected = true
            tvTabRight.setTextColor(Color.BLACK)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }


    companion object {
        var IS_FROM_PRAISE_RANKING_CARD_VIEW = false
        var SWITCH_HEIGHT = 0
    }
}