package com.example.praisewhale.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.praisewhale.*
import com.example.praisewhale.util.MyApplication
import kotlinx.android.synthetic.main.fragment_praise_level.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PraiseLevelFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var View =inflater.inflate(R.layout.fragment_praise_level, container, false)

        return View
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setting_btn.setOnClickListener {


            val intent= Intent(context,LevelInfoActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        val username = MyApplication.mySharedPreferences.getValue("nickName","")
        val whaleName = MyApplication.mySharedPreferences.getValue("whaleName","")

<<<<<<< HEAD
        level_txt.text=username+"님의"
        whalename_txt.text=whaleName

=======
>>>>>>> 749fe9ffab338f2b2e153c5f2079638571965655
        val token = MyApplication.mySharedPreferences.getValue("token","")
        val call : Call<ResponselevelData> = CollectionImpl.service.getlevelcount(token)
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




                        textView2.text=it.data.praiseCount.toString()+"번"
                        cpb_circlebar.progress = it.data.praiseCount*10





                        when(it.data.userLevel){
                             0-> {level_whale.setImageResource(R.drawable.lv_0_img_whale)
                               detail_txt.text="아직은 칭찬이 어색한 고래"}
                            1-> {level_whale.setImageResource(R.drawable.lv_1_img_whale)
                                detail_txt.text="칭찬에 흥미가 생긴 고래"}
                            2-> {level_whale.setImageResource(R.drawable.lv_2_img_whale)
                                detail_txt.text="칭찬에 익숙해진 고래"}
                            3-> {level_whale.setImageResource(R.drawable.lv_3_img_whale)
                                detail_txt.text="슬슬 리듬타기 시작한 고래"}
                            4-> {level_whale.setImageResource(R.drawable.lv_4_img_whale)
                                detail_txt.text="신나게 춤추는 고래"}
                            5-> {level_whale.setImageResource(R.drawable.lv_5_img_whale)
                                detail_txt.text="춤신 춤왕 만렙 고래"}

                        }
                        textView3.text=it.data.levelUpNeedCount.toString()+"번 달성시 다음레벨"
                        //level_txt.text = it.data.userLevel.toString()

                        //level_txt.text="칭찬 고래 "+it.data.userLevel.toString()+" 단계"

                        //detail_txt.text = "칭찬 "+it.data.needLikeCount.toString()+"번만 더 하면 춤추는 고래!"


                    }
            }
        })
    }
}

