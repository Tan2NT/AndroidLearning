package com.tanhoanngoc.glofttest.sqlite

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteStatement

class ItemDatabase (var context: Context, var DATABASE_NAME: String, var DATA_VERSION : Int, var factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATA_VERSION){
    override fun onCreate(db: SQLiteDatabase?) {
        return
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        return
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun queryData(sql : String){
        var data : SQLiteDatabase = writableDatabase
        data.execSQL(sql)
    }

    fun getData(sql : String): Cursor{
        var data : SQLiteDatabase = readableDatabase
        return data.rawQuery(sql, null)
    }

    fun insertItem(name : String, detail: String, image : ByteArray? ){
        var data : SQLiteDatabase = writableDatabase
        var sql : String = "INSERT INTO Item VALUES(null, ?, ?, ?)"
        var statement : SQLiteStatement = data.compileStatement(sql)
        statement.clearBindings()

        statement.bindString(1, name)
        statement.bindString(2, detail)
        statement.bindBlob(3, image)

        statement.executeInsert()
    }

}