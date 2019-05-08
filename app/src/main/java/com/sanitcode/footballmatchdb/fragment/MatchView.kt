package com.sanitcode.footballmatchdb.fragment

import com.sanitcode.footballmatchdb.model.MatchModel

interface MatchView {
    fun showLoading()
    fun hideLoading()
    fun showList(data: List<MatchModel>)
}