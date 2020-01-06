package com.tanhoanngoc.glofttest.sqlite

import android.content.Context
import android.database.Cursor
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

public class Database(context : Context, DATABASE_NAME : String, factory : SQLiteDatabase.CursorFactory?, DATABASE_VERSION : Int) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION)
{
    override fun onCreate(db: SQLiteDatabase?) {
        return
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        return
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // query database but not return the value: CREATE, INSERT, UPDATE, DELETE ...
    fun QueryData(sql: String){
        var database : SQLiteDatabase = writableDatabase
        database.execSQL(sql)
    }

    // query and return the value: SELECT
    fun getData(sql : String) : Cursor{
        var database : SQLiteDatabase = readableDatabase
        return database.rawQuery(sql, null)
    }

}