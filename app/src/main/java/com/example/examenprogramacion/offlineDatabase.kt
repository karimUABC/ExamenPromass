package com.example.examenprogramacion

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class offlineDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "libro_database"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "libros"
        const val COLUMN_ID = "id"
        const val COLUMN_AUTOR = "autor"
        const val COLUMN_TITULO = "titulo"
        const val COLUMN_FECHA = "fecha"
        const val COLUMN_CONTENIDO = "contenido"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_AUTOR TEXT," +
                "$COLUMN_TITULO TEXT," +
                "$COLUMN_FECHA TEXT," +
                "$COLUMN_CONTENIDO TEXT" +
                ")"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}