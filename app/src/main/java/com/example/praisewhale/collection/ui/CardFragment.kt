package com.example.praisewhale.collection.ui

import android.app.AlertDialog
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.NumberPicker
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.praisewhale.*
import com.example.praisewhale.collection.adapter.CardBoxAdapter
import com.example.praisewhale.collection.data.ResponseCardData
import com.example.praisewhale.databinding.FragmentCardBinding
import com.example.praisewhale.util.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class CardFragment : Fragment() {
    private var _binding: FragmentCardBinding? = null
    private val binding get() = _binding!!
    private lateinit var cardBoxAdapter: CardBoxAdapter

    private val cal: Calendar = Calendar.getInstance()
    private val thisYear = cal.get(Calendar.YEAR)
    private val thisMonth = cal.get(Calendar.MONTH) + 1
    private var firstYear = 0
    private var firstMonth = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setCardBox()
        setCardPicker()
    }

    private val visibleView
        get() = listOf(binding.cardCount, binding.cardCount2, binding.rvCardBox)

    private val emptyView
        get() = listOf(binding.emptyImg, binding.tvEmpty1, binding.tvEmpty2)

    private fun setCardBox() {
        cardBoxAdapter =
            CardBoxAdapter(
                requireContext()
            )
        binding.rvCardBox.adapter = cardBoxAdapter
        binding.rvCardBox.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        LinearSnapHelper().attachToRecyclerView(binding.rvCardBox)

        binding.btnCardPicker.text = thisYear.toString() + "년 전체"
        getServerCardData(thisYear, "0")
    }

    private fun setCardPicker() {
        binding.btnCardPicker.setOnClickListener {
            val dialog = AlertDialog.Builder(context).create()
            val edialog: LayoutInflater = LayoutInflater.from(context)
            val mView: View = edialog.inflate(R.layout.year_month_picker, null)

            val year: NumberPicker = mView.findViewById(R.id.card_picker_year)
            val month: NumberPicker = mView.findViewById(R.id.card_picker_month)
            val cancel: ImageButton = mView.findViewById(R.id.btn_card_picker_cancel)
            val confirm: Button = mView.findViewById(R.id.btn_card_picker_confirm)

            // editText 설정 해제
            year.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            month.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            year.wrapSelectorWheel = false

            // 값 설정
            val years = Array(thisYear - firstYear + 1) { i -> (firstYear + i).toString() + "년" }
            val months = arrayOfNulls<String>(13)
            months[0] = "전체"
            for (i in 1..12) {
                months[i] = i.toString() + "월"
            }
            val monthsOfOnlyYear = arrayOfNulls<String>(thisMonth - firstMonth + 2)
            monthsOfOnlyYear[0] = "전체"
            for (i in 1 until monthsOfOnlyYear.size) {
                monthsOfOnlyYear[i] = (firstMonth + i - 1).toString() + "월"
            }
            val monthsOfOThisYear = arrayOfNulls<String>(thisMonth + 1)
            monthsOfOThisYear[0] = "전체"
            for (i in 1 until monthsOfOThisYear.size) {
                monthsOfOThisYear[i] = i.toString() + "월"
            }
            val monthsOfOFirstYear = arrayOfNulls<String>(14 - firstMonth)
            monthsOfOFirstYear[0] = "전체"
            for (i in 1 until monthsOfOFirstYear.size) {
                monthsOfOFirstYear[i] = (firstMonth + i - 1).toString() + "월"
            }

            year.displayedValues = years
            year.minValue = 0
            year.maxValue = years.size - 1

            month.minValue = 0
            // 각 year 마다 다른 month 적용
            Log.d("첫년도", "$firstYear")
            Log.d("현년도", "$thisYear")
            Log.d("년 개수", "${years.size}")
            when (years.size) {
                1 -> { // firstYear == thisYear
                    month.displayedValues = monthsOfOnlyYear
                    month.maxValue = monthsOfOnlyYear.size - 1

                    // confirm
                    confirm.setOnClickListener {
                        clickConfirm(year, month, years, monthsOfOnlyYear)

                        dialog.dismiss()
                        dialog.cancel()
                    }
                }
                2 -> { // firstYear & thisYear
                    if (years[year.value] == "${thisYear}년") {
                        month.displayedValues = monthsOfOThisYear
                        month.maxValue = monthsOfOThisYear.size - 1
                    } else {
                        month.displayedValues = monthsOfOFirstYear
                        month.maxValue = monthsOfOFirstYear.size - 1
                    }

                    // 스크롤 시 다른 month 적용
                    year.setOnValueChangedListener { yearPicker, oldVal, newVal ->
                        if (monthsOfOThisYear.size > monthsOfOFirstYear.size) {
                            if (oldVal == yearPicker.maxValue && newVal == 0) { // this -> first
                                month.maxValue = monthsOfOFirstYear.size - 1
                                month.displayedValues = monthsOfOFirstYear
                            }
                            if (oldVal == 0 && newVal == yearPicker.maxValue) { // first -> this
                                month.displayedValues = monthsOfOThisYear
                                month.maxValue = monthsOfOThisYear.size - 1
                            }
                        } else {
                            if (oldVal == yearPicker.maxValue && newVal == 0) { // this -> first
                                month.displayedValues = monthsOfOFirstYear
                                month.maxValue = monthsOfOFirstYear.size - 1
                            }
                            if (oldVal == 0 && newVal == yearPicker.maxValue) { // first -> this
                                month.maxValue = monthsOfOThisYear.size - 1
                                month.displayedValues = monthsOfOThisYear
                            }
                        }
                    }

                    // confirm
                    confirm.setOnClickListener {
                        if (years[year.value] == "${thisYear}년") clickConfirm(year, month, years, monthsOfOThisYear)
                        else clickConfirm(year, month, years, monthsOfOFirstYear)

                        dialog.dismiss()
                        dialog.cancel()
                    }
                }
                else -> { // firstYear & thisYear & else
                    when {
                        years[year.value] == "${thisYear}년" -> {
                            month.displayedValues = monthsOfOThisYear
                            month.maxValue = monthsOfOThisYear.size - 1
                        }
                        years[year.value] == "${firstYear}년" -> {
                            month.displayedValues = monthsOfOFirstYear
                            month.maxValue = monthsOfOFirstYear.size - 1
                        }
                        else -> {
                            month.displayedValues = months
                            month.maxValue = months.size - 1
                        }
                    }

                    // 스크롤 시 다른 month 적용
                    year.setOnValueChangedListener { yearPicker, oldVal, newVal ->
                        if (oldVal == yearPicker.maxValue) { // this -> else
                            month.displayedValues = months
                            month.maxValue = months.size - 1
                        }
                        if (newVal == yearPicker.maxValue) { // else -> this
                            month.maxValue = monthsOfOThisYear.size - 1
                            month.displayedValues = monthsOfOThisYear
                        }
                        if (newVal == 0) { // else -> first
                            month.maxValue = monthsOfOFirstYear.size - 1
                            month.displayedValues = monthsOfOFirstYear
                        }
                        if (oldVal == 0) { // first -> else
                            month.displayedValues = months
                            month.maxValue = months.size - 1
                        }
                    }

                    // confirm
                    confirm.setOnClickListener {
                        when {
                            years[year.value] == "${thisYear}년" -> clickConfirm(year, month, years, monthsOfOThisYear)
                            years[year.value] == "${firstYear}년" -> clickConfirm(year, month, years, monthsOfOFirstYear)
                            else -> {
                                getServerCardData(years[year.value].substring(0, 4).toInt(), String.format("%02d", month.value))
                                if (month.value == 0) binding.btnCardPicker.text = years[year.value] + " 전체"
                                else binding.btnCardPicker.text = years[year.value] + " " + months[month.value]
                            }
                        }

                        dialog.dismiss()
                        dialog.cancel()
                    }
                }
            }

            // picker에 써있는 값이 선택되어 있게 설정
            for (i in years.indices) {
                if (binding.btnCardPicker.text.split(" ")[0] == years[i])
                    year.value = i
            }

            for (i in months.indices) {
                if (binding.btnCardPicker.text.split(" ")[1] == months[i])
                    month.value = i
            }

            cancel.setOnClickListener {
                dialog.dismiss()
                dialog.cancel()
            }

            // Dialog background 설정
            val color = ColorDrawable(Color.TRANSPARENT)
            val inset = InsetDrawable(color, 85)
            dialog.window?.setBackgroundDrawable(inset)

            dialog.setView(mView)
            dialog.setCancelable(false)
            dialog.show()
        }
    }

    private fun clickConfirm(year: NumberPicker, month: NumberPicker, years: Array<String>, months: Array<String?>) {
        if (month.value == 0) {
            getServerCardData(years[year.value].substring(0, 4).toInt(), String.format("%02d", month.value))
            binding.btnCardPicker.text = years[year.value] + " 전체"
        } else {
            val monthValue = months[month.value]!!.split("월")[0].toInt()
            getServerCardData(years[year.value].substring(0, 4).toInt(), String.format("%02d", monthValue))
            binding.btnCardPicker.text = years[year.value] + " " + months[month.value]
        }
    }

    private fun getServerCardData(year: Int, month: String) {
        CollectionImpl.service.getPraiseCard(
            year = year, month = month,
            token = MyApplication.mySharedPreferences.getValue("token", "")
        ).enqueue(object : Callback<ResponseCardData> {
            override fun onFailure(call: Call<ResponseCardData>, t: Throwable) {
                Log.d("tag", "t.localizedMessage!!")
            }

            override fun onResponse(
                call: Call<ResponseCardData>,
                response: Response<ResponseCardData>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        firstYear = it.data.firstDate.created_at.substring(0,4).toInt()
                        firstMonth = it.data.firstDate.created_at.substring(5,7).toInt()

                        if (it.data.praiseCount == 0) configureEmptyView()
                        else {
                            configureDefaultView()
                            binding.cardCount.text = it.data.praiseCount.toString() + "번"
                            cardBoxAdapter.data = it.data.collectionPraise
                            cardBoxAdapter.notifyDataSetChanged()
                        }
                    }
                } else configureAllEmptyView()
            }
        })
    }

    private fun configureAllEmptyView() {
        visibleView.forEach { view -> view.isVisible = false }
        emptyView.forEach { view -> view.isVisible = true }
        binding.btnCardPicker.isVisible = false
        binding.tvEmpty1.text = getString(R.string.all_empty_title)
        binding.tvEmpty2.text = getString(R.string.all_empty_sub)
    }

    private fun configureEmptyView() {
        visibleView.forEach { view -> view.isVisible = false }
        emptyView.forEach { view -> view.isVisible = true }
        binding.btnCardPicker.isVisible = true
        binding.tvEmpty1.text = getString(R.string.default_empty_title)
        binding.tvEmpty2.text = getString(R.string.default_empty_sub)
    }

    private fun configureDefaultView() {
        visibleView.forEach { view -> view.isVisible = true }
        emptyView.forEach { view -> view.isVisible = false }
        binding.btnCardPicker.isVisible = true
    }
}