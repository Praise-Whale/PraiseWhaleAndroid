package com.example.praisewhale.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.praisewhale.CollectionImpl
import com.example.praisewhale.R
import com.example.praisewhale.ResponseCollectionData
import com.example.praisewhale.ResponseUserData
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
        return inflater.inflate(R.layout.fragment_praise_level, container, false)
    }

    override fun onStart() {
        super.onStart()

        val call : Call<ResponseUserData> = CollectionImpl.service.getuserIdx(1)
        call.enqueue(object : Callback<ResponseUserData> {
            override fun onFailure(call: Call<ResponseUserData>, t: Throwable) {
                Log.d("tag", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<ResponseUserData>,
                response: Response<ResponseUserData>
            ) {

                response.takeIf { it.isSuccessful }

                    ?.body()
                    ?.let {

                            it ->

                        //level_txt.text = it.data.userLevel.toString()

                        //level_txt.text="칭찬 고래 "+it.data.userLevel.toString()+" 단계"
                        when(it.data.userLevel){
                            1->{
                                level_txt.text="아직은 칭찬이 어색한 고래"
                                level_whale.setImageResource(R.drawable.ic_whale_level_1)
                                detail_txt.text = "칭찬 "+it.data.needLikeCount.toString()+"" +
                                        "번만 더 하면\n" +
                                        "칭찬에 익숙해진 고래가 될 고래!"
                            }
                            2->{level_whale.setImageResource(R.drawable.ic_whale_level_2_1)
                                detail_txt.text = "칭찬 "+it.data.needLikeCount.toString()+"" +
                                        "번만 더 하면\n"+" 슬슬 리듬타기 시작한 고래가 될 고래!"
                                level_txt.text="칭찬이 익숙해진 고래"


                            }

                            3->{level_whale.setImageResource(R.drawable.ic_whale_level_3)
                                detail_txt.text = "칭찬 "+it.data.needLikeCount.toString()+"" +
                                        "번만 더 하면\n" +
                                        "춤 추는 고래가 될 고래!"
                                level_txt.text="슬슬 리듬타기 시작한 고래"

                            }
                            4->{level_whale.setImageResource(R.drawable.ic_whale_level_4)
                                detail_txt.text = "칭찬 "+it.data.needLikeCount.toString()+"" +
                                        "번만 더 하면\n" +
                                        "춤신 고래가 될 고래!"
                                level_txt.text="춤추는 고래"

                            }
                            5->{level_whale.setImageResource(R.drawable.ic_whale_level_5)
                                detail_txt.text = "당신은 이제 만랩이지 고랩!"
                                level_txt.text="춤신 고래"


                            }


                        }
                        //detail_txt.text = "칭찬 "+it.data.needLikeCount.toString()+"번만 더 하면 춤추는 고래!"


                    }
            }
        })
    }
}

