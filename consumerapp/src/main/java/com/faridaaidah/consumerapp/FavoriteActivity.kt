package com.faridaaidah.consumerapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.faridaaidah.consumerapp.adapter.FavoriteAdapter
import com.faridaaidah.consumerapp.database.UserContract.UserColumns.Companion.CONTENT_URI
import com.faridaaidah.consumerapp.databinding.ActivityFavoriteBinding
import com.faridaaidah.consumerapp.helper.MappingHelper
import com.faridaaidah.consumerapp.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.setHasFixedSize(true)
        favoriteAdapter = FavoriteAdapter()
        binding.rvUser.adapter = favoriteAdapter

        if (savedInstanceState == null) {
            loadData()
        }
    }

    private fun loadData() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            val deferredUsers = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToList(cursor)
            }
            binding.progressBar.visibility = View.INVISIBLE
            val users: ArrayList<UserModel>
            users = deferredUsers.await()
            if (users.isNotEmpty()) {
                favoriteAdapter.listFavorite = users
            } else {
                binding.signNoData.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.action_bar_menu2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_user -> {
                startActivity(Intent(this, MainActivity::class.java))
                return true
            }
            R.id.nav_set -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
        }
        return true
    }
}