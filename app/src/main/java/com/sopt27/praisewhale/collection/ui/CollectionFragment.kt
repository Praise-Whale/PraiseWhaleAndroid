package com.sopt27.praisewhale.collection.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.sopt27.praisewhale.MainActivity
import com.sopt27.praisewhale.collection.adapter.CollectionViewPager2Adapter
import com.sopt27.praisewhale.databinding.FragmentCollectionBinding


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
        setViewPager2()
        setOnBackPressedCallBack()
        showFragmentCollectionTab()
    }

    override fun onResume() {
        super.onResume()
        if (!onBackPressedCallback.isEnabled) {
            onBackPressedCallback.isEnabled = true
        }
    }

    override fun onPause() {
        super.onPause()
        onBackPressedCallback.isEnabled = false
    }

    private fun setViewPager2() {
        viewBinding.viewPager2.apply {
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

    fun showFragmentCollectionTab() {
        viewBinding.viewPager2.setCurrentItem(0, false)
    }

    fun showFragmentPraiseRankingCard() {
        viewBinding.viewPager2.setCurrentItem(1, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}