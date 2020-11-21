package com.example.praisewhale.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.praisewhale.R
import com.example.praisewhale.api.CollectionImpl
import com.example.praisewhale.data.ResponseCollectionData
import com.example.praisewhale.util.MyApplication
import kotlinx.android.synthetic.main.dialog_negative.*
import kotlinx.android.synthetic.main.dialog_negative.view.*
import kotlinx.android.synthetic.main.dialog_positive.*
import kotlinx.android.synthetic.main.dialog_positive.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainFragment : Fragment() {

    private lateinit var alertDialog: AlertDialog
    private val sharedPreferencesKey = "CountNegative"


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

        val sharedPreferencesValue =
            MyApplication.mySharedPreferences.getValue(sharedPreferencesKey, "")

        if (sharedPreferencesValue.isEmpty()) {
            MyApplication.mySharedPreferences.setValue(sharedPreferencesKey, 0.toString())
        }

        val call : Call<ResponseCollectionData> = CollectionImpl.service.getPraise()
        call.enqueue(object : Callback<ResponseCollectionData> {
            override fun onFailure(call: Call<ResponseCollectionData>, t: Throwable) {
                Log.d("tag", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<ResponseCollectionData>,
                response: Response<ResponseCollectionData>
            ) {
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let { it ->
                        tv_main_msg?.text = it.data.daily_praise
                        tv_sub_msg?.text = it.data.mission_praise
                        val msgId = it.data.id
                    }
            }
        })
    }

    private val fragmentOnClickListener = View.OnClickListener {
        when (it.id) {
            R.id.button_mainPositive -> {
                showPositiveDialog()
            }
            R.id.button_mainNegative -> {
                showNegativeDialog()
                setNegativeDialog()
            }
            R.id.button_negativeOk -> {
                updateSharedPreferences()
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

        val call : Call<ResponseCollectionData> = CollectionImpl.service.getUsersPraise()
        call.enqueue(object : Callback<ResponseCollectionData> {
            override fun onFailure(call: Call<ResponseCollectionData>, t: Throwable) {
                Log.d("tag", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<ResponseCollectionData>,
                response: Response<ResponseCollectionData>
            ) {
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let { it ->

                    }
            }
        })

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

    private fun setNegativeDialog() {
        val sharedPreferencesValue =
            MyApplication.mySharedPreferences.getValue(sharedPreferencesKey, "")

        if (sharedPreferencesValue.toInt() == 0 || sharedPreferencesValue.isEmpty()) {
            alertDialog.textView_dialogTitle.text = "내일은 꼭 칭찬해보아요!"
            Log.d("TAG", "setNegativeDialog_01: $sharedPreferencesValue")

        } else if (sharedPreferencesValue.toInt() == 1) {
            alertDialog.textView_dialogTitle.text = "저 내일은 춤추고 싶어요!"
            Log.d("TAG", "setNegativeDialog_02: $sharedPreferencesValue")

        } else if (sharedPreferencesValue.toInt() == 2) {
            alertDialog.textView_dialogTitle.text = "자꾸 이런 식이면\n" +
                    "나 고래고래 소리지를고래애애!"
            Log.d("TAG", "setNegativeDialog_03: $sharedPreferencesValue")
        } else {
            Log.d("TAG", "checkSharedPreferences: error")
        }
    }

    private fun updateSharedPreferences() {
        val sharedPreferencesValue =
            MyApplication.mySharedPreferences.getValue(sharedPreferencesKey, "")

        if (sharedPreferencesValue.toInt() == 2) {
            MyApplication.mySharedPreferences.setValue(sharedPreferencesKey, "0")
            Log.d("TAG", "setNegativeDialog_05: $sharedPreferencesValue")
        } else {
            if (sharedPreferencesValue.isNotEmpty()) {
                MyApplication.mySharedPreferences.setValue(
                    sharedPreferencesKey,
                    ((sharedPreferencesValue.toInt() + 1).toString())
                )
                Log.d("TAG", "setNegativeDialog_06: $sharedPreferencesValue")
            } else {
                MyApplication.mySharedPreferences.setValue(
                    sharedPreferencesKey, 0.toString())
                Log.d("TAG", "setNegativeDialog_07: $sharedPreferencesValue")
            }
        }
    }
}