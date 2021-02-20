package com.faridaaidah.consumerapp.adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.faridaaidah.consumerapp.R
import com.faridaaidah.consumerapp.fragment.FollowerFragment
import com.faridaaidah.consumerapp.fragment.FollowingFragment

//const val BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT = 1

class PagerAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var username: String? = null

    @StringRes
    private val tabTitles = intArrayOf(R.string.follower, R.string.following)

    override fun getItem(pos: Int): Fragment {
        var fragment: Fragment? = null
        when (pos) {
            0 -> fragment = FollowerFragment.newinstance(username)
            1 -> fragment = FollowingFragment.newinstance(username)
        }

        return fragment as Fragment
    }

    @Nullable
    override fun getPageTitle(pos: Int): CharSequence {
        return mContext.resources.getString(tabTitles[pos])
    }

    override fun getCount(): Int {
        return tabTitles.size
    }
}