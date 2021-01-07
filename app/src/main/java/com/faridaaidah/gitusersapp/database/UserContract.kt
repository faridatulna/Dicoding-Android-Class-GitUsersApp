package com.faridaaidah.gitusersapp.database

import android.net.Uri
import android.provider.BaseColumns

object UserContract {
    const val AUTHORITY = "com.faridaaidah.gitusersapp"
    const val SCHEME = "content"

    class UserColumns : BaseColumns {
        companion object {
            const val TABLE_USER = "user_table"
            const val ID = "id"
            const val USERNAME = "username"
            const val AVATAR_URL = "avatar_url"
            const val URL = "url"

            // untuk membuat URI content://com.faridaaidah.gitusersapp/note
            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_USER)
                .build()
        }
    }
}