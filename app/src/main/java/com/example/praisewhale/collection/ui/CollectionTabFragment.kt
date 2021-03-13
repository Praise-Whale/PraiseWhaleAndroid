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
import com.example.praisewhale.collection.adapter.CollectionTabViewPager2Adapter
import com.example.praisewhale.databinding.FragmentCollectionTabBinding
import com.example.praisewhale.util.MyApplication
import com.example.praisewhale.util.setContextCompatTextColor


class CollectionTabFragment : Fragment() {

    private var _viewBinding: FragmentCollectionTabBinding? = null
    private val viewBinding get() = _viewBinding!!
    private lateinit var onBackPressedCallback: OnBackPressedCallback


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentCollectionTabBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUserInfo()
        setListeners()
        setViewPager2()
        setOnBackPressedCallBack()
    }

    override fun onResume() {
        super.onResume()
        initTabButton()
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
            adapter = CollectionTabViewPager2Adapter(this@CollectionTabFragment)
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

    fun showFragmentPraiseRankingCard() {
        (parentFragment as CollectionFragment).showFragmentPraiseRankingCard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }


    companion object {
        var SWITCH_HEIGHT = 0
    }
}