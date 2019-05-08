package com.sanitcode.footballmatchdb.fragment

import com.google.gson.Gson
import com.sanitcode.footballmatchdb.api.ApiRepository
import com.sanitcode.footballmatchdb.api.APIResponse
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MatchPresenter(private val view: MatchFragment, private val api: String, private val gson: Gson){

    fun getList(){
        view.showLoading()
        doAsync {
            val data = gson.fromJson(ApiRepository().doRequest(api), APIResponse::class.java)

            uiThread {
                view.hideLoading()
                view.showList(data.events)
            }
        }
    }
}