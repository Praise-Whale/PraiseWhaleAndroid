package com.example.praisewhale.home.ui.dialog

import android.content.res.Resources
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.example.praisewhale.R
import com.example.praisewhale.databinding.DialogHomeResultBinding
import com.example.praisewhale.util.showToast


class HomeDialogDoneResultFragment : DialogFragment() {

    private var _viewBinding: DialogHomeResultBinding? = null
    private val viewBinding get() = _viewBinding!!

    private var isLevelUp = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = DialogHomeResultBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setDialogContents()
    }

    override fun onResume() {
        super.onResume()
        setDialogBackground()
    }

    private fun setListeners() {
        viewBinding.buttonConfirm.setOnClickListener(dialogDoneClickListener)
    }

    private fun setDialogContents() {
        viewBinding.apply {
            imageButtonClose.visibility = View.INVISIBLE
            imageViewWhale.setImageResource(R.drawable.yes_5_img_whale)
            textViewTitle.text = "참 잘했고래!"
            textViewSubTitle.text = "내일도 칭찬해요!"
        }
    }

    private fun setDialogBackground() {
        val deviceWidth = Resources.getSystem().displayMetrics.widthPixels
        val dialogHorizontalMargin = (Resources.getSystem().displayMetrics.density * 42) * 2
        dialog!!.window!!.apply {
            setLayout((deviceWidth - dialogHorizontalMargin).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawableResource(R.drawable.background_rectangle_radius_15_stroke_2)
        }
    }

    private val dialogDoneClickListener = View.OnClickListener {
        when (it.id) {
            viewBinding.buttonConfirm.id -> {
                dialog!!.dismiss()
                showLevelUpToast(isLevelUp)
            }
        }
    }

    private fun showLevelUpToast(isLevelUp: Boolean) {
        if (isLevelUp) {
            requireContext().showToast("레벨업 되었어요! 고래를 확인해보세요!")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    class CustomDialogBuilder {
        private val dialog = HomeDialogDoneResultFragment()

        fun getLevelUpStatus(isLevelUp: Boolean): CustomDialogBuilder {
            dialog.isLevelUp = isLevelUp
            return this
        }

        fun create(): HomeDialogDoneResultFragment {
            return dialog
        }
    }
}