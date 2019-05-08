package com.sanitcode.footballmatchdb.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.Typeface.BOLD
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sanitcode.footballmatchdb.R
import com.sanitcode.footballmatchdb.model.MatchModel
import com.sanitcode.footballmatchdb.model.changeFormatDate
import com.sanitcode.footballmatchdb.model.stringToDate
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView
import org.jetbrains.anko.sdk25.coroutines.onClick

class MatchAdapter(private val matchList: List<MatchModel>, private val listener: (MatchModel) -> Unit) : RecyclerView.Adapter<MatchAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MatchListUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun getItemCount() = matchList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(matchList[position], listener)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val matchDate: TextView = itemView.find(R.id.date)
        private val homeTeam: TextView = itemView.find(R.id.homeTeam)
        private val homeScore: TextView = itemView.find(R.id.homeScore)
        private val awayTeam: TextView = itemView.find(R.id.awayTeam)
        private val awayScore: TextView = itemView.find(R.id.awayScore)

        fun bindItem(schedule: MatchModel, listener: (MatchModel) -> Unit) {
            val date = stringToDate(schedule.eventDate)
            matchDate.text = changeFormatDate(date)
            homeTeam.text = schedule.homeTeam
            homeScore.text = schedule.homeScore
            awayTeam.text = schedule.awayTeam
            awayScore.text = schedule.awayScore

            itemView.onClick {
                listener(schedule)
            }
        }
    }

    class MatchListUI : AnkoComponent<ViewGroup> {
        @SuppressLint("RtlHardcoded")
        override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
            cardView {
                lparams(width = matchParent, height = wrapContent) {
                    gravity = Gravity.CENTER
                    margin = dip(4)
                    radius = 8f
                }
                verticalLayout {
                    textView("Date") {
                        id = R.id.date
                        textSize = 14f
                        textColor = Color.BLACK
                        topPadding = dip(12)
                        bottomPadding = dip(16)
                    }.lparams(width = wrapContent, height = wrapContent) {
                        gravity = Gravity.CENTER
                        margin = dip(12)
                    }

                    relativeLayout {

                        textView("homeTeam") {
                            id = R.id.homeTeam
                            textSize = 18f
                            BOLD
                            textColor = Color.BLACK
                            gravity = Gravity.CENTER
                        }.lparams(width = wrapContent, height = wrapContent) {
                            leftOf(R.id.homeScore)
                            rightMargin = dip(10)
                        }

                        textView("homeScore") {
                            id = R.id.homeScore
                            textSize = 14f
                            gravity = Gravity.CENTER
                            textColor = Color.BLACK
                        }.lparams(width = wrapContent, height = wrapContent) {
                            leftOf(R.id.vs)
                        }

                        textView("VS") {
                            id = R.id.vs
                            textSize = 14f
                            textColor = Color.BLACK
                            gravity = Gravity.CENTER
                        }.lparams(width = wrapContent, height = wrapContent) {
                            centerInParent()
                            leftMargin = dip(6)
                            rightMargin = dip(6)
                        }

                        textView("awayScore") {
                            id = R.id.awayScore
                            textSize = 14f
                            gravity = Gravity.CENTER
                            textColor = Color.BLACK
                        }.lparams(width = wrapContent, height = wrapContent) {
                            rightOf(R.id.vs)
                        }

                        textView("awayTeam") {
                            id = R.id.awayTeam
                            textSize = 18f
                            Typeface.BOLD
                            textColor = Color.BLACK
                            gravity = Gravity.CENTER
                        }.lparams(width = wrapContent, height = wrapContent) {
                            rightOf(R.id.awayScore)
                            leftMargin = dip(10)
                        }

                    }.lparams(width = matchParent, height = wrapContent)

                }.lparams(width = matchParent, height = wrapContent) {
                    gravity = Gravity.CENTER
                    bottomMargin = dip(12)
                }
            }
        }
    }
}