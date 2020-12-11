package com.example.praisewhale.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.praisewhale.CollectionImpl
import com.example.praisewhale.R
import com.example.praisewhale.ResponseHomeData
import com.example.praisewhale.ResponsePraiseTargetData
import com.example.praisewhale.data.ResponseCollectionData
import com.example.praisewhale.util.MyApplication
import kotlinx.android.synthetic.main.dialog_negative.*
import kotlinx.android.synthetic.main.dialog_negative.view.*
import kotlinx.android.synthetic.main.dialog_positive.*
import kotlinx.android.synthetic.main.dialog_positive.view.*
import kotlinx.android.synthetic.main.dialog_positive_status.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates


class MainFragment : Fragment() {

    private lateinit var positiveAlertDialog: AlertDialog
    private lateinit var positiveStatusAlertDialog: AlertDialog
    private lateinit var negativeAlertDialog: AlertDialog
    private val sharedPreferencesKey = "CountNegative"
    private var praiseIndex by Delegates.notNull<Int>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    // ui 작업 수행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_positive.setOnClickListener(fragmentOnClickListener)
        button_negative.setOnClickListener(fragmentOnClickListener)

        val sharedPreferencesValue =
            MyApplication.mySharedPreferences.getValue(sharedPreferencesKey, "")

        if (sharedPreferencesValue.isEmpty()) {
            MyApplication.mySharedPreferences.setValue(sharedPreferencesKey, 0.toString())
        }

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
                        textView_praiseMission.text = it.data.daily_praise
                        textView_praiseDescription.text = it.data.mission_praise
                        val msgId = it.data.id
                    }
            }
        })
    }

    override fun onStart(){
        super.onStart()
        getServerPraiseData()
    }

    private val fragmentOnClickListener = View.OnClickListener {
        when (it.id) {
            R.id.button_positive -> {
                showPositiveDialog()
            }
            R.id.button_negative -> {
                showNegativeDialog()
                setNegativeDialog()
            }
            R.id.button_negativeOk -> {
                updateSharedPreferences()
                negativeAlertDialog.dismiss()
            }
            R.id.button_positiveOk -> {
                showPositiveStatusDialog()
                positiveAlertDialog.dismiss()
            }
            R.id.button_positiveStatusOk -> {
                positiveStatusAlertDialog.dismiss()
            }
            R.id.radioButton_dialogRecentPraiseTo_01 -> {
                positiveAlertDialog.editText_dialogPraiseTo.setText("남궁선규")
            }
            R.id.radioButton_dialogRecentPraiseTo_02 -> {
                positiveAlertDialog.editText_dialogPraiseTo.setText("안나영")
            }
            R.id.radioButton_dialogRecentPraiseTo_03 -> {
                positiveAlertDialog.editText_dialogPraiseTo.setText("최다인")
            }
        }
    }

    private fun showPositiveDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_positive, null)
        dialogView.button_positiveOk.setOnClickListener(fragmentOnClickListener)

        val alarDialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)

        positiveAlertDialog = alarDialogBuilder.create()
        positiveAlertDialog.show()

        positiveAlertDialog.radioButton_dialogRecentPraiseTo_01.setOnClickListener(fragmentOnClickListener)
        positiveAlertDialog.radioButton_dialogRecentPraiseTo_02.setOnClickListener(fragmentOnClickListener)
        positiveAlertDialog.radioButton_dialogRecentPraiseTo_03.setOnClickListener(fragmentOnClickListener)

        savePraise(positiveAlertDialog.editText_dialogPraiseTo.toString())
    }

    private fun showPositiveStatusDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_positive_status, null)
        dialogView.button_positiveStatusOk.setOnClickListener(fragmentOnClickListener)

        val alarDialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)

        positiveStatusAlertDialog = alarDialogBuilder.create()
        positiveStatusAlertDialog.show()
    }

    private fun showNegativeDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_negative, null)
        dialogView.button_negativeOk.setOnClickListener(fragmentOnClickListener)

        val alarDialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)

        negativeAlertDialog = alarDialogBuilder.create()
        negativeAlertDialog.show()
    }

    private fun setNegativeDialog() {
        val sharedPreferencesValue =
            MyApplication.mySharedPreferences.getValue(sharedPreferencesKey, "")

        if (sharedPreferencesValue.toInt() == 0 || sharedPreferencesValue.isEmpty()) {
            negativeAlertDialog.textView_dialogTitle.text = "내일은 꼭 칭찬해보아요!"
            negativeAlertDialog.imageView_dialogWhale.setImageResource(R.drawable.no_popup_img_dolphin)
            Log.d("TAG", "setNegativeDialog_01: $sharedPreferencesValue")

        } else if (sharedPreferencesValue.toInt() == 1) {
            negativeAlertDialog.textView_dialogTitle.text = "저 내일은 춤추고 싶어요!"
            negativeAlertDialog.imageView_dialogWhale.setImageResource(R.drawable.no_popup_2_img_dolphin)
            Log.d("TAG", "setNegativeDialog_02: $sharedPreferencesValue")

        } else if (sharedPreferencesValue.toInt() == 2) {
            negativeAlertDialog.textView_dialogTitle.text = "자꾸 이런 식이면\n" +
                    "나 고래고래 소리지를고래애애!"
            negativeAlertDialog.imageView_dialogWhale.setImageResource(R.drawable.no_popup_3_img_dolphin)

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
                    sharedPreferencesKey, 0.toString()
                )
                Log.d("TAG", "setNegativeDialog_07: $sharedPreferencesValue")
            }
        }
    }

    private fun getServerPraiseData() {
        val call: Call<ResponseHomeData> = CollectionImpl.service.getPraise()
        call.enqueue(object : Callback<ResponseHomeData> {
            override fun onFailure(call: Call<ResponseHomeData>, t: Throwable) {
                Log.d("tag", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<ResponseHomeData>,
                response: Response<ResponseHomeData>
            ) {
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let { it ->
                        praiseIndex = it.data.id
                        textView_praiseMission?.text = it.data.dailyPraise
                        textView_praiseDescription?.text = it.data.missionPraise

                        Log.d("tag", textView_praiseMission.text.toString())
                        Log.d("tag", it.toString())
                    }
                Log.d("tag", "onResponse: success")
            }
        })
    }

    private fun savePraise(target: String) {
        val call: Call<ResponsePraiseTargetData> = CollectionImpl.service.postUsers(ResponsePraiseTargetData(praiseIndex, target))
        call.enqueue(object : Callback<ResponsePraiseTargetData> {
            override fun onFailure(call: Call<ResponsePraiseTargetData>, t: Throwable) {
                Log.d("tag", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<ResponsePraiseTargetData>,
                response: Response<ResponsePraiseTargetData>
            ) {
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let { it ->

                    }
                Log.d("tag", "onResponse: success")
            }
        })
    }
}