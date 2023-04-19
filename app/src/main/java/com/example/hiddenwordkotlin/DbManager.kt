package com.example.hiddenwordkotlin

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbManager(context: Context):SQLiteOpenHelper(context, tableName,null, version) {

    companion object {
        private const val tableName="Dictionnary"
        private const val version = 1
        private const val id = "id"
        private const val colWord="words"
    }

    init {
        val list = context.resources.getStringArray(R.array.words)
        for (i in list) {
            if (!this.readdb().contains(i)) this.addWord(i)
        }

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE $tableName($id INTEGER  PRIMARY KEY AUTOINCREMENT,$colWord TEXT)"
    db?.execSQL(query)
    }

    fun addWord(word: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(colWord,word)
        db.insert(tableName,null,contentValues)
        db.close()

    }

    fun deletWord(word: String) {
        val db = this.writableDatabase
        db.delete(tableName,"words=?", arrayOf(word))
        db.close()

    }

    fun readdb(): MutableList<String> {
        val db = this.readableDatabase
        val list = mutableListOf<String>()
        val cursor = db.rawQuery("SELECT * FROM $tableName",null)
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(1))
            }
                while(cursor.moveToNext())
        }
        cursor.close()

        return list
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $tableName  ")
        onCreate(db)
    }

}
