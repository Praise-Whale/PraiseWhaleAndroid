package com.example.praisewhale

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.praisewhale.data.RequestNickChange
import com.example.praisewhale.data.ResponseData
import com.example.praisewhale.data.home.ResponseNickChange
import com.example.praisewhale.databinding.ActivitySettingBinding
import com.example.praisewhale.home.data.ResponseHomePraise
import com.example.praisewhale.signup.SignUpActivity
import com.example.praisewhale.signup.WhaleNameFragment
import com.example.praisewhale.util.MyApplication
import com.example.praisewhale.util.textChangedListener
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.fragment_praise_level.*
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


        val nickname=MyApplication.mySharedPreferences.getValue("nickName","")


        setting_nickname.text=nickname


        nickchange.setOnClickListener {

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

            nick_modify_edit.textChangedListener {
                deletebtn.isVisible=true
            }

            deletebtn.setOnClickListener {

                nick_modify_edit.setText("")
            }
            nick_modify.setOnClickListener {
                /*val call: Call<ResponseData> = CollectionImpl.service.nicknameCheck(signUpViewModel.userName.value!!)
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
                                (activity as SignUpActivity).replaceFragment(WhaleNameFragment())
                            } ?: signUpViewModel.nameFail()
                    }
                })*/
                val token = MyApplication.mySharedPreferences.getValue("token","")

                val body=RequestNickChange(nickName =nickname ,newNickName = nick_modify_edit.text.toString())
                val call : Call<ResponseNickChange> = CollectionImpl.service.nickchange(token,body)
                call.enqueue(object : Callback<ResponseNickChange> {
                    override fun onFailure(call: Call<ResponseNickChange>, t: Throwable) {
                        Log.d("tag", t.localizedMessage)
                    }

                    override fun onResponse(
                        call: Call<ResponseNickChange>,
                        response: Response<ResponseNickChange>
                    ) {
                        when (response.body()?.status) {

                            400 ->Toast.makeText(applicationContext,"닉네임이 중복합니다",Toast.LENGTH_SHORT).show()


                        }
                        response.takeIf { it.isSuccessful }

                            ?.body()
                            ?.let {

                                    it ->
                                Log.d("닉네임변경완료","닉네임변경완료")
                                //MyApplication.mySharedPreferences.setValue("alarmtime")





                            }
                    }
                })

                MyApplication.mySharedPreferences.setValue("nickName",nick_modify_edit.text.toString())

                    setting_nickname.text=nick_modify_edit.text
                    dialog.dismiss()
                    dialog.cancel()


            }

            val color = ColorDrawable(Color.TRANSPARENT)
            // Dialog 크기 설정
            val inset = InsetDrawable(color, 85)
            dialog.window?.setBackgroundDrawable(inset)
            dialog.setCancelable(false)
            dialog.setView(mView)
           // dialog.create()
            dialog.show()
            dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)



            /*val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.custom_dialog, null)
            val dialogText = dialogView.findViewById<EditText>(R.id.editTextTextPersonName)

            builder.setView(dialogView)
                .setPositiveButton("확인") { dialogInterface, i ->
                    mainTv.text = dialogText.text.toString()

                    *//* 확인일 때 main의 View의 값에 dialog View에 있는 값을 적용 *//*

                }
                .setNegativeButton("취소") { dialogInterface, i ->
                    *//* 취소일 때 아무 액션이 없으므로 빈칸 *//*
                }
                .show()*/
        }

        alarm.setOnClickListener {

         /* val tdlg=TimepickerDialog2(this)
            tdlg.start()*/



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

                if ((ny2.value.toString()).length < 2) {
                    if (list[ny.value] == "오전") {
                        clock.text =list[ny.value]+ny1.value.toString() + ":0" + ny2.value.toString()
                        MyApplication.mySharedPreferences.setValue("alarmtime",list[ny.value]+ny1.value.toString() + ":0" + ny2.value.toString())

                    } else {
                        val clock_ = ny1.value + 12
                        clock.text =list[ny.value]+clock_.toString() + ":0" + ny2.value.toString()

                    }
                } else {
                    if (list[ny.value] == "오후") {
                        val clock_ = ny1.value + 12
                        clock.text =list[ny.value]+clock_.toString() + ":" + ny2.value.toString()

                    } else {
                        clock.text =list[ny.value]+ny1.value.toString() + ":" + ny2.value.toString()

                    }
                }
                dialog2.dismiss()
            }

            val color = ColorDrawable(Color.TRANSPARENT)
            // Dialog 크기 설정
            val inset = InsetDrawable(color, 85)
            dialog2.window?.setBackgroundDrawable(inset)

            dialog2.setView(mView2)
            dialog2.show()


        }


        developer.setOnClickListener {
            val intent= Intent(this, DeveloperActivity::class.java)
            startActivity(intent)
        }
        setting_close_btn.setOnClickListener {
            onBackPressed()
            finish()
        }

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
