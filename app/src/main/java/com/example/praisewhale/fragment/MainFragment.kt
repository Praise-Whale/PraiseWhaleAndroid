package com.example.praisewhale.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import com.example.praisewhale.R
import kotlinx.android.synthetic.main.dialog_positive.*
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment() {

    private lateinit var alertDialog: AlertDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    // ui 작업 수행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textView_mainPraiseMission.text = "칭찬 내용 / 칭찬 내용 / 칭찬 내용 / 칭찬 내용 / 칭찬 내용 / "
        Log.d("TAG", "onViewCreated: ${textView_mainPraiseMission.text}")
        button_mainPositive.setOnClickListener(fragmentOnClickListener)
        button_mainNegative.setOnClickListener(fragmentOnClickListener)
    }

    private val fragmentOnClickListener = View.OnClickListener {
        when (it.id) {
            R.id.button_mainPositive -> {
                showPositiveDialog()
            }
            R.id.button_mainNegative -> {
                showNegativeDialog()
                Log.d("TAG", "buttonNegative: clicked")
            }
        }
    }

    private fun showPositiveDialog() {
        val alarDialogBuilder = AlertDialog.Builder(context)
            .setView(layoutInflater.inflate(R.layout.dialog_positive, null))
            .setPositiveButton("확인", dialogClickListener)
            .setNegativeButton("취소", dialogClickListener)

        alertDialog = alarDialogBuilder.create()
        alertDialog.show()
        alertDialog.radioGroup_dialogRecentPraiseTo.setOnCheckedChangeListener(radioGroupListener)

    }

    private fun showNegativeDialog() {
        val alarDialogBuilder = AlertDialog.Builder(context)
            .setView(layoutInflater.inflate(R.layout.dialog_negative, null))
            .setPositiveButton("확인", dialogClickListener)

        alertDialog = alarDialogBuilder.create()
        alertDialog.show()
    }

    private val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                alertDialog.editText_dialogPraiseTo?.setText("")
                alertDialog.radioGroup_dialogRecentPraiseTo?.clearCheck()
            }
            DialogInterface.BUTTON_NEGATIVE -> {

            }
        }
    }

    private val radioGroupListener = RadioGroup.OnCheckedChangeListener { group, checkedId ->
//        checkCategoryColorValidation(group)
//        alartDialog.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = checkDialogValidation()
    }
}