package com.sanitcode.footballmatchdb.favorite

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.sanitcode.footballmatchdb.R
import com.sanitcode.footballmatchdb.activity.detail.DetailActivity
import com.sanitcode.footballmatchdb.db.database
import com.sanitcode.footballmatchdb.model.MatchModel
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class FavoriteFragment : Fragment(), AnkoComponent<Context> {

    private var favorites: MutableList<Favorite> = mutableListOf()
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var eventList: RecyclerView
    private lateinit var adapter: FavoriteAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = FavoriteAdapter(favorites){
            val event = MatchModel(
                    eventId = it.eventId,
                    eventDate = it.eventDate,
                    eventName = it.eventName,
                    homeTeamId = it.homeTeamId,
                    homeTeam = it.homeTeam,
                    homeScore = it.homeScore,
                    awayTeamId = it.awayTeamId,
                    awayTeam = it.awayTeam,
                    awayScore = it.awayScore
            )
            ctx.startActivity<DetailActivity>("EVENT" to event)
        }
        eventList.adapter = adapter

        showFavorite()
        swipeRefresh.onRefresh {
            favorites.clear()
            showFavorite()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = createView(AnkoContext.create(ctx))

    override fun createView(ui: AnkoContext<Context>) = with(ui){
        verticalLayout {
            lparams(width = matchParent, height = wrapContent)
            topPadding = dip(16)
            leftPadding = dip(16)
            rightPadding = dip(16)

            swipeRefresh = swipeRefreshLayout {
                setColorSchemeResources(R.color.colorAccent,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light
                )

                eventList = recyclerView {
                    lparams(width = matchParent, height = wrapContent)
                    layoutManager = LinearLayoutManager(ctx)
                }
            }

        }
    }

    private fun showFavorite(){
        context?.database?.use {
            swipeRefresh.isRefreshing = false
            val result = select(Favorite.FAVORITE_TABLE)
            val favorite = result.parseList(classParser<Favorite>())
            favorites.addAll(favorite)
            adapter.notifyDataSetChanged()
        }
    }
}// Required empty public constructor
