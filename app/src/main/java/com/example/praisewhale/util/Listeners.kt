package com.example.praisewhale.util

import android.view.View

/* HomeDialogDoneFragment.kt - 최근 칭찬한 사람 리사이클러뷰 아이템 클릭 인터페이스 */
interface RecentPraiseToClickListener {
    fun onClickRecentPraiseToItem(recentPraiseTo: String)
}

/* PraiseRankingFragment.kt - 칭찬 랭킹 아이템 클릭 인터페이스 */
interface PraiseRankingClickListener {
    fun onClickPraiseRankingItem(praiseTo: String)
}