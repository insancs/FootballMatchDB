package com.sanitcode.footballmatchdb.activity.detail

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteConstraintException
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import com.sanitcode.footballmatchdb.R
import com.sanitcode.footballmatchdb.R.color.colorAccent
import com.sanitcode.footballmatchdb.R.drawable.ic_favorite
import com.sanitcode.footballmatchdb.R.drawable.ic_favorite_border
import com.sanitcode.footballmatchdb.db.database
import com.sanitcode.footballmatchdb.favorite.Favorite
import com.sanitcode.footballmatchdb.model.MatchModel
import com.sanitcode.footballmatchdb.api.APISportDB
import com.sanitcode.footballmatchdb.model.changeFormatDate
import com.sanitcode.footballmatchdb.model.stringToDate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.detail_goals.*
import kotlinx.android.synthetic.main.detail_lineup.*
import kotlinx.android.synthetic.main.detail_match.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.support.v4.onRefresh

class DetailActivity : AppCompatActivity(), DetailView {

    private lateinit var match: MatchModel
    private lateinit var presenter: DetailPresenter

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        match = intent.getParcelableExtra("EVENT")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Match Detail"

        favoriteState()

        val matchData = APISportDB(match.eventId).getMatchDetail()
        val homeTeam = APISportDB(match.homeTeamId).getTeamDetail()
        val awayTeam = APISportDB(match.awayTeamId).getTeamDetail()
        val gson = Gson()
        presenter = DetailPresenter(this, matchData, homeTeam, awayTeam, gson)
        presenter.getEventDetail()

        swipe_match.onRefresh {
            presenter.getEventDetail()
        }
        swipe_match.setColorSchemeColors(colorAccent,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light)
    }

    override fun showLoading() {
        swipe_match.isRefreshing = true
    }

    override fun hideloading() {
        swipe_match.isRefreshing = false
    }

    override fun getDetail(matchDetail: MatchModel, hometeam: MatchModel, awayTeam: MatchModel) {
        val date = stringToDate(this.match.eventDate)
        textView_date.text = changeFormatDate(date)

        home_team.text = this.match.homeTeam
        home_score.text = this.match.homeScore

        away_team.text = this.match.awayTeam
        away_score.text = this.match.awayScore

        //Home Team
        Picasso.get().load(hometeam.teamBadge).into(home_image)
        home_team.text = matchDetail.homeTeam
        home_score.text = matchDetail.homeScore
        home_formattion.text = matchDetail.homeFormation
        home_goal.text = if (matchDetail.homeGoals.isNullOrEmpty()) "-" else matchDetail.homeGoals?.replace(";", ";\n")
        home_shot.text = matchDetail.homeShots ?: "-"
        home_goalkeeper.text = if (matchDetail.homeGoalKeeper.isNullOrEmpty()) "-" else matchDetail.homeGoalKeeper?.replace("; ", ";\n")
        home_defense.text = if (matchDetail.homeDefense.isNullOrEmpty()) "-" else matchDetail.homeDefense?.replace("; ", ";\n")
        home_midfield.text = if (matchDetail.homeMidfield.isNullOrEmpty()) "-" else matchDetail.homeMidfield?.replace("; ", ";\n")
        home_forward.text = if (matchDetail.homeForward.isNullOrEmpty()) "-" else matchDetail.homeForward?.replace("; ", ";\n")
        home_subs.text = if (matchDetail.homeSubstitutes.isNullOrEmpty()) "-" else matchDetail.homeSubstitutes?.replace("; ", ";\n")

        //Away Team
        Picasso.get().load(awayTeam.teamBadge).into(away_image)
        away_team.text = matchDetail.awayTeam
        away_score.text = matchDetail.awayScore
        away_formattion.text = matchDetail.awayFormation
        away_goal.text = if (matchDetail.awayGoals.isNullOrEmpty()) "-" else matchDetail.awayGoals?.replace(";", ";\n")
        away_shot.text = matchDetail.awayShots ?: "-"
        away_goalkeeper.text = if (matchDetail.awayGoalKeeper.isNullOrEmpty()) "-" else matchDetail.awayGoalKeeper?.replace("; ", ";\n")
        away_defense.text = if (matchDetail.awayDefense.isNullOrEmpty()) "-" else matchDetail.awayDefense?.replace("; ", ";\n")
        away_midfield.text = if (matchDetail.awayMidfield.isNullOrEmpty()) "-" else matchDetail.awayMidfield?.replace("; ", ";\n")
        away_forward.text = if (matchDetail.awayForward.isNullOrEmpty()) "-" else matchDetail.awayForward?.replace("; ", ";\n")
        away_subs.text = if (matchDetail.awaySubstitutes.isNullOrEmpty()) "-" else matchDetail.awaySubstitutes?.replace("; ", ";\n")
        hideloading()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_menu, menu)
        menuItem = menu
        setFavorite()

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            R.id.add_to_favorite -> {
                if(isFavorite) removeFromFavorite() else addToFavorite()

                isFavorite = !isFavorite
                setFavorite()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addToFavorite(){
        try {
            database.use {
                insert(Favorite.FAVORITE_TABLE,
                        Favorite.EVENT_ID to match.eventId,
                        Favorite.EVENT_NAME to match.eventName,
                        Favorite.EVENT_DATE to match.eventDate,
                        Favorite.HOME_ID to match.homeTeamId,
                        Favorite.HOME_NAME to match.homeTeam,
                        Favorite.HOME_SCORE to match.homeScore,
                        Favorite.AWAY_ID to match.awayTeamId,
                        Favorite.AWAY_NAME to match.awayTeam,
                        Favorite.AWAY_SCORE to match.awayScore)
            }
            snackbar(swipe_match, getString(R.string.add_favorite)).show()
        }catch (e: SQLiteConstraintException){
            snackbar(swipe_match, e.localizedMessage).show()
        }
    }

    private fun removeFromFavorite(){
        try {
            database.use{
                delete(Favorite.FAVORITE_TABLE, "(EVENT_ID = {id})", "id" to match.eventId.orEmpty())
            }
            snackbar(swipe_match, getString(R.string.remove_favorite)).show()
        } catch (e: SQLiteConstraintException){
            snackbar(swipe_match, e.localizedMessage).show()
        }
    }

    private fun setFavorite(){
        val icon = if(isFavorite) ic_favorite else ic_favorite_border

        menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, icon)
    }

    private fun favoriteState(){
        database.use {
            val result = select(Favorite.FAVORITE_TABLE)
                    .whereArgs("(EVENT_ID = {id})", "id" to match.eventId.orEmpty())
            val favorite = result.parseList(classParser<Favorite>())
            if (!favorite.isEmpty()) isFavorite = true
        }
    }
}
