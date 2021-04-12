package com.sopt27.praisewhale.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.sopt27.praisewhale.*
import com.sopt27.praisewhale.databinding.FragmentPraiseLevelBinding
import com.sopt27.praisewhale.util.MyApplication
import kotlinx.android.synthetic.main.fragment_praise_level.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PraiseLevelFragment : Fragment() {

    private lateinit var onBackPressedCallback: OnBackPressedCallback


    var popupcheck1=false
    var popupcheck2=false
    var popupcheck3=false
    var popupcheck4=false
    var popupcheck5=false

    private val praiseLevelViewModel :PraiseLevelViewModel by viewModels()
    lateinit var  binding: FragmentPraiseLevelBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_praise_level,container,false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.level=praiseLevelViewModel
        binding.lifecycleOwner=viewLifecycleOwner
        setListener()
        observeLiveData()

        popupcheck1=MyApplication.mySharedPreferences.getBooleanValue("popupcheck1",false)
        popupcheck2=MyApplication.mySharedPreferences.getBooleanValue("popupcheck2",false)
        popupcheck3=MyApplication.mySharedPreferences.getBooleanValue("popupcheck3",false)
        popupcheck4=MyApplication.mySharedPreferences.getBooleanValue("popupcheck4",false)
        popupcheck5=MyApplication.mySharedPreferences.getBooleanValue("popupcheck5",false)

        setOnBackPressedCallBack()
    }
    private fun requestLevel(){

        praiseLevelViewModel.requestPraiseLevelData()
    }

    private fun setListener(){
        binding.settingBtn.setOnClickListener {
            val intent= Intent(context,LevelInfoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeLiveData() {
        praiseLevelViewModel.userLevelCount.observe(viewLifecycleOwner){
            Log.d("nananana",it.toString())

            when (it) {
                0 -> {
                    Log.d("nananana",binding.detailTxt.text.toString())
                    binding.detailTxt.text="아직은 칭찬이 어색한 고래"
                    level_whale.isVisible=true
                    imageView_lv1.isVisible=false
                    imageView_lv2.isVisible=false
                    imageView_lv3.isVisible=false
                    imageView_lv4.isVisible=false
                    imageView_lv5.isVisible=false
                    level_num.setImageResource(R.drawable.lv_ic_lv_0)
                }
                1 -> {
                    if(!popupcheck1){
                        val dialogDone =
                            PraiseLeveldialogFragment.CustomDialogBuilder(1).create()
                        dialogDone.isCancelable = false
                        dialogDone.show(parentFragmentManager, dialogDone.tag)
                    }
                    popupcheck1=true
                    MyApplication.mySharedPreferences.setBooleanValue("popupcheck1",popupcheck1)
                    level_whale.isVisible=false
                    imageView_lv1.isVisible=true
                    imageView_lv2.isVisible=false
                    imageView_lv3.isVisible=false
                    imageView_lv4.isVisible=false
                    imageView_lv5.isVisible=false
                    detail_txt.text = "칭찬에 흥미가 생긴 고래"
                    level_num.setImageResource(R.drawable.lv_ic_lv_1)
                }
                2 -> {

                    if(!popupcheck2){
                        val dialogDone =
                            PraiseLeveldialogFragment.CustomDialogBuilder(2).create()
                        dialogDone.isCancelable = false
                        dialogDone.show(parentFragmentManager, dialogDone.tag)
                       }
                    popupcheck2=true
                    MyApplication.mySharedPreferences.setBooleanValue("popupcheck2",popupcheck2)
                    level_whale.isVisible=false
                    imageView_lv1.isVisible=false
                    imageView_lv2.isVisible=true
                    imageView_lv3.isVisible=false
                    imageView_lv4.isVisible=false
                    imageView_lv5.isVisible=false
                    detail_txt.text = "칭찬에 익숙해진 고래"
                    level_num.setImageResource(R.drawable.lv_ic_lv_2)
                }
                3 -> {

                    if(!popupcheck3){
                        val dialogDone =
                            PraiseLeveldialogFragment.CustomDialogBuilder(3).create()
                        dialogDone.isCancelable = false
                        dialogDone.show(parentFragmentManager, dialogDone.tag)
                    }
                    popupcheck3=true
                    MyApplication.mySharedPreferences.setBooleanValue("popupcheck3",popupcheck3)
                    level_whale.isVisible=false
                    imageView_lv1.isVisible=false
                    imageView_lv2.isVisible=false
                    imageView_lv3.isVisible=true
                    imageView_lv4.isVisible=false
                    imageView_lv5.isVisible=false
                    detail_txt.text = "슬슬 리듬타기 시작한 고래"
                    level_num.setImageResource(R.drawable.lv_ic_lv_3)
                }
                4 -> {

                    if(!popupcheck4){
                        val dialogDone =
                            PraiseLeveldialogFragment.CustomDialogBuilder(4).create()
                        dialogDone.isCancelable = false
                        dialogDone.show(parentFragmentManager, dialogDone.tag)
                    }
                    popupcheck4=true
                    MyApplication.mySharedPreferences.setBooleanValue("popupcheck4",popupcheck4)
                    level_whale.isVisible=false
                    imageView_lv1.isVisible=false
                    imageView_lv2.isVisible=false
                    imageView_lv3.isVisible=false
                    imageView_lv4.isVisible=true
                    imageView_lv5.isVisible=false
                    detail_txt.text = "신나게 춤추는 고래"
                    level_num.setImageResource(R.drawable.lv_ic_lv_4)

                }
                5 -> {

                    if(!popupcheck5){
                        val dialogDone =
                            PraiseLeveldialogFragment.CustomDialogBuilder(5).create()
                        dialogDone.isCancelable = false
                        dialogDone.show(parentFragmentManager, dialogDone.tag)
                    }
                    popupcheck5=true
                    MyApplication.mySharedPreferences.setBooleanValue("popupcheck5",popupcheck5)

                    level_whale.isVisible=false
                    imageView_lv1.isVisible=false
                    imageView_lv2.isVisible=false
                    imageView_lv3.isVisible=false
                    imageView_lv4.isVisible=false
                    imageView_lv5.isVisible=true
                    detail_txt.text = "춤신 춤왕 만렙 고래"
                    level_num.setImageResource(R.drawable.lv_ic_lv_5)
                    textViewPhrase.isVisible=false
                    textViewPhraseGod.isVisible=false
                    whaleGod5.isVisible=true
                    textViewPhraseGod5.text = MyApplication.mySharedPreferences.getValue("nickName","")+"님은 이제"
                    textViewPhrase5.text=" 칭찬의 신!"
                    textViewPhraseGod5.isVisible=true
                    textViewPhrase5.isVisible=true

                }

            }
            binding.whaleGod.isVisible=true
            binding.whale.isVisible=true
            binding.detailTxt.isVisible=true
            binding.levelNum.isVisible=true
        }
        praiseLevelViewModel.praiseCount.observe(viewLifecycleOwner){
           when (it.toInt()){
               in 0..4->binding.cpbCirclebar.progress =it.toInt() * 20
               in 5..9->cpb_circlebar.progress = it.toInt() * 20
               in 10..29->cpb_circlebar.progress = it.toInt() * 5
               in 30..99->cpb_circlebar.progress = it.toInt() *2
               100->cpb_circlebar.progress=100

           }
        }



    }

    override fun onResume() {
        super.onResume()
        requestLevel()
        if (!onBackPressedCallback.isEnabled) {
            onBackPressedCallback.isEnabled = true
        }


    }

    override fun onPause() {
        super.onPause()
        onBackPressedCallback.isEnabled = false
    }

    private fun setOnBackPressedCallBack() {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as MainActivity).showFinishToast()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }
}

