package com.sopt.cherish.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import com.sopt27.praisewhale.R
import com.sopt27.praisewhale.databinding.LevelPopupBinding


//created by nayoung : 알람주기 설정 보여주는 팝업 창
class PraseLevelDialogFragment(
    @LayoutRes
    private val layoutResId: Int,var level :Int
) : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layoutResId, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCancelable(false)

        val binding = LevelPopupBinding.bind(view)

        when (level){
            1-> binding.levelPopText.setText("칭찬에 흥미가 생겼군요!")
            2-> binding.levelPopText.setText("칭찬에 익숙해지고 있네요!")
            3-> binding.levelPopText.setText("둠칫 두둠칫 점점 신이나요!")
            4-> binding.levelPopText.setText("저 춤 출래요, 말리지 마세요!")
            5-> {binding.levelPopText.setText("친구에게도 칭찬할고래를 알려줘요!")
                binding.textViewLv.setText("이제 만렙 고래!")
            }


        }



        val cancel: Button = view.findViewById(R.id.pop_ok_btn)
        cancel.setOnClickListener {
            dismiss()

        }

        return binding.root


    }



}