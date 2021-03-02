package com.example.praisewhale.collection.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.praisewhale.MainActivity
import com.example.praisewhale.collection.ui.CollectionFragment.Companion.IS_FROM_PRAISE_RANKING_CARD_VIEW
import com.example.praisewhale.collection.ui.CollectionFragment.Companion.SWITCH_HEIGHT
import com.example.praisewhale.collection.ui.PraiseRankingFragment.Companion.PRAISE_TARGET
import com.example.praisewhale.databinding.FragmentPraiseRankingCardBinding
import com.example.praisewhale.util.MyApplication


class PraiseRankingCardFragment : Fragment() {

    private var _viewBinding: FragmentPraiseRankingCardBinding? = null
    private val viewBinding get() = _viewBinding!!
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    private val sharedPreferences = MyApplication.mySharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentPraiseRankingCardBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUserInfo()
        setListeners()
        setButtonPraiseTargetHeight()
        setOnBackPressedCallBack()
    }

    private fun setUserInfo() {
        viewBinding.apply {
            textViewUserName.text = "${sharedPreferences.getValue("nickName", "")}님의"
            buttonPraiseTarget.text = PRAISE_TARGET
        }
    }

    private fun setListeners() {
        viewBinding.imageButtonBack.setOnClickListener(fragmentOnClickListener)
    }

    private fun setButtonPraiseTargetHeight() {
        viewBinding.buttonPraiseTarget.height = SWITCH_HEIGHT
    }

    private fun setOnBackPressedCallBack() {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { backToPraiseRankingView() }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    private fun backToPraiseRankingView() {
        (activity as MainActivity).changeFragment(CollectionFragment())
        IS_FROM_PRAISE_RANKING_CARD_VIEW = true
    }

    private val fragmentOnClickListener = View.OnClickListener {
        viewBinding.apply {
            when (it.id) {
                imageButtonBack.id -> backToPraiseRankingView()
            }
        }
    }
}