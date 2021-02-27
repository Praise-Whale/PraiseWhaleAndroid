package com.example.praisewhale.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.praisewhale.*
import com.example.praisewhale.databinding.FragmentPraiseLevelBinding
import com.example.praisewhale.util.MyApplication
import kotlinx.android.synthetic.main.fragment_praise_level.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PraiseLevelFragment : Fragment() {

    lateinit var binding: FragmentPraiseLevelBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding= FragmentPraiseLevelBinding.inflate(layoutInflater)
        binding.settingBtn.setOnClickListener {


            val intent= Intent(context,LevelInfoActivity::class.java)
            startActivity(intent)
        }


        return binding.root
    }


    override fun onResume() {
        super.onResume()
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

                        level_txt.text = it.data.nickName.toString() + "님의"
                        whalename_txt.text = it.data.whaleName.toString()

                        textView2.text = it.data.praiseCount.toString() + "번"
                       // cpb_circlebar.progress = it.data.praiseCount * 10
                        cpb_circlebar.progress =3 * 10


                        Log.d("세번째", "세번째")



                        when (it.data.userLevel) {
                            0 -> {
                                level_whale.setImageResource(R.drawable.lv_0_img_whale)
                                detail_txt.text = "아직은 칭찬이 어색한 고래"
                                level_num.setImageResource(R.drawable.level0)
                                textViewPhraseGod.text = it.data.levelUpNeedCount.toString()

                            }
                            1 -> {
                                level_whale.setImageResource(R.drawable.lv_1_img_whale)
                                detail_txt.text = "칭찬에 흥미가 생긴 고래"
                                level_num.setImageResource(R.drawable.level1)
                                textViewPhraseGod.text = it.data.levelUpNeedCount.toString()

                            }
                            2 -> {
                                level_whale.setImageResource(R.drawable.lv_2_img_whale)
                                detail_txt.text = "칭찬에 익숙해진 고래"
                                level_num.setImageResource(R.drawable.level2)
                                textViewPhraseGod.text = it.data.levelUpNeedCount.toString()

                            }
                            3 -> {
                                level_whale.setImageResource(R.drawable.lv_3_img_whale)
                                detail_txt.text = "슬슬 리듬타기 시작한 고래"
                                level_num.setImageResource(R.drawable.level3)
                                textViewPhraseGod.text = it.data.levelUpNeedCount.toString()

                            }
                            4 -> {
                                level_whale.setImageResource(R.drawable.lv_4_img_whale)
                                detail_txt.text = "신나게 춤추는 고래"
                                level_num.setImageResource(R.drawable.level4)
                                textViewPhraseGod.text = it.data.levelUpNeedCount.toString()

                            }
                            5 -> {
                                level_whale.setImageResource(R.drawable.lv_5_img_whale)
                                detail_txt.text = "춤신 춤왕 만렙 고래"
                                level_num.setImageResource(R.drawable.level5)
                                textViewPhraseGod.text = it.data.nickName+"님은 이제 칭찬의 신!"

                            }

                        }

                    }
            }
        })
    }

}

