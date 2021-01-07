package com.faridaaidah.gitusersapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.constraintlayout.widget.Constraints
import com.faridaaidah.gitusersapp.database.UserContract.UserColumns.Companion.TABLE_USER
import com.faridaaidah.gitusersapp.database.UserContract.UserColumns.Companion.ID
import com.faridaaidah.gitusersapp.database.UserContract.UserColumns.Companion.USERNAME
import com.faridaaidah.gitusersapp.database.UserContract.UserColumns.Companion.AVATAR_URL
import com.faridaaidah.gitusersapp.database.UserContract.UserColumns.Companion.URL
import com.faridaaidah.gitusersapp.model.UserModel

class UserHelper(context: Context) {
    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_USER
        private var userHelper: UserHelper? = null

        fun getDatabase(context: Context): UserHelper =
            userHelper ?: synchronized(this) {
                userHelper ?: UserHelper(context)
            }
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$ID ASC")
    }

    fun queryById(id: String) : Cursor{
        return database.query(
            DATABASE_TABLE,
            null,
            "$ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null)
    }

    fun delete(id: String): Int{
        return database.delete(DATABASE_TABLE, "$ID = '$id'", null)
    }

    fun insert(contentValues: ContentValues?) : Long{
        return database.insert(DATABASE_TABLE, null, contentValues)
    }

    fun update(id: String, contentValues: ContentValues?) : Int{
        return database.update(DATABASE_TABLE, contentValues, "$ID = ?", arrayOf(id))
    }

    fun checkUser(id: String): Boolean {
        database = dataBaseHelper.writableDatabase
        val selectId =
            "SELECT * FROM $DATABASE_TABLE WHERE $ID =?"
        val cursor =
            database.rawQuery(selectId, arrayOf(id))
        var check = false
        if (cursor.moveToFirst()) {
            check = true
            var i = 0
            while (cursor.moveToNext()) {
                i++
            }
            Log.d(Constraints.TAG, String.format("%d records found", i))
        }
        cursor.close()
        return check
    }
}