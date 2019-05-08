package com.sanitcode.footballmatchdb.activity.detail

import com.sanitcode.footballmatchdb.model.MatchModel

interface DetailView {
    fun showLoading()
    fun hideloading()
    fun getDetail(matchDetail: MatchModel, hometeam: MatchModel, awayTeam: MatchModel)
}