package com.example.praisewhale.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.NumberPicker
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.praisewhale.*
import kotlinx.android.synthetic.main.fragment_card.*
import java.time.LocalDate


class CardFragment : Fragment() {

    private lateinit var cardBoxAdapter: CardBoxAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_card, container, false)

        return view
    }

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardBoxAdapter = CardBoxAdapter(view.context)

        rv_card_box.adapter = cardBoxAdapter
        rv_card_box.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)

        // 서버 통신
//        val call : Call<ResponseCardData> = CollectionImpl.service.getCollection()
//        call.enqueue(object : Callback<ResponseCardData> {
//            override fun onFailure(call: Call<ResponseCardData>, t: Throwable) {
//                Log.d("tag", t.localizedMessage)
//            }
//
//            override fun onResponse(
//                call: Call<ResponseCardData>,
//                response: Response<ResponseCardData>
//            ) {
//                response.takeIf { it.isSuccessful }
//                    ?.body()
//                    ?.let { it ->
//                        Log.d("tag", it.toString())
//                        tv_total.text = it.data.praiseCount[0].likeCount.toString()
//                        // collectionAdapter.data = it.data.praiseResult as MutableList<PraiseResult>
//                        cardBoxAdapter.notifyDataSetChanged()
//                    }
//
//            }
//        })

        // 스냅 적용
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(rv_card_box)

        cardBoxAdapter.data = mutableListOf(
            CardBoxData("희빈", "12월 13일", "너가 내 친구라서 참 좋아"),
            CardBoxData("윤소", "12월 13일", "엄마 아빠 사랑해요"),
            CardBoxData("생각이안나영", "12월 13일", "넌 참 사랑스러워"),
            CardBoxData("남궁웅앵웅앵", "12월 13일", "너가 내 친구라서 참 좋아")
        )
        cardBoxAdapter.notifyDataSetChanged()

        // 현재 년도 가져오기
        val currentDate: LocalDate = LocalDate.now()
        val thisDate = currentDate.toString().substring(0, currentDate.toString().length-1)
        val thisYear = thisDate.split("-")[0]
        btn_card_month.text = thisYear + "년 전체"

        // 년, 월 Dialog 적용
        btn_card_month.setOnClickListener {
            val dialog = AlertDialog.Builder(context).create()
            val edialog: LayoutInflater = LayoutInflater.from(context)
            val mView: View = edialog.inflate(R.layout.year_month_picker, null)

            val year: NumberPicker = mView.findViewById(R.id.card_picker_year)
            val month: NumberPicker = mView.findViewById(R.id.card_picker_month)
            val cancel: ImageButton = mView.findViewById(R.id.btn_card_picker_cancel)
            val confirm: Button = mView.findViewById(R.id.btn_card_picker_confirm)

            // Display 사이즈 구하기
            val metrics: DisplayMetrics = resources.displayMetrics
            val deviceWidth: Int = metrics.widthPixels
            val deviceHeight: Int = metrics.heightPixels

            // editText 설정 해제
            year.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            month.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

            // 보여질 값 설정
            val years = Array(thisYear.toInt() - 2020 + 3) { i ->
                (thisYear.toInt() + i).toString() + "년"
            }
            val months = arrayOfNulls<String>(13)
            months[0] = "전체"
            for (i in 1 .. 12){
                months[i] = (month.minValue + i).toString() + "월"
            }

            year.displayedValues = years
            month.displayedValues = months

            year.minValue = 0
            year.maxValue = years.size - 1
            month.minValue = 0
            month.maxValue = 12

            for (i in years.indices) {
                if (btn_card_month.text.split(" ")[0] == years[i])
                    year.value = i
            }

            for (i in months.indices) {
                if (btn_card_month.text.split(" ")[1] == months[i])
                    month.value = i
            }

            // 라인 제거
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q){
                year.removeDivider()
                month.removeDivider()
            }

            // [X] 버튼 클릭했을 때
            cancel.setOnClickListener {
                dialog.dismiss()
                dialog.cancel()
            }

            // 설정 버튼 클릭했을 때
            confirm.setOnClickListener {
                if (month.value == 0) {
                    btn_card_month.text = years[year.value] + " 전체"
                } else {
                    btn_card_month.text = years[year.value] + " " + (month.value).toString() + "월"
                }
                dialog.dismiss()
                dialog.cancel()
            }

            // Dialog Background 설정
            val color = ColorDrawable(Color.TRANSPARENT)
            // Dialog 크기 설정
            val inset = InsetDrawable(color, 85)
            dialog.window?.setBackgroundDrawable(inset)

            dialog.setView(mView)
            dialog.create()
            dialog.show()
        }
    }

    // NumberPicker 라인 제거 함수
    private fun NumberPicker.removeDivider() {
        val pickerFields = NumberPicker::class.java.declaredFields
        for (pf in pickerFields){
            if (pf.name == "mSelectionDivider"){
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