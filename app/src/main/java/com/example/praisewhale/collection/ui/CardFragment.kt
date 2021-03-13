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
    private var firstYear = 0

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
                months[i] = (month.minValue + i).toString() + "월"
            }

            year.displayedValues = years
            month.displayedValues = months

            year.minValue = 0
            year.maxValue = years.size - 1
            month.minValue = 0
            month.maxValue = 12

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

            confirm.setOnClickListener {
                getServerCardData(years[year.value].substring(0, 4).toInt(), String.format("%02d", month.value))

                if (month.value == 0) {
                    binding.btnCardPicker.text = years[year.value] + " 전체"
                } else {
                    binding.btnCardPicker.text = years[year.value] + " " + months[month.value]
                }

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