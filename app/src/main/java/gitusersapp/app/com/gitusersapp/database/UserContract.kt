package gitusersapp.app.com.gitusersapp.database

import android.provider.BaseColumns

internal class UserContract {
    internal class UserColumns : BaseColumns {
        companion object{
            const val TABLE_USER = "user_table"
            const val ID = "id"
            const val USERNAME = "username"
            const val AVATAR_URL = "avatar_url"
            const val URL = "url"
        }
    }
}