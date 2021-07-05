package com.example.afinal

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $TABLE_NAME " + "($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_NOMBRE TEXT, $COLUMN_TEL TEXT, $COLUMN_DIR TEXT, $COLUMN_EMAIL TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertRow(nombre: String, telefono: String, direccion: String, email: String) {
        val values = ContentValues()
        values.put(COLUMN_NOMBRE, nombre)
        values.put(COLUMN_TEL, telefono)
        values.put(COLUMN_DIR, direccion)
        values.put(COLUMN_EMAIL, email)

        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateRow(row_id: String, nombre: String, telefono: String, direccion: String, email: String) {
        val values = ContentValues()
        values.put(COLUMN_NOMBRE, nombre)
        values.put(COLUMN_TEL, telefono)
        values.put(COLUMN_DIR, direccion)
        values.put(COLUMN_EMAIL, email)

        val db = this.writableDatabase
        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(row_id))
        db.close()
    }

    fun deleteRow(row_id: String) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(row_id))
        db.close()
    }

    fun getAllRow(buscar: String): Cursor? {
        val db = this.readableDatabase
        if (buscar.isNotEmpty()) {
            return db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_NOMBRE LIKE ?",  arrayOf(buscar+"%"))
        }
        else {
            return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        }
    }

    fun getAllRowDesc(buscar: String): Cursor? {
        val db = this.readableDatabase
        if (buscar.isNotEmpty()) {
            return db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_NOMBRE LIKE ?  ORDER BY UPPER($COLUMN_NOMBRE) DESC",  arrayOf(buscar+"%"))
        }
        else {
            return db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY UPPER($COLUMN_NOMBRE) DESC", null)
        }
    }

    fun getAllRowAsc(buscar: String): Cursor? {
        val db = this.readableDatabase
        if (buscar.isNotEmpty()) {
            return db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_NOMBRE LIKE ?  ORDER BY UPPER($COLUMN_NOMBRE) ASC",  arrayOf(buscar+"%"))
        }
        else {
            return db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY UPPER($COLUMN_NOMBRE) ASC", null)
        }
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "myDBfile.db"
        const val TABLE_NAME = "persona"

        const val COLUMN_ID = "id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_TEL = "telefono"
        const val COLUMN_DIR = "direccion"
        const val COLUMN_EMAIL = "email"
    }
}