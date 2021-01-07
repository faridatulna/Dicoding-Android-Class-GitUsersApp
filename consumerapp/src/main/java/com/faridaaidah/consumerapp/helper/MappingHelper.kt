package com.faridaaidah.consumerapp.helper

import android.database.Cursor
import com.faridaaidah.consumerapp.database.UserContract
import com.faridaaidah.consumerapp.model.UserModel

object MappingHelper {
    fun mapCursorToList(usersCursor: Cursor?): ArrayList<UserModel> {
        val favUserList = ArrayList<UserModel>()

        usersCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(UserContract.UserColumns.ID))
                val username = getString(getColumnIndexOrThrow(UserContract.UserColumns.USERNAME))
                val avatar = getString(getColumnIndexOrThrow(UserContract.UserColumns.AVATAR_URL))
                val url = getString(getColumnIndexOrThrow(UserContract.UserColumns.URL))
                favUserList.add(UserModel(username, avatar, url , id))
            }
        }
        return favUserList
    }

    fun mapCursorToObject(notesCursor: Cursor?): UserModel {
        var user = UserModel()
        notesCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(UserContract.UserColumns.ID))
            val username = getString(getColumnIndexOrThrow(UserContract.UserColumns.USERNAME))
            val avatar = getString(getColumnIndexOrThrow(UserContract.UserColumns.AVATAR_URL))
            val url = getString(getColumnIndexOrThrow(UserContract.UserColumns.URL))
            user = UserModel(username, avatar, url , id)
        }
        return user
    }
}