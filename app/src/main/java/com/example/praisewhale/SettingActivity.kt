package com.example.praisewhale

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.praisewhale.data.RequestAlarm
import com.example.praisewhale.data.RequestNickChange
import com.example.praisewhale.data.ResponseData
import com.example.praisewhale.data.home.ResponseNickChange
import com.example.praisewhale.util.MyApplication
import com.example.praisewhale.util.Vibrate
import com.example.praisewhale.util.showToast
import com.example.praisewhale.util.textChangedListener
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import kotlinx.android.synthetic.main.activity_setting.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingActivity :AppCompatActivity() {

    lateinit var btnCancel      : Button   // 닫기
    lateinit var btnOk          : Button   // 확인

    lateinit var npHour         : NumberPicker //시간 넘버픽커
    lateinit var npMinute       : NumberPicker //분 넘버 픽커

    lateinit var mDisplayedValuesHr     : MutableList<String>
    lateinit var mDisplayedValuesMin    : MutableList<String>
    var tvalarmtimetoast=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)




        layout_change_nickname.setOnClickListener {
            NicknameDialog()
        }

        layout_alarm.setOnClickListener {

            AlarmClockDialog()


        }

        switch_alarm.setOnCheckedChangeListener { buttonView, isChecked ->
            registerAlarm(isChecked)

            if(!MyApplication.mySharedPreferences.getBooleanValue(
                    "alarm_onoff",
                    isChecked
                )
            ){
                if(isChecked){
                    showToast("앞으로 " + tv_alarm_time.text +" 에 칭찬 알림을 보내드릴게요!")
                }
            }
            MyApplication.mySharedPreferences.setBooleanValue(
                "alarm_onoff",
                isChecked
            )

        }




        layout_service.setOnClickListener {
            //val intent= Intent(this, InfoUserActivity::class.java)
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.notion.so/8ced90e384b1417ab6e24ce9c8436ab8")
            )
           // startActivity(intent)

            startActivity(intent)
        }

        layout_personal_information.setOnClickListener {
            //val intent= Intent(this, InfoActivity::class.java)
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.notion.so/4ae478f551f249d097a6e46cffad6d07")
            )
            startActivity(intent)
           // startActivity(intent)
        }
        layout_developer.setOnClickListener {
            val intent= Intent(this, DeveloperActivity::class.java)
            startActivity(intent)
        }
        btn_close.setOnClickListener {
            onBackPressed()
            finish()
        }

        layout_ask.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_receiver)))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_title))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_content))
            intent.type = "message/rfc822"
            startActivity(intent)
        }

        layout_open_source.setOnClickListener {
            startActivity(Intent(this, OssLicensesMenuActivity::class.java))
            OssLicensesMenuActivity.setActivityTitle("Ossl Title")
        }
    }

    override fun onResume() {
        super.onResume()
        tv_nickname.text=MyApplication.mySharedPreferences.getValue("nickName", "")

        tv_alarm_time.text=MyApplication.mySharedPreferences.getValue("alarm_time","오전 9:00")
        switch_alarm.isChecked=MyApplication.mySharedPreferences.getBooleanValue("alarm_onoff",true)
    }



    fun AlarmClockDialog(){
        val dialog2 = AlertDialog.Builder(this).create()
        val edialog2: LayoutInflater = LayoutInflater.from(this)
        val mView2: View = edialog2.inflate(R.layout.numberpicker_time, null)

        val ny:NumberPicker=mView2.findViewById(R.id.numberPicker)
        val ny1:NumberPicker=mView2.findViewById(R.id.numberPicker2)
        val ny2:NumberPicker=mView2.findViewById(R.id.numberPicker3)

        val btnok:Button=mView2.findViewById(R.id.button_ok_time)
        val btnalarmcancel:ImageButton=mView2.findViewById(R.id.button_time_cancel)

        btnalarmcancel.setOnClickListener {
            dialog2.dismiss()
            dialog2.cancel()
        }


        val list = resources.getStringArray(R.array.ampm)
        //val list_minute=resources.getStringArray(R.array.minute)

        val list_minute = arrayOfNulls<String>(60)
        //list_minute[0] = "00"
        for (i in 0..59) {
            if(i<10){
                list_minute[i] = "0$i"
            }else{
                list_minute[i]=i.toString()
            }
        }
        val list_hour = arrayOfNulls<String>(13)
        //list_minute[0] = "00"
        for (i in 0..12) {

                list_hour[i] = (i+1).toString()

        }



        ny.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        ny2.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        ny1.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        ny.displayedValues = list
        ny1.displayedValues=list_hour
        ny2.displayedValues=list_minute

        ny.minValue=0
        ny.maxValue=list.size-1

        ny1.minValue=0
        ny1.maxValue=list_hour.size-2

        ny2.minValue=0
        ny2.maxValue=list_minute.size-1



        for (i in list_minute.indices) {
            if (tv_alarm_time.text.split(" ")[1].split(":")[1] == list_minute[i]){
            ny2.value = i}
        }

        for (i in list.indices) {
            if (tv_alarm_time.text.split(" ")[0] == list[i]){
                ny.value = i}
        }
        for (i in list_hour.indices) {
            if (tv_alarm_time.text.split(" ")[1].split(":")[0] == list_hour[i]){
                ny1.value = i}
        }

        btnok.setOnClickListener {

            if(list[ny.value]=="오전"){
                tvalarmtimetoast=list[ny.value]+" "+(ny1.value+1).toString() + ":" +list_minute[ny2.value].toString()
//                    tv_alarm_time.text =list[ny.value]+" "+ny1.value.toString() + ":0" + ny2.value.toString()
                tv_alarm_time.text=tvalarmtimetoast

                MyApplication.mySharedPreferences.setValue(
                    "alarm_hour",
                    (ny1.value+1).toString()
                )
                Log.d("apple",(ny1.value+1).toString())
                MyApplication.mySharedPreferences.setValue(
                    "alarm_minute",
                    list_minute[ny2.value].toString()
                )
                Log.d("appless", list_minute[ny2.value].toString())
                MyApplication.mySharedPreferences.setValue(
                    "alarm_time",
                    list[ny.value]+" "+(ny1.value+1).toString() + ":" +list_minute[ny2.value].toString()
                )

            }else{
                tvalarmtimetoast=list[ny.value]+" "+(ny1.value+1).toString() + ":" +list_minute[ny2.value].toString()
                tv_alarm_time.text=tvalarmtimetoast

                val clock_ = (ny1.value+1) + 12
                // tv_alarm_time.text =list[ny.value]+" "+clock_.toString() + ":0" + ny2.value.toString()
                MyApplication.mySharedPreferences.setValue(
                    "alarm_hour",
                    clock_.toString()
                )
                Log.d("apple",clock_.toString())

                MyApplication.mySharedPreferences.setValue(
                    "alarm_minute",
                    list_minute[ny2.value].toString()
                )
                Log.d("appless", list_minute[ny2.value].toString())

                MyApplication.mySharedPreferences.setValue(
                    "alarm_time",
                    list[ny.value]+" "+(ny1.value+1).toString() + ":" +list_minute[ny2.value].toString()
                )

            }
            /*if ((ny2.value.toString()).length < 2) {
                if (list[ny.value] == "오전") {
                    tvalarmtimetoast=list[ny.value]+" "+(ny1.value+1).toString() + ":" +list_minute[ny2.value].toString()
//                    tv_alarm_time.text =list[ny.value]+" "+ny1.value.toString() + ":0" + ny2.value.toString()
                    tv_alarm_time.text=tvalarmtimetoast


                    MyApplication.mySharedPreferences.setValue(
                        "alarm_hour",
                        (ny1.value+1).toString()
                    )
                    Log.d("apple",(ny1.value+1).toString())
                    MyApplication.mySharedPreferences.setValue(
                        "alarm_minute",
                        list_minute[ny2.value].toString()
                    )

                    MyApplication.mySharedPreferences.setValue(
                        "alarm_time",
                        list[ny.value]+" "+(ny1.value+1).toString() + ":" +list_minute[ny2.value].toString()
                    )


                } else {
                    tvalarmtimetoast=list[ny.value]+" "+(ny1.value+1).toString() + ":" +list_minute[ny2.value].toString()
                    tv_alarm_time.text=tvalarmtimetoast

                    val clock_ = (ny1.value+1) + 12
                   // tv_alarm_time.text =list[ny.value]+" "+clock_.toString() + ":0" + ny2.value.toString()
                    MyApplication.mySharedPreferences.setValue(
                        "alarm_hour",
                        clock_.toString()
                    )
                    Log.d("apple",clock_.toString())

                    MyApplication.mySharedPreferences.setValue(
                        "alarm_minute",
                        list_minute[ny2.value].toString()
                    )

                    MyApplication.mySharedPreferences.setValue(
                        "alarm_time",
                        list[ny.value]+" "+(ny1.value+1).toString() + ":" +list_minute[ny2.value].toString()
                    )
                }
            } else {
                if (list[ny.value] == "오후") {
                    tvalarmtimetoast=list[ny.value]+" "+(ny1.value+1).toString() + ":" +list_minute[ny2.value].toString()
                    tv_alarm_time.text=tvalarmtimetoast

                    val clock_ = (ny1.value+1) + 12
                   // tv_alarm_time.text =list[ny.value]+" "+clock_.toString() + ":" + ny2.value.toString()
                    MyApplication.mySharedPreferences.setValue(
                        "alarm_hour",
                        clock_.toString()
                    )
                    MyApplication.mySharedPreferences.setValue(
                        "alarm_minute",
                        list_minute[ny2.value].toString()
                    )

                    MyApplication.mySharedPreferences.setValue(
                        "alarm_time",
                        list[ny.value]+" "+(ny1.value+1).toString() + ":" +list_minute[ny2.value].toString()
                    )

                } else {
                    tvalarmtimetoast=list[ny.value]+" "+(ny1.value+1).toString() + ":" +list_minute[ny2.value].toString()
                    tv_alarm_time.text=tvalarmtimetoast
                   // tv_alarm_time.text =list[ny.value]+" "+ny1.value.toString() + ":" + ny2.value.toString()
                    MyApplication.mySharedPreferences.setValue(
                        "alarm_hour",
                        (ny1.value+1).toString()
                    )
                    MyApplication.mySharedPreferences.setValue(
                        "alarm_minute",
                        list_minute[ny2.value].toString()
                    )

                    MyApplication.mySharedPreferences.setValue(
                        "alarm_time",
                        list[ny.value]+" "+(ny1.value+1).toString() + ":" +list_minute[ny2.value].toString()
                    )
                }
            }*/
            dialog2.dismiss()
            showToast("앞으로 " + tvalarmtimetoast + " 에 칭찬 알림을 보내드릴게요!")
        }

        val color = ColorDrawable(Color.TRANSPARENT)
        // Dialog 크기 설정
        val inset = InsetDrawable(color, 85)
        dialog2.window?.setBackgroundDrawable(inset)
        dialog2.setCancelable(false)
        dialog2.setView(mView2)
        dialog2.show()


    }


    fun NicknameDialog(){
        /* val dlg = NicknameDialog(this)
            dlg.start()*/
        val dialog = AlertDialog.Builder(this).create()
        val edialog: LayoutInflater = LayoutInflater.from(this)

        val mView: View = edialog.inflate(R.layout.namechange, null)
        //키보드 올라오기
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        val close: ImageButton = mView.findViewById(R.id.close_btn_nick)
        close.setOnClickListener {
            dialog.dismiss()
            dialog.cancel()
        }
        val nick_modify: Button = mView.findViewById(R.id.change_btn)
        val deletebtn: Button = mView.findViewById(R.id.delete_btn)

        val nick_modify_edit: EditText = mView.findViewById(R.id.editnickname)
        nick_modify_edit.hint=MyApplication.mySharedPreferences.getValue("nickName","")


        nick_modify_edit.setOnEditorActionListener { textView, action, event ->
            var handled = false
            if (action == EditorInfo.IME_ACTION_DONE) {
                changeNickname(nick_modify, nick_modify_edit, dialog, mView)
                handled = true
            }
            handled
        }

        nick_modify_edit.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val textcount: TextView = mView.findViewById(R.id.textcount)
                    if(nick_modify_edit.text.toString()==""){
                        textcount.setTextColor(Color.parseColor("#9c9c9c"))
                        val changebtn: Button=mView.findViewById(R.id.change_btn)
                        changebtn.setBackgroundResource(R.drawable.popup_btn_bg_init)

                    }
                }
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) { val textcount: TextView = mView.findViewById(R.id.textcount)
                    if(nick_modify_edit.text.toString()==""){
                        textcount.setTextColor(Color.parseColor("#9c9c9c"))

                    }
                    nick_modify.isClickable=true


                    textcount.isVisible=true
                    val textcount7:TextView=mView.findViewById(R.id.textcount7)
                    textcount7.isVisible=true
                    val existnickbg:ConstraintLayout=mView.findViewById(R.id.editTextTextPersonName)
                    existnickbg.setBackgroundResource(R.drawable.edittext_bg)
                    val existnick:TextView=mView.findViewById(R.id.existingnick)
                    existnick.isVisible=false
                    val changebtn: Button=mView.findViewById(R.id.change_btn)
                    changebtn.setBackgroundResource(R.drawable.popup_btn_bg)
                    val input: String = s.toString()
                    textcount.setTextColor(Color.parseColor("#503000"))
                    textcount.text=input.length.toString()
                }
            })
        nick_modify_edit.textChangedListener {

            deletebtn.isVisible = nick_modify_edit.text.toString() != ""
            nick_modify.isClickable=true

        }

        deletebtn.setOnClickListener {

            nick_modify_edit.setText("")
            deletebtn.isVisible=false
            nick_modify.isClickable=false
            val textcount: TextView = mView.findViewById(R.id.textcount)
            textcount.setTextColor(Color.parseColor("#9c9c9c"))
            nick_modify.setBackgroundResource(R.drawable.popup_btn_bg_init)

        }
        nick_modify.setOnClickListener {
            changeNickname(nick_modify, nick_modify_edit, dialog, mView)
        }

        val color = ColorDrawable(Color.TRANSPARENT)
        // Dialog 크기 설정
        val inset = InsetDrawable(color, 85)
        dialog.window?.setBackgroundDrawable(inset)
        dialog.setCancelable(false)
        dialog.setView(mView)
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun changeNickname(nick_modify: Button, nick_modify_edit: EditText, dialog: AlertDialog, mView: View) {
        val token = MyApplication.mySharedPreferences.getValue("token", "")

        val body=RequestNickChange(
            nickName = MyApplication.mySharedPreferences.getValue("nickName", ""),
            newNickName = nick_modify_edit.text.toString()
        )
        val call : Call<ResponseNickChange> = CollectionImpl.service.nickchange(token, body)
        call.enqueue(object : Callback<ResponseNickChange> {
            override fun onFailure(call: Call<ResponseNickChange>, t: Throwable) {
                Log.d("tag", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<ResponseNickChange>,
                response: Response<ResponseNickChange>
            ) {

                if(!response.isSuccessful){
                    Log.d("status코드2", response.body()?.status.toString())
                    Log.d("닉네임 중복", "닉네임 중복")
                    Vibrate.startVibrate(context = this@SettingActivity)
                    val existnick:TextView=mView.findViewById(R.id.existingnick)
                    existnick.isVisible=true
                    val existnickbg:ConstraintLayout=mView.findViewById(R.id.editTextTextPersonName)
                    existnickbg.setBackgroundResource(R.drawable.edittext_bg_exist)
                    val textcount:TextView=mView.findViewById(R.id.textcount)
                    textcount.isVisible=false
                    val textcount7:TextView=mView.findViewById(R.id.textcount7)
                    textcount7.isVisible=false
                    nick_modify.isClickable=false
                    nick_modify.setBackgroundResource(R.drawable.popup_btn_bg_init)
                }
                response.takeIf { it.isSuccessful }

                    ?.body()
                    ?.let {
                        Log.d("status코드", it.status.toString())

                        if(it.status == 200) {
                            nick_modify.isClickable=true
                            val existnickbg:ConstraintLayout=mView.findViewById(R.id.editTextTextPersonName)
                            existnickbg.setBackgroundResource(R.drawable.edittext_bg)
                            Log.d("닉네임변경완료", "닉네임변경완료")

                            tv_nickname.text=nick_modify_edit.text.toString()
                            dialog.dismiss()
                            dialog.cancel()
                            showToast("닉네임이 변경되었어요!")

                            MyApplication.mySharedPreferences.setValue(
                                "nickName",
                                nick_modify_edit.text.toString()
                            )
                        }
                        else {

                            //MyApplication.mySharedPreferences.setValue("alarmtime")
                        }

                    }
            }
        })
    }

    private fun registerAlarm(isChecked : Boolean) {
        val body = RequestAlarm(alarmSet = isChecked)
        val token = MyApplication.mySharedPreferences.getValue("token", "")
        val call: Call<ResponseData> = CollectionImpl.service.registerAlarm(token, body)
        call.enqueue(object : Callback<ResponseData> {
            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                Log.d("response", t.localizedMessage!!)
            }

            override fun onResponse(
                call: Call<ResponseData>,
                response: Response<ResponseData>
            ) {
                Log.d("response", response.body().toString())
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let {
                    } ?: Toast.makeText(this@SettingActivity, "error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
