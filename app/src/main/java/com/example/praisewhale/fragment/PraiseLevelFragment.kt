package com.example.praisewhale.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import com.example.praisewhale.*
import com.example.praisewhale.databinding.FragmentPraiseLevelBinding
import com.example.praisewhale.util.MyApplication
import com.sopt.cherish.ui.dialog.PraseLevelDialogFragment
import kotlinx.android.synthetic.main.fragment_praise_level.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PraiseLevelFragment : Fragment() {

    lateinit var binding: FragmentPraiseLevelBinding
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    var praisecount=0
    var popupcheck1=false
    var popupcheck2=false
    var popupcheck3=false
    var popupcheck4=false
    var popupcheck5=false
    var userlevel=""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding= FragmentPraiseLevelBinding.inflate(layoutInflater)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.settingBtn.setOnClickListener {

            val intent= Intent(context,LevelInfoActivity::class.java)
            startActivity(intent)
        }

        popupcheck1=MyApplication.mySharedPreferences.getBooleanValue("popupcheck1",false)
        popupcheck2=MyApplication.mySharedPreferences.getBooleanValue("popupcheck2",false)
        popupcheck3=MyApplication.mySharedPreferences.getBooleanValue("popupcheck3",false)
        popupcheck4=MyApplication.mySharedPreferences.getBooleanValue("popupcheck4",false)
        popupcheck5=MyApplication.mySharedPreferences.getBooleanValue("popupcheck5",false)

        level_txt.text=MyApplication.mySharedPreferences.getValue("nickName","")+ "님의"
        whalename_txt.text=MyApplication.mySharedPreferences.getValue("whaleName","")
        val token = MyApplication.mySharedPreferences.getValue("token", "")
        val call: Call<ResponselevelData> = CollectionImpl.service.getlevelcount(token)
        call.enqueue(object : Callback<ResponselevelData> {
            override fun onFailure(call: Call<ResponselevelData>, t: Throwable) {
                Log.d("tag", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<ResponselevelData>,
                response: Response<ResponselevelData>
            ) {

                response.takeIf { it.isSuccessful }

                    ?.body()
                    ?.let {

                            it ->

                        /*  level_txt.text = it.data.nickName.toString() + "님의"
                          whalename_txt.text = it.data.whaleName.toString()*/
                       // userlevel=it.data.userLevel
                        praisecount=it.data.praiseCount
                        binding.textView2.text = it.data.praiseCount.toString() + "번"
                        binding.textViewPhraseGod.text = it.data.levelUpNeedCount.toString() +"번"

                        setview(it.data.userLevel.toString(),it.data.praiseCount)



                    }
            }
        })

        setOnBackPressedCallBack()
    }
    fun setview(level:String,praisecount:Int){
        when (level) {
            "0" -> {
                detail_txt.text = "아직은 칭찬이 어색한 고래"
                level_whale.isVisible=true
                imageView_lv1.isVisible=false
                imageView_lv2.isVisible=false
                imageView_lv3.isVisible=false
                imageView_lv4.isVisible=false
                imageView_lv5.isVisible=false
                level_num.setImageResource(R.drawable.lv_ic_lv_0)
                cpb_circlebar.progress = praisecount.toInt() * 20
            }
            "1" -> {
                if(!popupcheck1){
                    PraseLevelDialogFragment(R.layout.level_popup,1).show(
                        parentFragmentManager,
                        "MainActivity"
                    )}
                popupcheck1=true
                MyApplication.mySharedPreferences.setBooleanValue("popupcheck1",popupcheck1)

                textView2.text = praisecount.toString() + "번"
                level_whale.isVisible=false
                imageView_lv1.isVisible=true
                imageView_lv2.isVisible=false
                imageView_lv3.isVisible=false
                imageView_lv4.isVisible=false
                imageView_lv5.isVisible=false
                detail_txt.text = "칭찬에 흥미가 생긴 고래"
                level_num.setImageResource(R.drawable.lv_ic_lv_1)
                cpb_circlebar.progress = (praisecount.toInt()-5) * 20
            }
            "2" -> {

                if(!popupcheck2){
                    PraseLevelDialogFragment(R.layout.level_popup,2).show(
                        parentFragmentManager,
                        "MainActivity"
                    )}
                popupcheck2=true
                MyApplication.mySharedPreferences.setBooleanValue("popupcheck2",popupcheck2)

                textView2.text = praisecount.toString() + "번"
                level_whale.isVisible=false
                imageView_lv1.isVisible=false
                imageView_lv2.isVisible=true
                imageView_lv3.isVisible=false
                imageView_lv4.isVisible=false
                imageView_lv5.isVisible=false
                detail_txt.text = "칭찬에 익숙해진 고래"
                level_num.setImageResource(R.drawable.lv_ic_lv_2)

                cpb_circlebar.progress = (praisecount.toInt()-10) * 5
            }
            "3" -> {

                if(!popupcheck3){PraseLevelDialogFragment(R.layout.level_popup,3).show(
                    parentFragmentManager,
                    "MainActivity"
                )}
                popupcheck3=true
                MyApplication.mySharedPreferences.setBooleanValue("popupcheck3",popupcheck3)

                level_whale.isVisible=false
                imageView_lv1.isVisible=false
                imageView_lv2.isVisible=false
                imageView_lv3.isVisible=true
                imageView_lv4.isVisible=false
                imageView_lv5.isVisible=false
                //    level_whale.setImageResource(R.drawable.lv_3_img_whale)
                detail_txt.text = "슬슬 리듬타기 시작한 고래"
                level_num.setImageResource(R.drawable.lv_ic_lv_3)
                cpb_circlebar.progress = (praisecount.toInt()-30) * 5
            }
            "4" -> {

                if(!popupcheck4){PraseLevelDialogFragment(R.layout.level_popup,4).show(
                    parentFragmentManager,
                    "MainActivity"
                )}
                popupcheck4=true
                MyApplication.mySharedPreferences.setBooleanValue("popupcheck4",popupcheck4)
                level_whale.isVisible=false
                imageView_lv1.isVisible=false
                imageView_lv2.isVisible=false
                imageView_lv3.isVisible=false
                imageView_lv4.isVisible=true
                imageView_lv5.isVisible=false

                //     level_whale.setImageResource(R.drawable.lv_4_img_whale)
                detail_txt.text = "신나게 춤추는 고래"
                level_num.setImageResource(R.drawable.lv_ic_lv_4)

                cpb_circlebar.progress = (praisecount.toInt()-50) *2
            }
            "5" -> {

                if(!popupcheck5){PraseLevelDialogFragment(R.layout.level_popup,5).show(
                    parentFragmentManager,
                    "MainActivity"
                )}
                popupcheck5=true
                MyApplication.mySharedPreferences.setBooleanValue("popupcheck5",popupcheck5)

                textView2.text = praisecount.toString() + "번"

                //level_whale.setImageResource(R.drawable.lv_5_img_whale)
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

                //뒤에
                cpb_circlebar.progress=100
            }

        }
        binding.whaleGod.isVisible=true
        binding.whale.isVisible=true
        binding.detailTxt.isVisible=true
        binding.levelNum.isVisible=true
    }

    override fun onResume() {
        super.onResume()
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

