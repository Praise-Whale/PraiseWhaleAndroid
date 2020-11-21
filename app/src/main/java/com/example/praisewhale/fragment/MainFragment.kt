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
import kotlinx.android.synthetic.main.dialog_negative.view.*
import kotlinx.android.synthetic.main.dialog_positive.*
import kotlinx.android.synthetic.main.dialog_positive.view.*
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
            }
            R.id.button_negativeOk -> {
                alertDialog.dismiss()
            }
            R.id.button_positiveOk -> {
                alertDialog.dismiss()
            }
        }
    }

    private fun showPositiveDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_positive, null)
        dialogView.button_positiveOk.setOnClickListener(fragmentOnClickListener)

        val alarDialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)

        alertDialog = alarDialogBuilder.create()
        alertDialog.show()
    }

    private fun showNegativeDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_negative, null)
        dialogView.button_negativeOk.setOnClickListener(fragmentOnClickListener)

        val alarDialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)

        alertDialog = alarDialogBuilder.create()
        alertDialog.show()
    }
}