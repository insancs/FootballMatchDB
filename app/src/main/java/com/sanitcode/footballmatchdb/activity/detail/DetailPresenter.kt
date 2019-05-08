package com.sanitcode.footballmatchdb.activity.detail

import com.google.gson.Gson
import com.sanitcode.footballmatchdb.api.ApiRepository
import com.sanitcode.footballmatchdb.api.APIResponse
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class DetailPresenter (private val view: DetailActivity, private val apiMatchDetail: String,
                       private val apiHomeTeam: String, private val apiAwayTeam: String, private val gson: Gson){

    fun getEventDetail(){
        view.showLoading()
        doAsync {
            val matchDetail = gson.fromJson(ApiRepository().doRequest(apiMatchDetail), APIResponse::class.java)
            val homeTeam = gson.fromJson(ApiRepository().doRequest(apiHomeTeam), APIResponse::class.java)
            val awayTeam = gson.fromJson(ApiRepository().doRequest(apiAwayTeam), APIResponse::class.java)

            uiThread {
                view.hideloading()
                view.getDetail(matchDetail.events[0], homeTeam.teams[0], awayTeam.teams[0])
            }
        }
    }
}