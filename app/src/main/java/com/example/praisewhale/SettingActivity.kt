package com.example.praisewhale

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.praisewhale.data.RequestNickChange
import com.example.praisewhale.data.home.ResponseNickChange
import com.example.praisewhale.util.MyApplication
import com.example.praisewhale.util.textChangedListener
import kotlinx.android.synthetic.main.activity_developer.view.*
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.fragment_praise_level.view.*
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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)




        layout_change_nickname.setOnClickListener {

           /* val dlg = NicknameDialog(this)
            dlg.start()*/
            val dialog = AlertDialog.Builder(this).create()
            val edialog: LayoutInflater = LayoutInflater.from(this)
            val mView: View = edialog.inflate(R.layout.namechange, null)
            val close: Button = mView.findViewById(R.id.close_btn)
            close.setOnClickListener {
                dialog.dismiss()
                dialog.cancel()
            }
            val nick_modify: Button = mView.findViewById(R.id.change_btn)
            val deletebtn: Button = mView.findViewById(R.id.delete_btn)

            val nick_modify_edit: EditText = mView.findViewById(R.id.editnickname)
            nick_modify_edit.addTextChangedListener(
                object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {}
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
                    ) {

                        val textcount: TextView = mView.findViewById(R.id.textcount)
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
                        textcount.text=input.length.toString()
                    }
                })
            nick_modify_edit.textChangedListener {
                deletebtn.isVisible=true
            }

            deletebtn.setOnClickListener {

                nick_modify_edit.setText("")
                deletebtn.isVisible=false
            }
            nick_modify.setOnClickListener {

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
                      Log.d("status코드2", response.body()?.status.toString())
                        Log.d("닉네임 중복", "닉네임 중복")
                        val existnick:TextView=mView.findViewById(R.id.existingnick)
                        existnick.isVisible=true
                        val existnickbg:ConstraintLayout=mView.findViewById(R.id.editTextTextPersonName)
                        existnickbg.setBackgroundResource(R.drawable.edittext_bg_exist)
                        val textcount:TextView=mView.findViewById(R.id.textcount)
                        textcount.isVisible=false
                        val textcount7:TextView=mView.findViewById(R.id.textcount7)
                        textcount7.isVisible=false
                        response.takeIf { it.isSuccessful }

                            ?.body()
                            ?.let {
                                Log.d("status코드", it.status.toString())

                                if(it.status == 200) {

                                    val existnickbg:ConstraintLayout=mView.findViewById(R.id.editTextTextPersonName)
                                    existnickbg.setBackgroundResource(R.drawable.edittext_bg)
                                    Log.d("닉네임변경완료", "닉네임변경완료")
                                    tv_nickname.text=nick_modify_edit.text
                                    dialog.dismiss()
                                    dialog.cancel()
                                    com.example.praisewhale.util.Toast.customToast("닉네임이 변경되었어요!", this@SettingActivity)

                                }
                                else {

                                    //MyApplication.mySharedPreferences.setValue("alarmtime")
                                }

                            }
                    }
                })

                MyApplication.mySharedPreferences.setValue(
                    "nickName",
                    nick_modify_edit.text.toString()
                )





            }

            val color = ColorDrawable(Color.TRANSPARENT)
            // Dialog 크기 설정
            val inset = InsetDrawable(color, 85)
            dialog.window?.setBackgroundDrawable(inset)
            dialog.setCancelable(false)
            dialog.setView(mView)
           // dialog.create()
            dialog.show()
            dialog.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )


        }

        layout_alarm.setOnClickListener {

            val dialog2 = AlertDialog.Builder(this).create()
            val edialog2: LayoutInflater = LayoutInflater.from(this)
            val mView2: View = edialog2.inflate(R.layout.numberpicker_time, null)

            val ny:NumberPicker=mView2.findViewById(R.id.numberPicker)
            val ny1:NumberPicker=mView2.findViewById(R.id.numberPicker2)
            val ny2:NumberPicker=mView2.findViewById(R.id.numberPicker3)

            val btnok:Button=mView2.findViewById(R.id.button_ok_time)
            val btnalarmcancel:Button=mView2.findViewById(R.id.button_time_cancel)

            btnalarmcancel.setOnClickListener {
                dialog2.dismiss()
                dialog2.cancel()
            }


            val list = resources.getStringArray(R.array.ampm)

            ny.removeDivider()
            ny1.removeDivider()
            ny2.removeDivider()


            ny.minValue=0
            ny.maxValue=list.size-1

            ny1.minValue=1
            ny1.maxValue=12

            ny2.minValue=0
            ny2.maxValue=59

            ny.displayedValues = list

            ny.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            ny2.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            ny1.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS


            btnok.setOnClickListener {
                val switch:Switch=findViewById(R.id.switch_alarm)
                if ((ny2.value.toString()).length < 2) {
                    if (list[ny.value] == "오전") {
                        tv_alarm_time.text =list[ny.value]+ny1.value.toString() + ":0" + ny2.value.toString()
                        MyApplication.mySharedPreferences.setValue(
                            "alarm_hour",
                            ny1.value.toString()
                        )
                        MyApplication.mySharedPreferences.setValue(
                            "alarm_minute",
                             ny2.value.toString()
                        )
                        MyApplication.mySharedPreferences.setValue(
                            "alarm_onoff",
                            switch.isChecked.toString()
                        )

                    } else {
                        val clock_ = ny1.value + 12
                        tv_alarm_time.text =list[ny.value]+clock_.toString() + ":0" + ny2.value.toString()
                        MyApplication.mySharedPreferences.setValue(
                            "alarm_hour",
                            ny1.value.toString()
                        )
                        MyApplication.mySharedPreferences.setValue(
                            "alarm_minute",
                            ny2.value.toString()
                        )
                        MyApplication.mySharedPreferences.setValue(
                            "alarm_onoff",
                            switch.isChecked.toString()
                        )
                    }
                } else {
                    if (list[ny.value] == "오후") {
                        val clock_ = ny1.value + 12
                        tv_alarm_time.text =list[ny.value]+clock_.toString() + ":" + ny2.value.toString()
                        MyApplication.mySharedPreferences.setValue(
                            "alarm_hour",
                            ny1.value.toString()
                        )
                        MyApplication.mySharedPreferences.setValue(
                            "alarm_minute",
                            ny2.value.toString()
                        )
                        MyApplication.mySharedPreferences.setValue(
                            "alarm_onoff",
                            switch.isChecked.toString()
                        )
                    } else {
                        tv_alarm_time.text =list[ny.value]+ny1.value.toString() + ":" + ny2.value.toString()
                        MyApplication.mySharedPreferences.setValue(
                            "alarm_hour",
                            ny1.value.toString()
                        )
                        MyApplication.mySharedPreferences.setValue(
                            "alarm_minute",
                            ny2.value.toString()
                        )
                        MyApplication.mySharedPreferences.setValue(
                            "alarm_onoff",
                            switch.isChecked.toString()
                        )
                    }
                }
                dialog2.dismiss()
                com.example.praisewhale.util.Toast.customToast(
                    "앞으로 " + tv_alarm_time.text + "에 칭찬 알림을 보내드릴게요!",
                    this
                )
            }

            val color = ColorDrawable(Color.TRANSPARENT)
            // Dialog 크기 설정
            val inset = InsetDrawable(color, 85)
            dialog2.window?.setBackgroundDrawable(inset)

            dialog2.setView(mView2)
            dialog2.show()



        }

        layout_service.setOnClickListener {
            val intent= Intent(this, InfoUserActivity::class.java)
            startActivity(intent)
        }

        layout_personal_information.setOnClickListener {
            val intent= Intent(this, InfoActivity::class.java)
            startActivity(intent)
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
        }

    override fun onResume() {
        super.onResume()
        tv_nickname.text=MyApplication.mySharedPreferences.getValue("nickName", "")
    }


private fun NumberPicker.removeDivider() {
    val pickerFields = NumberPicker::class.java.declaredFields
    for (pf in pickerFields) {
        if (pf.name == "mSelectionDivider") {
            pf.isAccessible = true
            try {
                val colorDrawable = ColorDrawable(Color.TRANSPARENT)
                pf[this] = colorDrawable
            } catch (e: java.lang.IllegalArgumentException) {

            } catch (e: Resources.NotFoundException) {

            } catch (e: IllegalAccessException) {

            }
            break
        }
    }
}


    }
