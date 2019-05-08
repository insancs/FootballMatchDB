package com.sanitcode.footballmatchdb.favorite

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favorite(
        val id: Long?,
        var eventId: String?,
        var eventName: String?,
        var eventDate: String?,
        var homeTeamId: String?,
        var homeTeam: String?,
        var homeScore: String?,
        var awayTeamId: String?,
        var awayTeam: String?,
        var awayScore: String?) : Parcelable {

    companion object {
        const val FAVORITE_TABLE = "FAVORITE_TABLE"
        const val ID = "ID_"
        const val EVENT_ID = "EVENT_ID"
        const val EVENT_NAME = "EVENT_NAME"
        const val EVENT_DATE = "EVENT_DATE"
        const val HOME_ID = "HOME_ID"
        const val HOME_NAME = "HOME_NAME"
        const val HOME_SCORE = "HOME_SCORE"
        const val AWAY_ID = "AWAY_ID"
        const val AWAY_NAME = "AWAY_NAME"
        const val AWAY_SCORE = "AWAY_SCORE"
    }
}