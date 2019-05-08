package com.sanitcode.footballmatchdb.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.sanitcode.footballmatchdb.favorite.Favorite
import org.jetbrains.anko.db.*

class DatabaseHelper (ctx: Context): ManagedSQLiteOpenHelper(ctx, "FavoriteSchedule.db", null ) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable(Favorite.FAVORITE_TABLE, true,
                Favorite.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                Favorite.EVENT_ID to TEXT + UNIQUE,
                Favorite.EVENT_NAME to TEXT,
                Favorite.EVENT_DATE to TEXT,
                Favorite.HOME_ID to TEXT + UNIQUE,
                Favorite.HOME_NAME to TEXT,
                Favorite.HOME_SCORE to TEXT,
                Favorite.AWAY_ID to TEXT + UNIQUE,
                Favorite.AWAY_NAME to TEXT,
                Favorite.AWAY_SCORE to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.dropTable(Favorite.FAVORITE_TABLE, true)
    }

    companion object {
        private var instance: DatabaseHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): DatabaseHelper{
            if (instance==null){
                instance = DatabaseHelper(ctx.applicationContext)

            }
            return  instance as DatabaseHelper
        }

    }
}
val Context.database: DatabaseHelper
    get() = DatabaseHelper.getInstance(applicationContext)