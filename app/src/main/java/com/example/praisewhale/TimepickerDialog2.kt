package com.example.praisewhale

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_setting.*
import java.util.*

class TimepickerDialog2(context: Context): DialogFragment(),TimePickerDialog.OnTimeSetListener{


    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var lblDesc : TextView
    private lateinit var btnOK : Button
    private lateinit var btnCancel : Button
    private lateinit var listener : MyDialogOKClickedListener

    private lateinit var nnedit : EditText

    private lateinit var calendar: Calendar




    fun start() {
        calendar = Calendar.getInstance()

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)


        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.time_picker)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        btnOK=dlg.findViewById(R.id.button)//알람 설정 버튼

        btnOK.setOnClickListener {

        }







        btnCancel = dlg.findViewById(R.id.button2)
        btnCancel.setOnClickListener {
            dlg.dismiss()
        }

        dlg.show()
    }


    fun setOnOKClickedListener(listener: (String) -> Unit) {
        this.listener = object: MyDialogOKClickedListener {
            override fun onOKClicked(content: String) {
                listener(content)

            }
        }
    }

    interface MyDialogOKClickedListener {
        fun onOKClicked(content : String)


    }
    private fun getHourAMPM(hour: Int): Int {
        var modifiedHour = if (hour > 11) hour - 12 else hour
        if (modifiedHour == 0) {
            modifiedHour = 12
        }
        return modifiedHour
    }
    private fun getAMPM(hour: Int): String {
        return if (hour > 11) "PM" else "AM"
    }


     override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
         TODO("Not yet implemented")
         tv_alarm_time.text = "${p1}시 ${p2}분"

     }
}