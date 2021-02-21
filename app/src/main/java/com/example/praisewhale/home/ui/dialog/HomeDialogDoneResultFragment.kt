package com.example.praisewhale.home.ui.dialog

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.praisewhale.R
import com.example.praisewhale.databinding.CustomToastHomeBinding
import com.example.praisewhale.databinding.DialogHomeResultBinding
import kotlinx.android.synthetic.main.custom_toast_home.*


class HomeDialogDoneResultFragment : DialogFragment() {

    private var _dialogResultViewBinding: DialogHomeResultBinding? = null
    private val dialogResultViewBinding get() = _dialogResultViewBinding!!
    private var isLevelUp = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _dialogResultViewBinding = DialogHomeResultBinding.inflate(layoutInflater)
        return dialogResultViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setDialogContents()
        setDialogBackground()
    }

    private fun setListeners() {
        dialogResultViewBinding.buttonConfirm.setOnClickListener(dialogDoneClickListener)
    }

    private fun setDialogContents() {
        dialogResultViewBinding.apply {
            imageViewWhale.setImageResource(R.drawable.yes_5_img_whale)
            textViewTitle.text = "참 잘했고래!"
            textViewSubTitle.text = "내일도 칭찬해요!"
        }
    }

    private fun setDialogBackground() {
        val dialogWidth = resources.getDimensionPixelSize(R.dimen.dialog_main_width)
        dialog!!.window!!.setLayout(dialogWidth, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog!!.window!!.setBackgroundDrawableResource(R.drawable.background_rectangle_radius_15_stroke)
    }

    private val dialogDoneClickListener = View.OnClickListener {
        when (it.id) {
            dialogResultViewBinding.buttonConfirm.id -> {
                dialog!!.dismiss()
                showLevelUpToast(isLevelUp)
            }
        }
    }

    private fun showLevelUpToast(isLevelUp: Boolean) {
        if (isLevelUp) {
            val toastViewBinding = CustomToastHomeBinding.inflate(layoutInflater)
            Toast(requireContext()).apply {
                view = toastViewBinding.constraintLayoutToastContainer
                toastViewBinding.textViewToastMessage.text = "레벨업 되었어요! 고래를 확인해보세요!"
                duration = Toast.LENGTH_LONG
                setGravity(Gravity.BOTTOM, 0, 250)
                show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _dialogResultViewBinding = null
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